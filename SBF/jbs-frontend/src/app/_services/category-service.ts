import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  getAllCategoryList() {
    let header = new HttpHeaders();
    header = header.set("Authorization", "Brain@2022");
    // header = header.set('Content-Type', 'application/json');
    return this.http
      .get<any>(CONFIG.getAllCategoryListURL, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }
  getCategoryTreeview() {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set("Content-Type", "application/json");
    return this.http
      .get<any>(CONFIG.getCategoryTreeviewURL, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  getCategoryById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set("Content-Type", "application/json");
    return this.http
      .get<any>(CONFIG.getCategoryByIdURL + id, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  updateCategory(categoryData: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set("Content-Type", "application/json");
    return this.http
      .put<any>(CONFIG.updateCategoryURL + id, categoryData, {
        headers: header,
      })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createCategory(categoryData: any) {
    let header = new HttpHeaders();
    // header = header.set("Version", "V1");
    // header = header.set('Content-Type', 'application/json');
    return this.http
      .post<any>(CONFIG.createCategoryURL, categoryData, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeCategoryStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set("Content-Type", "application/json");
    return this.http
      .put<any>(
        CONFIG.changeCategoryStatusURL,
        { id: id, status: status },
        { headers: header }
      )
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteCategory(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    return this.http
      .delete<any>(CONFIG.deleteCategoryURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  saveCategoryTreeviewData(categoryData: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set("Content-Type", "application/json");
    return this.http
      .put<any>(CONFIG.createCategoryTreeURL, categoryData, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
  getParentCategoryList(parentName: String) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    header = header.set("Content-Type", "application/json");
    return this.http
      .get<any>(CONFIG.parentCategoryListURL + parentName, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }
}
