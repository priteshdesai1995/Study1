import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor(private httpClient:HttpClient) { }

  getStates(): Observable<any> {
    return this.httpClient.get('../../assets/state.json').pipe(
      map((result: Response) => {
        return result;
      })
    );
  }

  getCities(): Observable<any> {
    return this.httpClient.get('../../assets/cities.json').pipe(
      map((result) => {
        return result;
      })
    );
  }
}
