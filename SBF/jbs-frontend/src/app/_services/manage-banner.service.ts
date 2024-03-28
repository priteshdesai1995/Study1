import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class ManageBannerService {
  constructor(private http: HttpClient) {}

  getAllBannerList(bannerData: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllBannerListURL, bannerData, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeManageBannerStatus(status: string, id: String) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(
        CONFIG.changeManageBannerStatusURL,
        { bannerId: id, status: status },
        { headers: header }
      )
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteManageBanner(id: String) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .delete<any>(CONFIG.deleteManageBannerURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getManageBannerById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getManageBannerByIdURL + id, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  createManageBanner(bannerData: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createManageBannerURL, bannerData, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  updateManageBanner(bannerData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.updateManageBannerURL + id, bannerData, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
