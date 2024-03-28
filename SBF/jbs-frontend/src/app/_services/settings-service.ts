import { Injectable, Inject } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";
import { DOCUMENT } from "@angular/common";
import { CONFIGCONSTANTS } from "../config/app-constants";

@Injectable({
  providedIn: "root",
})
export class SettingsService {
  setting: any = {};
  constructor(
    private http: HttpClient,
    @Inject(DOCUMENT) private _document: HTMLDocument
  ) {}

  /* Get Response of the Settings Data */
  getSettingsDataURL() {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    /* added {} as empty parameter due POST method but need to remove {} and replace POST Method to GET Method */
    return this.http
      .post<any>(CONFIG.getSettingsDataURL, {}, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* SAVE Data to the Settings Data */
  updateSettingsDataURL(SettingsData: any, feviCon: File, logo: File) {
    console.log("SettingsData", SettingsData);
    console.log("SettingsData- feviCon=>", feviCon);
    console.log("SettingsData- logo=>", logo);

    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getSettingsSaveDataURL, SettingsData, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* Update Image URL */
  updateSettingicon(siteName: String, feviCon: File, logo: File) {
    console.log("feviCon : " + feviCon);
    console.log("logo : " + logo);

    var data = { feviCon, logo };
    console.log(JSON.stringify(data));
    console.log("Site Name : " + siteName);

    let header = new HttpHeaders();
    header = header.set("Content-Type", "multipart/form-data");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.updateSettingsImage + "/" + siteName, data, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
  /* Get Image URL */
  getSettingsImageDataURL() {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getSettingsImageDataURL, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* Delete Image on basis of ID value */
  removeImage(siteName: String) {
    return this.http.get<any>(CONFIG.getSettingsRemoveImageURL + siteName).pipe(
      map((response) => {
        return response;
      })
    );
  }
  changeFavicon(href: string): void {
    this._document.getElementById("appFavicon").setAttribute("href", href);
  }
  setSettingsData(sitename, logo, favicon) {
    let site, log, fav;
    site = sitename ? sitename : CONFIGCONSTANTS.siteName;
    log = logo ? logo : "assets/img/brand/angular_logo.png";
    fav = favicon ? favicon : "assets/img/brand/angular_logo_small.png";

    this.setting["sitename"] = site;
    this.setting["logo"] = log;
    this.setting["favicon"] = fav;
    localStorage.setItem("settings", JSON.stringify(this.setting));
  }
}
