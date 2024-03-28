import { Component, OnInit, ViewChild, OnDestroy } from "@angular/core";
/* Settings Service for API configuration */
import { SettingsService } from "./../../_services/settings-service";
/* First Operations in RXJS */
import { first } from "rxjs/operators";
/* ngModel so used NgForm */
import { NgForm, Validator, NgModel } from "@angular/forms";
/* Toaster for Success and Error Message */
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../_services/loader.service";
import { CONFIGCONSTANTS } from "../../config/app-constants";
import { Subscription } from "rxjs";
import { LanguageService } from "../../_services/language.service";
import { TranslateService } from "@ngx-translate/core";
import { IDropdownSettings } from "ng-multiselect-dropdown/multiselect.model";

@Component({
  selector: "app-settings",
  templateUrl: "./settings.component.html",
  styleUrls: ["./settings.component.scss"],
})
export class SettingsComponent implements OnInit, OnDestroy {
  /* Type Defination */
  obj: any;
  logoURL: string;
  faviconURL: string;
  @ViewChild("f", { static: true }) form: any;
  private _logo_id: number;
  private _fav_id: number;
  site_name: string;
  tag_line: string;
  default_language: string;
  support_email: string;
  contact_email: string;
  profile_image: string;
  noLogo = false;
  noFav = false;
  address: string;
  contact_number: number;
  device_tracking: string;
  fb_pixel_code: string;
  google_tag: string;
  upload_image: string;
  /* this is required as we are passing all the data in Model */
  model: any = [];
  settingsData: any = [];
  settingsDataSave: any = [];
  setOfSets = [];
  submitted = false; // this is used form validate
  ImageData: any;
  readonly hotjarSettings: IDropdownSettings = {
    singleSelection: false,
    idField: "id",
    textField: "route_name",
    selectAllText: "Select All",
    unSelectAllText: "UnSelect All",
    itemsShowLimit: 3,
    allowSearchFilter: true,
    noDataAvailablePlaceholderText: "No Page Found",
    searchPlaceholderText: "Search Page name",
  };
  hotjar_script: string;
  hot_jar_pages_name: any = [];
  hotjar_ids: any = [];

  private subscription: Subscription;
  private Language = localStorage.getItem("lan") || "en";
  constructor(
    private settingsservice: SettingsService,
    private toastr: ToastrService,
    private loader: LoaderService,
    private languageSwitcher: LanguageService,
    private translate: TranslateService
  ) {
    this.subscription = this.languageSwitcher.getLanguage().subscribe((lan) => {
      this.translate.setDefaultLang(lan);
      this.translate.use(lan);
    });
    this.languageSwitcher.changeLanguage(this.Language);
  }

  ngOnInit() {
    this.model.upload_image = "";
    this.getSettingsData(); // Calling this function on loading page
    this.logoURL = "assets/img/brand/angular_logo.png";
    this.faviconURL = "assets/img/brand/angular_logo_small.png";
  }

  /**
   * get API Response for binding to HTML
   */
  private getSettingsData() {
    this.getImageData(); // Getting Image Response and Bind the same
    this.settingsservice
      .getSettingsDataURL()
      .pipe(first())
      .subscribe(
        (data) => {
          this.settingsData = data.responseData;
          const that = this;
          if (this.settingsData.length > 0) {
            this.settingsData.forEach(function (value, index) {
              that.getValue(
                value.optionName,
                value.optionValue,
                value.configurationId
              );
            });
          }

          // hot jar pages enable display selected
          const addEnablePage = [];
          if (this.hotjar_ids) {
            this.hotjar_ids.forEach((result) => {
              const key = this.hot_jar_pages_name.findIndex(
                (k) => k.id === result
              );
              if (key !== -1) {
                addEnablePage.push(this.hot_jar_pages_name[key]);
              }
            });
            this.model.hotjar_ids = addEnablePage;
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }
  /**
   * Get dynamic input value and set it
   * @param name site_name...
   * @param value get value by key name
   * @param id for logo and favicon
   */
  private getValue(name, value, id) {
    switch (name) {
      case "site_name": {
        this.model.site_name = value;
        break;
      }
      case "tag_line": {
        this.model.tag_line = value;
        break;
      }
      case "default_language": {
        this.model.default_language = value;
        break;
      }
      case "support_email": {
        this.model.support_email = value;
        break;
      }
      case "contact_email": {
        this.model.contact_email = value;
        break;
      }
      case "contact_number": {
        this.model.contact_number = value;
        break;
      }
      case "address": {
        this.model.address = value;
        break;
      }
      case "allow_multiple_login": {
        this.model.multiple_login = value === "true" ? true : false;
        break;
      }
      case "enquiry_email_alert": {
        this.model.enquiry_email = value === "true" ? true : false;
        break;
      }
      case "activity_logs": {
        this.model.activity_logs = value === "ON" ? true : false;
        break;
      }
      case "auto_block_user": {
        this.model.auto_block_user = value === "true" ? true : false;
        break;
      }
      case "abuse_threshold": {
        this.model.abuse_threshold = value;
        break;
      }
      case "device_tracking": {
        this.model.device_tracking = value;
        break;
      }
      case "fb_pixel_code": {
        this.model.fb_pixel_code = value;
        break;
      }
      case "google_tag": {
        this.model.google_tag = value;
        break;
      }
      case "upload_image": {
        this.model.upload_image = value;
        break;
      }
      case "hotjar_script": {
        this.model.hotjar_script = value;
        break;
      }
      case "hot_jar_pages_name": {
        this.hot_jar_pages_name = value;
        break;
      }
      case "ids": {
        this.hotjar_ids = value.split(",");
        break;
      }
      case "logo": {
        this._logo_id = id;
        localStorage.setItem("logo_id_value", JSON.stringify(this._logo_id));
        break;
      }
      case "favicon": {
        this._fav_id = id;
        /* store value as string in localstorage for remove function */
        localStorage.setItem("fav_id_value", JSON.stringify(this._fav_id));
        break;
      }
      default: {
        console.log("Invalid choice");
        break;
      }
    }
  }
  /**
   * Update settings
   * @param frm for validate form data
   */
  public onSettingsDataSave(frm: NgForm) {
    this.submitted = true;
    if (frm.invalid) {
      return;
    }
    /* FormData for pushing the code with Image URL */
    const formData: {} = {};
    formData["site_name"] = this.model.site_name;
    formData["tag_line"] = this.model.tag_line;
    formData["default_language"] = this.model.default_language;
    formData["support_email"] = this.model.support_email;
    formData["contact_email"] = this.model.contact_email;
    formData["contact_number"] = this.model.contact_number;
    formData["address"] = this.model.address;
    formData["allow_multiple_login"] = this.model.multiple_login;
    formData["enquiry_email_alert"] = this.model.enquiry_email;
    formData["activity_logs"] =
      this.model.activity_logs === true ? "ON" : "OFF";
    formData["auto_block_user"] = this.model.auto_block_user;
    formData["abuse_threshold"] = this.model.abuse_threshold;
    formData["device_tracking"] = this.model.device_tracking;
    formData["fb_pixel_code"] = this.model.fb_pixel_code;
    formData["google_tag"] = this.model.google_tag;
    formData["upload_image"] = this.model.upload_image;
    formData["hotjar_script"] = this.model.hotjar_script;

    /* Hot Jar Enable Pages ID */
    console.log(this.model.hotjar_ids);
    const enablePagesArray = this.model.hotjar_ids.map((ele) => ele.id);
    if (enablePagesArray.length > 0) {
      formData["ids"] = enablePagesArray.toString();
    } else {
      formData["ids"] = "";
    }
    /* Get Logo file */
    const fileLOGO = (<HTMLInputElement>document.getElementById("profile_logo"))
      .files[0];
    /* Get Favicon file */
    const fileFAV = (<HTMLInputElement>(
      document.getElementById("profile_favicon")
    )).files[0];

    /* Check Logo & Favicon file */
    interface MyFile extends File {
      lastModified: any;
      webkitRelativePath: any;
    }

    if (fileLOGO) {
      let myFile = <MyFile>fileLOGO;

      formData["logo"] = JSON.stringify({
        lastModified: fileLOGO.lastModified,
        lastModifiedDate: myFile.lastModified,
        name: fileLOGO.name,
        size: fileLOGO.size,
        type: fileLOGO.type,
        webkitRelativePath: myFile.webkitRelativePath,
      });
    }
    if (fileFAV) {
      let myFile = <MyFile>fileFAV;
      formData["favicon"] = JSON.stringify({
        lastModified: fileLOGO.lastModified,
        lastModifiedDate: myFile.lastModified,
        name: fileLOGO.name,
        size: fileLOGO.size,
        type: fileLOGO.type,
        webkitRelativePath: myFile.webkitRelativePath,
      });
    }

    /* Call Update function */
    this.updateSettingsData(formData, fileFAV, fileLOGO);
    this.updateImage(formData, fileFAV, fileLOGO);
  }

  private updateImage(formData, logofaviCon: File, logo: File) {
    if (logofaviCon != null || logo != null) {
      this.settingsservice
        .updateSettingicon(formData.site_name, logofaviCon, logo)
        .pipe(first())
        .subscribe(
          (data) => {
            this.loader.hideLoader();
            if (data.meta.status) {
              this.toastr.success(data.meta.message);
              this.getSettingsData();
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
              this.toastr.error("Something went wrong please try again.");
            }
            this.submitted = false;
          }
        );
    }
  }

  /* Update Method */
  private updateSettingsData(formData, logofaviCon: File, logo: File) {
    this.loader.showLoader();
    this.settingsDataSave = this.settingsservice
      .updateSettingsDataURL(formData, logofaviCon, logo)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.getSettingsData();
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
            this.toastr.error("Something went wrong please try again.");
          }
          this.submitted = false;
        }
      );
  }

  /**
   * Get Image Data from API
   */
  private getImageData() {
    this.settingsservice
      .getSettingsImageDataURL()
      .pipe(first())
      .subscribe((data) => {
        this.ImageData = data.responseData;
        const sitename = data.responseData.siteName
          ? data.responseData.siteName
          : CONFIGCONSTANTS.siteName;
        if (data.responseData.logo) {
          this.logoURL = data.responseData.logo;
          this.noLogo = true;
        }
        if (data.responseData.favicon) {
          this.faviconURL = data.responseData.favicon;
          this.noFav = true;
          this.settingsservice.changeFavicon(data.responseData.favicon32);
        }
        this.settingsservice.setSettingsData(
          sitename,
          this.logoURL,
          this.faviconURL
        );
      });
  }

  /**
   * Validate image and Change Logo Image
   * @param event get image array
   */
  public changeLogoImage(event: any) {
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
          reader.onload = (e: any) => {
            this.noLogo = true;
            this.logoURL = e.target.result;
          };
          reader.readAsDataURL(event.target.files[0]);
        } else {
          this.toastr.error(
            "Image Size should be smaller than " + imgSize + "MB"
          );
        }
      }
    }
  }
  public openLogoUpload() {
    document.getElementById("profile_logo").click();
  }
  /* remove logo */
  public removeLogoPicture(profile_logo) {
    /* convert string value to number type */
    const logo_id_value = localStorage.getItem("logo_id_value")
      ? JSON.parse(localStorage.getItem("logo_id_value"))
      : "";
    this.settingsservice
      .removeImage(logo_id_value)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.logoURL = "assets/img/brand/angular_logo.png";
            profile_logo.value = null;
            this.noLogo = false;
          }
        },
        (error) => {
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  /**
   * Validate image and Change Favicon Image
   * @param event get image array
   */
  public changeFavImage(event: any) {
    if (event.target.files[0] !== undefined) {
      const imgSize = 2;
      let temp;
      const fileTypes = [
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/x-icon",
      ];
      if (fileTypes.includes(event.target.files[0].type) === false) {
        this.toastr.error("Please upload a valid image.");
      } else {
        if (
          (temp = imgSize) === void 0 ||
          temp === "" ||
          event.target.files[0].size / 1024 / 1024 < imgSize
        ) {
          const reader = new FileReader();
          reader.onload = (e: any) => {
            this.noFav = true;
            this.faviconURL = e.target.result;
          };
          reader.readAsDataURL(event.target.files[0]);
        } else {
          this.toastr.error(
            "Image Size should be smaller than " + imgSize + "MB"
          );
        }
      }
    }
  }

  public openFavUpload() {
    document.getElementById("profile_favicon").click();
  }

  /* remove favicon */
  public removeFavPicture(profile_favicon) {
    /* convert string value to number type */
    const fav_id_value = localStorage.getItem("fav_id_value")
      ? JSON.parse(localStorage.getItem("fav_id_value"))
      : "";
    this.settingsservice
      .removeImage(fav_id_value)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.faviconURL = "assets/img/brand/angular_logo_small.png";
            profile_favicon.value = null;
            this.noFav = false;
            this.settingsservice.changeFavicon(
              "assets/img/brand/angular_logo_small.png"
            );
          }
        },
        (error) => {
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
