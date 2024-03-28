import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class BsMediaServiceService {
  constructor(private http: HttpClient) {}

  /* get all media */
  getAllMediaFolderAndFile(Items: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllMediaFolderAndFileURL, Items, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* create new folder */
  createFolder(Items: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createFolderURL, Items, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* delete media */
  deleteFolder(id: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    return this.http
      .delete<any>(CONFIG.deleteFolderURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* Upload any type of media */
  uploadBsMedia(Items: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.uploadBsMediaURL, Items, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* Re-Name Media */
  renameBsMedia(Items: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.renameBsMediaURL, Items, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  /* move media to another folder */
  moveBsMedia(Items: any) {
    let header = new HttpHeaders();
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.moveBsMediaURL, Items, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
