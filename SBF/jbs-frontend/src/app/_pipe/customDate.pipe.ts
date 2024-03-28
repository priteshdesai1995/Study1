import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({ name: 'customDate' })
export class CustomDate implements PipeTransform {
  transform(date: string, format: string): any {
    if (!date) {
      return date;
    }
    return moment(date).format(format);
  }
}
