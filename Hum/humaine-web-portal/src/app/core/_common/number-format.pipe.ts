
import { Pipe, PipeTransform } from '@angular/core';
import { formatNumber } from '../_utility/common';

@Pipe({
  name: 'numberFormat'
})
export class NumberFormatPipe implements PipeTransform {

  transform(num: number, digits: number = 2): string {
    return formatNumber(num, digits);
  }

}
