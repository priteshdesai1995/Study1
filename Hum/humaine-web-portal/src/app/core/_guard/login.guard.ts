import { CONFIGCONSTANTS } from './../_constant/app-constant';
import { getValueByKey } from './../_utility/common';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanActivateChild } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../../services/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate, CanActivateChild  {
  constructor(private router: Router,private authService :AuthenticationService) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.authService.checkIfRegistrationPending();
    return true;
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.canActivate(route, state);
  }
}
