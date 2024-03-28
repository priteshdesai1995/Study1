import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class ManageSubscriptionService {
  constructor(private http: HttpClient) {}

  getAllManageSubscriptionList(SubscriptionData: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllManageSubscrptionListURL, SubscriptionData, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getManagerSubscriptionById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getManagerSubscriptionByIdURL + id, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  updateManageSubscription(SubscriptionData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.updateManageSubscriptionURL + id, SubscriptionData, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createManageSubscription(SubscriptionData: any): Observable<any> {
    let header = new HttpHeaders();
    header.set("Content-Type", "application/json");
    header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createManageSubscriptionURL, SubscriptionData, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeManageSubscriptionStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(
        CONFIG.changeManageSubscriptionStatusURL,
        { subscription_id: id, status: status },
        { headers: header }
      )
      .pipe()
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteManageSubscription(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .delete<any>(CONFIG.deleteManageSubscriptionURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
