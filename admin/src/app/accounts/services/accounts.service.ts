import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { BankAccount } from './account.model';

@Injectable({
  providedIn: 'root'
})
export class BankAccountService {

  private _accountsBaseUrl: string;

  constructor(private readonly http: HttpClient) { 
    this._accountsBaseUrl = `${environment.baseUrl}/accounts`;
  }

  getBankAccounts(): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(this._accountsBaseUrl)
      .pipe(catchError(error => throwError(error)));
  }
}
