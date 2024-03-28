import { ToasterService } from './../_utility/notify.service';
import { AuthenticationService } from './../../services/authentication.service';
import { ErrorCodes } from './../_constant/app-constant';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import * as _ from 'lodash';
import { ToastrService } from 'ngx-toastr';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  unAuthorized = false;
  constructor(private authService: AuthenticationService, private router: Router, private toastr: ToasterService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(evt => {
        if (evt instanceof HttpResponse) {
            if(evt.body && evt.body.success) {
              this.unAuthorized = false;
            }
        }
      }),
      catchError((err) => {
        if (err.status === 401) {
            const errCode = _.get(err, 'error.errorList[0].code', null);
            if (ErrorCodes.UNAUTHORIZED === errCode) {
                const errMessge =_.get(err, 'error.errorList[0].message', null);
                if (errMessge && !this.unAuthorized) {
                    this.unAuthorized = true;
                    this.toastr.errorMsg(errMessge);
                }
                this.authService.logout();
            } else  {
              this.unAuthorized = false;
            }
        }
        return throwError(err);
      })
    );
  }
}
