import { Pipe, PipeTransform } from "@angular/core";
import * as moment from "moment";

@Pipe({ name: 'UTCDate'})
export class UTCDatePipe implements PipeTransform  {
    constructor() {}
    transform(value) {
        if (value)  {
            let date = moment(value).format('YYYY-MM-DD HH:mm:ss');
            let date2: Date = new Date(date + ' UTC');
            return date2.toLocaleString();
        } else {
              return '';
        }
    }
}