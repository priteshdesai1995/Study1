import { Injectable } from '@angular/core';
import { URLS } from '../../core/_constant/api.config';
import { DataService } from '../../services/data-service.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserGroupService {

  constructor(private dataService: DataService) { }
  getDemographicData() {
    return this.dataService.get(URLS.getDemographic).pipe(
      map((data) => {
        return data;
      })
    )
  }

  getCognitiveData(id: number) {
    return this.dataService.getById(URLS.getCognitive, id).pipe(
      map((data) => {
        return data;
      })
    )
  }

  addUserGroup(data: any) {
    return this.dataService.post(URLS.addUsergroup, data).pipe(
      map((data) => {
        return data;
      })
    );
  }

  userGroupList(){
    return this.dataService.get(URLS.UserList).pipe(
      map((data) => {
        return data;
      })
    )
  }

  deleteUserGroup(id:number){
    return this.dataService.delete(URLS.deleteUser,id).pipe(
      map((data) => {
        return data;
      })
    )
  }

}
