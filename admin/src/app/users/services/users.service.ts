import {HttpClient, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from './user.model';
import {catchError, map} from 'rxjs/operators';
import {Page} from "../../shared/models/page.model";

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private _usersBaseUrl: string;

  constructor(private readonly http: HttpClient) {
    this._usersBaseUrl = `${environment.baseUrl}/users`;
  }

  getUsers(page: number, size: number): Observable<Page<User>> {
    return this.http.get<Page<User>>(this._usersBaseUrl, {
      params: new HttpParams().set("page", page).set("size", size)
    }).pipe(catchError(error => throwError(error)));
  }

  saveUser = (user: User): Observable<User> => {
    return this.http.post<User>(this._usersBaseUrl, user)
      .pipe(catchError(error => throwError(error)));
  }

  updateUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this._usersBaseUrl}/${id}`, user)
      .pipe(
        map(() => user),
        catchError(error => throwError(error))
      );
  }

  deleteUserById(id: number): Observable<void> {
    return this.http.delete<void>(`${this._usersBaseUrl}/${id}`)
      .pipe(catchError(error => throwError(error)));
  }

}
