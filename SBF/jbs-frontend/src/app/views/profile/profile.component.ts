import { Component, OnInit, OnDestroy } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Router } from "@angular/router";
import { CONFIG } from "./../../config/app-config";
import { ToastrService } from "ngx-toastr";
import { ManageuserService } from "../../_services/manageuser-service";
import { AuthenticationService } from "../../_services/authentication.service";
import { LoaderService } from "../../_services/loader.service";
import { NgForm } from "@angular/forms";
import { LanguageService } from "../../_services/language.service";
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { RegisterService } from "../../_services/register.service";

@Component({
  templateUrl: "./profile.component.html",
})
export class ProfileComponent implements OnInit, OnDestroy {
  firstname: string;
  lastname: string;
  username: string;
  email: string;
  oldpass: string;
  newpass: string;
  confirmpass: string;
  password_submit: boolean;
  userId: string;
  conf_pass_match = "";

  private subscription: Subscription;
  private Language = localStorage.getItem("lan") || "en";
  constructor(
    private http: HttpClient,
    public router: Router,
    private authenticationService: AuthenticationService,
    private toastr: ToastrService,
    private loader: LoaderService,
    private manageuserService: ManageuserService,
    private languageSwitcher: LanguageService,
    private translate: TranslateService,
    private registerService: RegisterService
  ) {
    this.subscription = this.languageSwitcher.getLanguage().subscribe((lan) => {
      this.translate.setDefaultLang(lan);
      this.translate.use(lan);
    });
    this.languageSwitcher.changeLanguage(this.Language);
  }

  ngOnInit() {
    this.getProfileData();
  }
  /**
   * set input value of logged in user
   */
  private getProfileData() {
    this.registerService.getProfile().subscribe((res) => {
      const profiledata = res["responseData"];
      this.firstname = profiledata.firstName;
      this.lastname = profiledata.lastName;
      this.username = profiledata.username;
      this.userId = profiledata.profileId;
      this.email = profiledata.email;
      localStorage.setItem("fullName", this.firstname + " " + this.lastname);
      document.getElementById("headerName").innerHTML =
        this.firstname + " " + this.lastname;
    });
  }
  /**
   * Update logged in user profile
   * @param frm validate form fields
   */
  public updateProfile(frm: NgForm) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    if (frm.invalid) {
      return;
    }
    const data = {
      firstName: this.manageuserService.trimText(this.firstname),
      lastName: this.manageuserService.trimText(this.lastname),
      userName: this.manageuserService.trimText(this.username),
      email: this.email,
    };
    this.http
      .put(CONFIG.updateProfileInfoURL + "/" + this.userId, data, {
        headers: header,
      })
      .subscribe(
        (res) => {
          this.toastr.success("User updated successfully", "", {
            timeOut: 3000,
            closeButton: true,
          });
          this.getProfileData();
        },
        (msg) => {
          if (msg !== "Unauthorized") {
            Object.keys(msg.errors).forEach((key) => {
              this.toastr.error(msg.errors[key], "", {
                timeOut: 3000,
                closeButton: true,
              });
            });
          }
        }
      );
  }
  /**
   * Change logged in user password
   * @param frm validate form fields
   */
  public changePassword(frm: NgForm) {
    if (frm.invalid) {
      return;
    }
    if (this.oldpass === this.newpass) {
      this.toastr.error("Current and new password should not be same");
      return false;
    }
    const data = {
      oldPassword: this.oldpass,
      newPassword: this.newpass,
      confirmPassword: this.confirmpass,
      userId: this.userId,
    };
    this.loader.showLoader();

    this.registerService.changePassword(data).subscribe((res) => {
      this.loader.hideLoader();
      if (res.status) {
        this.toastr.success("Password changed successfully.");
        this.authenticationService.logout();
        this.router.navigate(["/login"]);
      } else {
        this.toastr.error(res.errorList[0]);
      }
    });
  }

  checkPass() {
    this.conf_pass_match = this.manageuserService.checkPassword(this.newpass);
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
