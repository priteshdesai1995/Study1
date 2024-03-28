import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";
import { Subadmin } from "./../model/subadmin";

@Injectable({
  providedIn: "root",
})
export class SubadminService {
  constructor(private http: HttpClient) {}

  getAllSubadminList(SubadminData: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllSubadminListURL, SubadminData, {
        headers: header,
        responseType: "json",
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /**
   * Fetch sub admin list [Without Role Permission API]
   *
   * @param SubadminData
   */
  getAllSubadminListForDropdown() {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getSubadminListForDropdownURL, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getSubadminById(id: number) {
    return this.http.get<any>(CONFIG.getSubadminByIdURL + id).pipe(
      map((data) => {
        return data;
      })
    );
  }

  getActiveRoleList() {
    return this.http.get<any>(CONFIG.getActiveRoleURL).pipe(
      map((data) => {
        return data;
      })
    );
  }

  updateSubadmin(SubadminData: any, id: number) {
    return this.http.put<any>(CONFIG.updateSubadminURL + id, SubadminData).pipe(
      map((response) => {
        return response;
      })
    );
  }

  createSubadmin(SubadminData: Subadmin) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createSubadminURL, SubadminData, {
        headers: header,
        responseType: "json",
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeSubadminPassword(SubadminData: any) {
    return this.http.put<any>(CONFIG.changeSubadminPassURL, SubadminData).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeSubadminStatus(status: string, id: number) {
    return this.http
      .put<any>(CONFIG.changeSubadminStatusURL, { id: id, status: status })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteSubadmin(id: number) {
    return this.http.delete<any>(CONFIG.deleteSubadminURL + id).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
