import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CONFIG } from '../config/app-config';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  constructor(private http: HttpClient) {}
  getReviewList(data: any) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.post<any>(CONFIG.getReviewListURL, data, {headers: header}).pipe(
      map((response) => {
        return response;
      })
    );
  }

  deleteReview(id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http.delete<any>(CONFIG.deleteReviewURL + id, {headers: header}).pipe(
      map((data) => {
        return data;
      })
    );
  }
  changeReviewStatus(status: string, id: number) {
    let header = new HttpHeaders();
    header = header.set('Content-Type', 'application/json');
    header = header.set('Version', 'V1');
    return this.http
      .post<any>(CONFIG.changeReviewStatusURL, { reviewId: id, status: status }, {headers: header})
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
