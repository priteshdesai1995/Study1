import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class ManageEventService {
  constructor(private http: HttpClient) {}

  getAllEventList(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllEventURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getEventById(id: number, data) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getEventByIdURL + id, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  updateEvent(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.updateEventURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createEvent(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createEventURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteEvent(data) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.deleteEventURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
