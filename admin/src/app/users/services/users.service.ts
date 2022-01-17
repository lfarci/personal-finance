import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from './user.model';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private _usersBaseUrl: string;

  constructor(private readonly http: HttpClient) {
    this._usersBaseUrl = `${environment.baseUrl}/users`;
  }

  findUserById(id: number): Observable<User | null> {
    return this.http.get<User>(`${this._usersBaseUrl}/${id}`)
      .pipe(catchError(error => throwError(error)));
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this._usersBaseUrl)
      .pipe(catchError(error => throwError(error)));
  }

  saveUser(user: User): Observable<User> {
    return this.http.post<User>(this._usersBaseUrl, user)
      .pipe(catchError(error => throwError(error)));
  }


  deleteUserById(id: number): Observable<void> {
    return this.http.delete<void>(`${this._usersBaseUrl}/${id}`)
      .pipe(catchError(error => throwError(error)));
  }

}
