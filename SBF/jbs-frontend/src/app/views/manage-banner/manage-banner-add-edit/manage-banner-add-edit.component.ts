import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  NgModule,
  OnDestroy,
} from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { first } from "rxjs/operators";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { CookieService } from "ngx-cookie-service";
import * as moment from "moment";
import { LoaderService } from "../../../_services/loader.service";
import { ImageCroppedEvent } from "ngx-image-cropper";
import { ManageBannerService } from "../../../_services/manage-banner.service";
import { MultilingualService } from "../../../_services/multilingual.service";

@Component({
  selector: "app-manage-banner-add-edit",
  templateUrl: "./manage-banner-add-edit.component.html",
  styleUrls: ["./manage-banner-add-edit.component.scss"],
})
export class ManageBannerAddEditComponent implements OnInit, OnDestroy {
  url: String = "";
  noPicture: Boolean = false;
  status: any = {
    Active: "Active",
    Inactive: "Inactive",
  };

  @ViewChild("f", { static: true }) form: any;

  private _id: number;
  editMode = false;
  private editBannerId: number;
  submitted: Boolean = false;
  private routeSub: Subscription;
  private bannerData: Subscription;
  private bannerDataSave: Subscription;
  model = {
    banner_image: "",
    banner_title: {},
    banner_title_en: "",
    banner_title_ar: "",
    banner_status: "Active",
  };
  imageChangedEvent: any = "";
  isRemoveAPI: Boolean = false;
  languages = [];
  constructor(
    private route: ActivatedRoute,
    private manageBannerService: ManageBannerService,
    private router: Router,
    private toastr: ToastrService,
    private _eref: ElementRef,
    private cookieService: CookieService,
    private loader: LoaderService,
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

  /* Change and Upload Image in User Module */
  changeImage(event: any) {
    const img = event;
    if (event.target.files[0] !== undefined) {
      const imgSize = 2;
      let temp;
      const fileTypes = ["image/jpeg", "image/jpg", "image/png"];
      if (fileTypes.includes(event.target.files[0].type) === false) {
        this.toastr.error("Please upload a valid image.");
      } else {
        if (
          (temp = imgSize) === void 0 ||
          temp === "" ||
          event.target.files[0].size / 1024 / 1024 < imgSize
        ) {
          const reader = new FileReader();
          reader.onload = (ev: any) => {
            const image = new Image();
            image.src = ev.target.result;
            image.onload = () => {
              this.isRemoveAPI = false;
              this.imageChangedEvent = img;
              const banheight = image.height;
              const banwidth = image.width;
              if (banwidth >= 700 && banheight >= 400) {
                const ratio = (banwidth / banheight).toFixed(1);
                const heightSubset1 = (banwidth / 1.5).toFixed();
                const heightSubset2 = (banwidth / 1.6).toFixed();
                const heightSubset3 = (banwidth / 1.7).toFixed();
                const heightSubset4 = (banwidth / 1.8).toFixed();
                if (ratio >= "1.5" && ratio <= "1.8") {
                  this.noPicture = true;
                  this.url = ev.target.result;
                  this.model.banner_image = img.target.files[0];
                } else {
                  this.toastr.error(
                    `Image width is ${banwidth}px , the height of this image should be one of subset: [${heightSubset4}px, ${heightSubset3}px,${heightSubset2}px, ${heightSubset1}px]`
                  );
                }
              } else {
                this.toastr.error(
                  "Minimum image dimensions must be 700px*400px"
                );
              }
            };
          };
          reader.readAsDataURL(event.target.files[0]);
        } else {
          this.toastr.error(
            "File Size should be smaller than " + imgSize + "MB"
          );
        }
      }
    }
  }

  /* Remove Image in User Module */
  removePicture(upld_photo) {
    this.noPicture = false;
    upld_photo.value = null;
    this.model.banner_image = "";
    this.url = "";
  }
  openImageUpload() {
    document.getElementById("banner_picture").click();
  }

  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      /* Set the value to Cookies with Edit Mode */
      /* error TS2345:
       Argument of type 'number' is not assignable to parameter of type 'string'.
      */
      this.cookieService.set("get_id", "" + this._id + "");
      /* Set the value to Cookies with Edit Mode */
      this.bannerData = this.manageBannerService
        .getManageBannerById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            const resData = response.responseData;
            this.editBannerId = resData.id || null;
            this.model.banner_status = resData.status || "";
            this.model.banner_image = resData.image || "";
            this.model.banner_title_en = resData.titleEN;
            this.model.banner_title_ar = resData.titleAR;
            // resData.translations.forEach((element) => {
            //   this.model.banner_title[element.locale] = element.title;
            // });
            /* error - InvalidStateError: Failed to set the 'value' property on 'HTMLInputElement' */
            if (resData.banner_image) {
              this.url = resData.banner_image;
              this.noPicture = true;
              this.isRemoveAPI = true;
            } else {
              this.url = "assets/default-user-image.png";
            }
            /* error */
          },
          (error) => {
            console.log(error);
          }
        );
    } else {
      /* Set the value to Cookies without Edit Mode */
      this.cookieService.set("get_id", undefined);
      /* Set the value to Cookies without Edit Mode */
    }
  }

  /**
   * Create/Update banner data
   * @param frm for validate form
   */
  public onBannerDataSave(frm: NgForm) {
    this.submitted = true;
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }

    /* FormData for pushing the code with Image URL */
    const formData: {} = {};
    let title =
      "{ 'en' : '" +
      this.model.banner_title_en +
      "', 'ar' : '" +
      this.model.banner_title_ar +
      "' }";
    formData["banner_title"] = this.model.banner_title ? title : "";
    formData["status"] = this.model.banner_status;
    formData["userId"] = JSON.parse(localStorage.getItem("user")).userId;

    const file = (<HTMLInputElement>document.getElementById("banner_picture"))
      .files[0];

    if (file) {
      console.log(file.name);
      formData["banner_image"] = file.name;
    }

    if (this.editBannerId) {
      this.updateManageBanner(formData, this.editBannerId);
    } else {
      this.createManageBanner(formData);
    }
  }

  private createManageBanner(formData) {
    this.loader.showLoader();
    this.bannerDataSave = this.manageBannerService
      .createManageBanner(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/manage-banner/list"]);
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
            this.router.navigate(["/manage-banner/list"]);
            this.toastr.error("Something went wrong please try again.");
            this.toastr.error(errorData.meta.message);
          }
          this.submitted = false;
        }
      );
  }

  private updateManageBanner(formData, id) {
    this.loader.showLoader();
    this.bannerDataSave = this.manageBannerService
      .updateManageBanner(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/manage-banner/list"]);
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
            this.router.navigate(["/manage-banner/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
          this.submitted = false;
        }
      );
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.bannerData.unsubscribe();
    }
  }
}
