import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Owner } from './owner.model';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class OwnersService {

  private _ownersBaseUrl: string;

  constructor(private readonly http: HttpClient) { 
    this._ownersBaseUrl = `${environment.baseUrl}/owners`;
  }

  getOwners(): Observable<Owner[]> {
    return this.http.get<Owner[]>(this._ownersBaseUrl)
      .pipe(catchError(error => throwError(error)));
  }

}
