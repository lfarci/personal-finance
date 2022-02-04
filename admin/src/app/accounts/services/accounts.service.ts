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
    this._accountsBaseUrl = environment.baseUrl;
  }

  getBankAccounts = (userId: string): Observable<BankAccount[]> => {
    const url = `${this._accountsBaseUrl}/users/${userId}/accounts`;
    return this.http.get<BankAccount[]>(url).pipe(catchError(error => throwError(error)));
  };

  deleteById = (id: number): Observable<void> => {
    const url = `${this._accountsBaseUrl}/accounts/${id}`;
    return this.http.delete<void>(url).pipe(catchError(error => throwError(error)));
  };
}
