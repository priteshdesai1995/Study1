import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { URLS } from '../../_constant/api.config';
import { DataService } from '../../../services/data-service.service';

@Injectable({
    providedIn: 'root'
})

export class MapService {
    constructor(private dataService: DataService) { }

    getStateInsights(stateName : string) {    
        return this.dataService.get(URLS.stateInsight + "/" + stateName).pipe(
            map((data) => {
                return data;
            })
        )
    }
}