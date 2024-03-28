import { Component, OnInit, ViewChild, OnDestroy } from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from "@angular/forms";
import { CategoryService } from "./../../../_services/category-service";
import { Subscription } from "rxjs";
import { first } from "rxjs/operators";
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../../_services/loader.service";
import { ManageuserService } from "../../../_services/manageuser-service";
import { MultilingualService } from "../../../_services/multilingual.service";

@Component({
  selector: "app-category-add-edit",
  templateUrl: "./category-add-edit.component.html",
  styleUrls: ["./category-add-edit.component.scss"],
})
export class CategoryAddEditComponent implements OnInit, OnDestroy {
  @ViewChild("catFrm", { static: true }) form: any;

  private _id: number;
  editMode = false;
  private editCategoryId: number;
  category_subject: string;
  category_body: string;
  topicListData: any;
  private routeSub: Subscription;
  private categorySub: Subscription;
  private categorySaveSub: Subscription;
  model = {
    name: {},
    description: {},
  };
  category_status = "Active";
  isOpen = false;
  addEditCmsForm: FormGroup;
  parent_category: any = { id: "", name: "" };
  parent_name: string;
  parent_id: number;
  parentCatList: any[] = [];
  private parentCategorySub: Subscription;
  languages = [];
  constructor(
    private route: ActivatedRoute,
    private categoryService: CategoryService,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private manageuserService: ManageuserService,
    private multilingualService: MultilingualService
  ) {
    this.languages = this.multilingualService.getLanguage();
  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params["id"];
      this.editMode = params["id"] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }

  openTree() {
    this.isOpen = this.isOpen === true ? false : true;
  }
  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      this.categorySub = this.categoryService
        .getCategoryById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            const resData = response.responseData;
            this.editCategoryId = resData.id || null;
            this.category_status = resData.status || "Active";
            this.parent_name = resData.parent_name || "";
            this.parent_id = resData.parent_id === 0 ? "0" : resData.parent_id;
            resData.translatable.forEach((element) => {
              this.model.name[element.locale] = element.name;
              this.model.description[element.locale] = element.description;
            });
            this.getParentCategory();
          },
          (error) => {
            console.log(error);
          }
        );
    } else {
      this.getParentCategory();
    }
  }
  /**
   * Create/Update Category data
   */
  public onCategorySave() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    if (Object.keys(this.model.description).length === 0) {
      this.languages.forEach((e) => {
        this.model.description[e.locale] = "";
      });
    }
    let translable: any[] = [];
    let obj1 = {
      name: this.model.name["en"],
      description: this.model.description["en"],
      locale: "en",
    };
    let obj2 = {
      name: this.model.name["ar"],
      description: this.model.description["ar"],
      locale: "ar",
    };
    translable = [obj2, obj1];

    var data;
    if (this.parent_category.id != "0") {
      data = {
        translable: translable,
        parent_id: this.parent_category.id,
        status: this.category_status,
      };
    } else {
      data = {
        translable: translable,
        status: this.category_status,
      };
    }
    if (this.editCategoryId) {
      this.updateCategory(data, this.editCategoryId);
    } else {
      // if (!data.parent_id) {
      //   data.parent_id = "";
      // }
      this.createCategory(data);
      console.log(data);
    }
  }

  private createCategory(formData) {
    this.loader.showLoader();
    this.categorySaveSub = this.categoryService
      .createCategory(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status == "SUCCESS") {
            this.toastr.success(data.responseData);
            this.router.navigate(["/category/list"]);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/category/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  private updateCategory(formData, id) {
    this.loader.showLoader();
    this.categorySaveSub = this.categoryService
      .updateCategory(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status == "SUCCESS") {
            this.toastr.success(data.responseData);
            this.router.navigate(["/category/list"]);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/category/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  /**
   * Get parent category list
   * @param catId parent category id
   */
  private getParentCategory() {
    this.parentCatList = [];
    const parentName = this.parent_name ? this.parent_name : "0";
    this.parentCategorySub = this.categoryService
      .getParentCategoryList("")
      .pipe(first())
      .subscribe(
        (data) => {
          const newValue = { id: "0", name: "Select Category" };
          this.parentCatList = data.responseData;
          this.parentCatList.splice(0, 0, newValue);
          if (this._id) {
            let id = Number(this._id);
            this.parentCatList = this.filterChildren(this.parentCatList, id);
          }
          if (this._id) {
            this.parent_category = {
              id: this.parent_id,
              name: this.parent_name,
            };
          } else {
            this.parent_category = this.parentCatList[0];
          }
        },
        (error) => {
          const errorData = error;
          if (errorData && errorData.meta) {
            this.toastr.error(errorData.meta.message);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.categorySub.unsubscribe();
    }
  }

  filterChildren(arr, id) {
    return arr
      .filter((el) => el.id !== id)
      .map((el) => {
        if (!el.children || !el.children.length) {
          return el;
        }
        el.children = this.filterChildren(el.children, id);
        return el;
      });
  }
}
