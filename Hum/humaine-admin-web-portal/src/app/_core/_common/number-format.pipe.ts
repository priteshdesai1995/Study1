import { formatNumber } from './../../utility/common';

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberFormat'
})
export class NumberFormatPipe implements PipeTransform {

  transform(num: number, digits: number = 2): string {
    return formatNumber(num, digits);
  }
}
