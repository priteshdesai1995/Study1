import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, EventEmitter, Input, OnInit, Output, ViewChildren } from '@angular/core';
import { preventEvent } from '../../_utility/common';

@Component({
  selector: 'app-otp-input',
  templateUrl: './otp-input.component.html',
  styleUrls: ['./otp-input.component.scss']
})
export class OtpInputComponent implements OnInit {
  @Input('err')  errorMsg: boolean;
  @Output() onInputChange: EventEmitter<any> = new EventEmitter<any>();
  @ViewChildren('formRow') rows: any;
  @Input('isFormSubmitted') isFormSubmitted :boolean;

  @Input() digit = 6;
  @Input() required = true;

  
  form: FormGroup;

  constructor() { }

  formInput = [];

  ngOnInit(): void {
    for(let i =0 ;i<this.digit;i++) {
      this.formInput.push('input'+i);
    }
    this.form = this.toFormGroup(this.formInput);
  }

  toFormGroup(elements) {
    const group: any = {};
    const validations = [];
    if (this.required) {
      validations.push(Validators.required);
    }
    elements.forEach(key => {
      group[key] = new FormControl('', [...validations]);
    });
    return new FormGroup(group);
  }

  keyUpEvent(event, index) {
    let pos = index;
    const charCode = event.which ? event.which : event.keyCode;
    if ((charCode > 31 && (charCode < 48 || charCode > 57)) || [9,16,13].includes(charCode)) {
      preventEvent(event);
      return false;
    }
    if (event.keyCode === 8 && event.which === 8) {
      pos = index - 1 ;
    } else {
      pos = index + 1 ;
    }
    if (pos > -1 && pos < this.formInput.length ) {
      this.rows._results[pos].nativeElement.focus();
    }
  }
  onKeyPress(event) {
    const value = event.target.value;
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      preventEvent(event);
      return false;
    }
    if (value.length >= 1) {
      preventEvent(event);
      return false;
    }
    return true;
  }
  onChange() {
    this.onInputChange.emit(this.value);
  }
  public get value() {
    return Object.values(this.form.value).join("");
  }
}
