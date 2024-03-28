import { Validators } from '@angular/forms';

const PasswordRegx = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*\d)(?=.*[@!$%^&#])[A-Za-z\d@!$%^&#]{8,50}$/;
const numericRegx = '^[0-9]+$';
const URLRegx = /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)?|^((http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)?$/;
const UserRegex = /^(?=.{5,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$/;
export const emailValidator = Validators.pattern(
  /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
);
export const passwordValidator = Validators.pattern(PasswordRegx);
export const numericValidator = Validators.pattern(numericRegx);
export const URLValidator = Validators.pattern(URLRegx);
export const userNameValidator = Validators.pattern(UserRegex);

const Validator = {
  emailValidator,
  passwordValidator,
  numericValidator,
  URLValidator,
  userNameValidator
};

export default Validator;
