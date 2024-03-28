import { Component, OnInit, ViewChild, AfterViewInit } from "@angular/core";
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
import { TreeviewItem, TreeviewConfig } from "ngx-treeview";
import { OnDestroy, TemplateRef } from "@angular/core";
import { Role } from "./../../../model/role";
import { Subject } from "rxjs";
import { Title } from "@angular/platform-browser";

@Component({
  selector: "app-category-treeview",
  templateUrl: "./category-treeview.component.html",
  styleUrls: ["./category-treeview.component.scss"],
})
export class CategoryTreeviewComponent implements OnInit {
  roleLoading: Boolean = false;
  submitted: Boolean = false;
  allCategory: any[];
  private newArray: any = [];
  private finalArray: any[];
  options = {
    allowDrag: true,
    allowDrop: true,
  };
  private isPermissionSub: Boolean = false;

  constructor(
    private categoryService: CategoryService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit() {
    this.getCategoryTreeData();
  }
  /**
   * Get category data in tree format
   */
  private getCategoryTreeData() {
    this.categoryService.getCategoryTreeview().subscribe((data) => {
      this.allCategory = data.responseData;
    });
  }

  onMoveNode(event) {
    console.log(event);
  }

  private myFun(categoryDt, parentId) {
    const that = this;
    categoryDt.forEach(function (value, key) {
      that.newArray.push({
        id: value.id,
        parent_id: parentId == 0 ? "" : parentId,
      });
      if (
        value.children &&
        value.children != null &&
        value.children.length > 0
      ) {
        that.myFun(value.children, value.id);
      }
    });
  }
  /**
   * Save category
   */
  public saveCategoryData() {
    this.myFun(this.allCategory, 0);
    const bodyData = {
      categories: this.newArray,
    };
    this.categoryService
      .saveCategoryTreeviewData(this.newArray)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/category/treeview"]);
          }
        },
        (error) => {
          const errorData = error;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              if (errorData.errors.page_title) {
                this.toastr.error(errorData.errors.page_title[0]);
              }
            } else {
              this.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/category/treeview"]);
            this.toastr.error("Something went wrong please try again.");
          }
          this.submitted = false;
        }
      );
  }
}
