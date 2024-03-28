import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { CONFIG } from "../config/app-config";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class LocationService {
  constructor(private http: HttpClient) {}

  // Country
  getAllCountryList(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http.post<any>(CONFIG.getAllCountryURL, data).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getCountryById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getCountryByIdURL + id, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  updateCountry(data: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(CONFIG.updateCountryURL + id, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createCountry(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createCountryURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeCountryStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(
        CONFIG.changeCountryStatusURL,
        { uuid: id, status: status },
        { headers: header }
      )
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteCountry(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .delete<any>(CONFIG.deleteCountryURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  // State
  getAllStateList(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllStateURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getStateById(id: string) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getStateByIdURL + id, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  updateState(data: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(CONFIG.updateStateURL + id, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createState(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createStateURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeStateStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(
        CONFIG.changeStateStatusURL,
        { uuid: id, status: status },
        { headers: header }
      )
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteState(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .delete<any>(CONFIG.deleteStateURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  // City
  getAllCityList(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.getAllCityURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  getCityById(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getCityByIdURL + id, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }

  updateCity(data: any, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.updateCityURL + id, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  createCity(data: any) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .post<any>(CONFIG.createCityURL, data, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  changeCityStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .put<any>(
        CONFIG.changeCityStatusURL,
        { uuid: id, status: status },
        { headers: header }
      )
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteCity(id: number) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .delete<any>(CONFIG.deleteCityURL + id, { headers: header })
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
  getActiveCountry() {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getActiveCountryURL, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }
  getActiveState(countryId) {
    let header = new HttpHeaders();
    header = header.set("Content-Type", "application/json");
    header = header.set("Version", "V1");
    return this.http
      .get<any>(CONFIG.getActiveStateURL + countryId, { headers: header })
      .pipe(
        map((data) => {
          return data;
        })
      );
  }
}
