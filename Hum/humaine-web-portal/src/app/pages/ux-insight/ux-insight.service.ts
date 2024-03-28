import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';

@Injectable({
  providedIn: 'root'
})
export class UXInsightService {
  constructor(private dataService: DataService) { }

  getUXInsight() {
    return this.dataService.get(URLS.UXInsight).pipe(
      map((data) => {
        return data;
      })
    )
  }

  getUXInsightProduct() {
    return this.dataService.get(URLS.UXInsightProduct).pipe(
      map((data) => {
        return data;
      })
    )
  }
}
