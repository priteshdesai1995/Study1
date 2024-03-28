import { Directive, HostListener } from '@angular/core';
import { preventEvent } from '../_utility/common';

@Directive({
  selector: '[appPreventSubmitOnEnter]',
})
export class PreventSubmitOnEnterDirective {
  constructor() {}

  @HostListener('keydown', ['$event']) onSubmit(event) {
    if (event.keyCode === 13 && event.target.tagName !== 'TEXTAREA') {
      preventEvent(event);
    }
  }
}
