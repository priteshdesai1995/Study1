import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';

@Injectable({
  providedIn: 'root'
})
export class HeatmapService {

  constructor(private dataService: DataService) { }
  getHeatmapImages() {
    return this.dataService.get(URLS.heatmapsImages).pipe(
      map((data) => {
        return data;
      })
    )
  }
  getHeatmapSigned(body) {
    return this.dataService.post(URLS.heatmapsSignUrl,body).pipe(
      map((data) => {
        return data;
      })
    )
  }
}