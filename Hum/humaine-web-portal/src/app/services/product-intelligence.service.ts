import { ProductIntelligenceListRequest } from './../core/_model/ProductIntelligenceListRequest';
import { URLS } from './../core/_constant/api.config';
import { DataService } from './data-service.service';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductIntelligenceService {

  constructor(private dataService: DataService) { }

  getProuductList(page: number, size: number, field: string, filter: string, desc = false) {
    return this.dataService.post(URLS.productList, new ProductIntelligenceListRequest(page, size, field, filter,desc)).pipe(
      map((data) => {
        return data;
      })
    )
  }
}
