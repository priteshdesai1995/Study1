import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ManageOfferService {
  constructor(private http: HttpClient) {}

  getAllManageOfferList(manageOffer: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getAllManageOfferListURL, manageOffer, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  updateManageOffer(manageOffer: any, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.put<any>(CONFIG.updateManageOfferURL + id, manageOffer, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  createManageOffer(manageOffer: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.createManageOfferURL, manageOffer, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  changeManageOfferStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http
      .put<any>(CONFIG.changeManageOfferStatusURL, {offer_id: id, status: status }, {headers: header}).pipe()
      .pipe(
        map((response) => {
          return response;
        })
      );
  }

  deleteManageOffer(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.delete<any>(CONFIG.deleteManageOfferURL + id, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getManageOfferById(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getManageOfferByIdURL + id, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  /**
   *
   * @param data fetch User data for common users List Used
   */
  getActiveUser() {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.get<any>(CONFIG.getActiveUserURL, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getExportOfferList(data: any, type: string) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    let route = CONFIG.exportOfferExcelURL;
    if (type === 'pdf') {
      route = CONFIG.exportOfferPDFURL;
    } else if (type === 'csv') {
      route = CONFIG.exportOfferCSVURL;
    }
    return this.http.post<any>(route, data, {headers: header, responseType: 'blob' as 'json'}).pipe(
      map((response) => {
        return response;
      })
    );
  }
  getOfferReportListURL(uuid, data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getOfferReportListURL + uuid, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  getExportUserList(data: any, type: string) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    let route = CONFIG.exportUserExcelURL;
    if (type === 'pdf') {
      route = CONFIG.exportUserPDFURL;
    } else if (type === 'csv') {
      route = CONFIG.exportUserCSVURL;
    }
    return this.http.post<any>(route, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }
}
