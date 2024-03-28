import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";
import { Role } from "./../model/role";
import { TreeviewItem } from "ngx-treeview";

@Injectable({
  providedIn: "root",
})
export class RolePermissionService {
  constructor(private http: HttpClient) {}

  getAllRoleList() {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getAllRoleListURL, { headers: header })
      .pipe(
        map((data) => {
          // login successful if there's a jwt token in the response
          return data;
        })
      );
  }

  changeRoleStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(
        CONFIG.changeRoleStatusURL,
        { roleId: id, status: status },
        { headers: header }
      )
      .pipe()
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createRole(roleData: Role) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createRoleURL, roleData, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  updateRole(roleData: Role, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(CONFIG.updateRoleURL + id, roleData, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getRolePermissions(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(
        CONFIG.getAllPermissionListURL,
        { role_id: id },
        { headers: header }
      )
      .pipe()
      .pipe(
        map((data) => {
          // login successful if there's a jwt token in the response
          return data;
        })
      );
  }

  assignPermissions(ids: any[], roleId: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(
        CONFIG.assignPermissionURL + roleId,
        { permission_key: ids.toString() },
        { headers: header }
      )
      .pipe(
        map((data) => {
          // login successful if there's a jwt token in the response
          return data;
        })
      );
  }
}
