import {
   HttpErrorResponse,
   HttpEvent,
   HttpHandler,
   HttpInterceptor,
   HttpRequest,
   HttpStatusCode
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GENERAL_ERROR_PAGE, SERVER_AUTHORIZE_URL } from '@common/constants';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from '../services/api.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
   private redirecting = false;

   constructor(private api: ApiService) {}

   intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      return next.handle(request).pipe(
         catchError((ex: HttpErrorResponse) => {
            if (ex.status === HttpStatusCode.Unauthorized) {
               this.redirectToAuthorizeUrl();
            }

            return throwError(() => ex);
         })
      );
   }

   private async redirectToAuthorizeUrl() {
      if (this.redirecting) return;
      this.redirecting = true;

      // Fire-and-forget: we want to redirect the browser ASAP.
      try {
         const { authorizeUrl } = await this.api.request<{ authorizeUrl?: string }>(
            `${SERVER_AUTHORIZE_URL}?forceLogin=true`,
            { withAuth: false }
         );

         if (authorizeUrl && typeof authorizeUrl === 'string') {
            window.location.assign(authorizeUrl);
            return;
         }
      } catch (error) {
         console.error('HttpErrorInterceptor: failed to resolve authorize URL', error);
      }

      // Fallback: if backend didn't return a URL, at least navigate to the endpoint.
      window.location.assign(GENERAL_ERROR_PAGE);
   }
}
