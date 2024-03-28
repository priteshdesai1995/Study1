import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';

@Injectable({
  providedIn: 'root'
})
export class ViewUserGroupService {
  constructor(private dataService: DataService) { }

  getUserGroupStatistics() {
    return this.dataService.get(URLS.UserGroupStatistics).pipe(
      map((data) => {
        return data;
      })
    )
  }
}
