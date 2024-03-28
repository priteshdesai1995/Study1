import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CONFIG } from '../../config/app-config';
import { RegisterRequest } from '../../model/registerrequest';
import { AuthenticationService } from '../../_services/authentication.service';
import { RegisterService } from '../../_services/register.service';

@Component({
  selector: 'app-dashboard',
  styleUrls: ['./register.component.scss'],
  templateUrl: 'register.component.html',
})
export class RegisterComponent implements OnInit {
  SignupForm:FormGroup;
  
  constructor(private http: HttpClient,private toastr: ToastrService,
    private router: Router, private authService: AuthenticationService, private registerService: RegisterService) {}
  ngOnInit(): void {
    this.SignupForm = new FormGroup({
      'addressDTOList': new FormGroup({
        'addressType': new FormControl(null),
        'addressLineOne': new FormControl(null),
        'addressLineTwo': new FormControl(null),
        'city': new FormControl(null),
        'province': new FormControl(null),
        'postalCode': new FormControl(null),       
      }),
      'firstName': new FormControl(null,[Validators.required]),
      'middleName': new FormControl(null),
      'lastName': new FormControl(null,[Validators.required]),
      'userName': new FormControl(null,[Validators.required]),
      'email': new FormControl(null,[Validators.required,Validators.email]),
      'dob':  new FormControl(null,[Validators.required]),
      'mobileNo':  new FormControl(null,[Validators.required,Validators.pattern(/^\d{10}$/)]),
      'gender':new FormControl('female',[Validators.required]),
      'password': new FormControl(null,[Validators.required,Validators.minLength(8),Validators.maxLength(32)]),
      'confirmPassword': new FormControl(null, [Validators.required,Validators.minLength(8),Validators.maxLength(32)]),
      'occupation':  new FormControl(null,[Validators.required]),
      'employer':  new FormControl(null,[Validators.required]),  
    });
  
  }
  onSubmit(){
    if(this.SignupForm.invalid){
      this.SignupForm.markAllAsTouched();
      return;
    } else{
      let signUpRequest = new RegisterRequest(this.SignupForm.value );
      console.log(signUpRequest);
      this.signUp(signUpRequest);
    }
  }
  // All is this method
  onPasswordChange() {
    if (this.confirmPassword.value == this.password.value) {
      this.confirmPassword.setErrors(null);
    } else {
      this.confirmPassword.setErrors({ mismatch: true });
    }
  }
  
  // getting the form control elements
  get password(): AbstractControl {
    return this.SignupForm.controls['password'];
  }
  
  get confirmPassword(): AbstractControl{
    return this.SignupForm.controls['confirmPassword'];
  }

  signUp(signUpRequest: RegisterRequest) { 
      this.registerService.signUp(signUpRequest)
      .subscribe((data)=>{
        if(data.status){
          this.router.navigate(['/login']);
          this.toastr.success("User created successfully!!!..");
        } else{
          this.toastr.error("Something went wrong.");
          
        }
      })
  }
}
