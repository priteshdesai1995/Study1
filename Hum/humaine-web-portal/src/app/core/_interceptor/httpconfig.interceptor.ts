import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent
} from '@angular/common/http';
import { isPlatformBrowser } from '@angular/common';
import { Observable, of, throwError } from "rxjs";
import { catchError, map } from 'rxjs/operators';
import { ToasterService } from '../../core/_utility/notify.service';
import { AuthenticationService } from '../../services/authentication.service';

@Injectable()

export class HttpConfigInterceptor implements HttpInterceptor {

    isBrowser: boolean;

    constructor(@Inject(PLATFORM_ID) platformId: string,
     private toater: ToasterService,
     private authService:AuthenticationService) {
        this.isBrowser = isPlatformBrowser(platformId);
    }
   

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let loginToken = this.authService.getLoginToken();
        request = request.clone({ headers: request.headers.set('Authorization', loginToken ?  "Bearer "+loginToken : '') });

        request = request.clone({ headers: request.headers.set('Accept', 'application/json') });
        if (!request.headers.has('Content-Type')) {
            request = request.clone({
                headers: request.headers.set('Content-Type', 'application/json')
                    .set('version', 'v1')
            });
        }
        return next.handle(request).pipe(
            map((event: HttpEvent<any>) => {
                return event;
            }));
    }
}
