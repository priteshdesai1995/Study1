import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CONFIG } from '../config/app-config';
import { EncrDecrService } from '../_services/encr-decr.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private EncrDecr: EncrDecrService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    const decrypted = localStorage.getItem('currentUser');
    if (decrypted) {
      const currentUser = JSON.parse(localStorage.getItem('user'));
      if (currentUser && currentUser.access_token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${currentUser.access_token}`,
            'language': localStorage.getItem("lan")
          },
        });
      }
    }

    return next.handle(request);
  }
}
