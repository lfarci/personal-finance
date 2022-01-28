import { Injectable } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private readonly snackBar: MatSnackBar) { }

  show = (message: string) => {
    this.snackBar.open(message, "DISMISS", {
      duration: 3000
    });
  };
}
