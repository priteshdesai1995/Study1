import { Directive, ElementRef, HostListener, Input, Optional } from '@angular/core';
import { FormGroup, FormGroupDirective, NgForm } from '@angular/forms';
import { fromEvent } from 'rxjs';
import { debounceTime, take } from 'rxjs/operators';
@Directive({
  selector: '[InvalidFormScrollDirective]',
})
export class InvalidFormScrollDirective {
  @Input() isTemplateDriven = false;
  constructor(private el: ElementRef, @Optional() private formGroupDir: FormGroupDirective, @Optional() private frm: NgForm) {}
  @HostListener('ngSubmit') onSubmit() {
    let invalid = false;
    if (this.formGroupDir) invalid = this.formGroupDir.invalid;
    if (this.frm && this.isTemplateDriven === true) invalid = this.frm.invalid;
    if (invalid === true) {
      this.scrollToFirstInvalidControl();
    }
  }
  private scrollToFirstInvalidControl() {
    const firstInvalidControl: HTMLElement = this.el.nativeElement.querySelector('.ng-invalid');
    window.scroll({
      top: this.getTopOffset(firstInvalidControl) - 100,
      left: 0,
      behavior: 'smooth',
    });

    fromEvent(window, 'scroll')
      .pipe(debounceTime(100), take(1))
      .subscribe(() => firstInvalidControl.focus());
  }

  private getTopOffset(controlEl: HTMLElement): number {
    const labelOffset = 50;
    const controlElTop = controlEl.getBoundingClientRect().top;

    const absoluteControlElTop = controlElTop + window.scrollY;

    return absoluteControlElTop - labelOffset;
  }
}
