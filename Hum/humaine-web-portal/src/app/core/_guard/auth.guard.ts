import { CONFIGCONSTANTS } from './../_constant/app-constant';
import { getValueByKey } from './../_utility/common';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanActivateChild } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../../services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild  {
  constructor(private router: Router,private authService :AuthenticationService) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }
    const account = this.authService.getAccountDetails();
    const status = getValueByKey(account, 'status', false);
    const stateObj = {
      setStep:true,
      step: CONFIGCONSTANTS.registrationSteps.BUSINESS
    }
    if (!status) {
      this.router.navigate(['/registration']);
    }
    return true;
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(route, state);
  }
}
