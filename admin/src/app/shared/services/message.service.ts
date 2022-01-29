import { Injectable } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(
    private readonly translate: TranslateService,
    private readonly snackBar: MatSnackBar
  ) {}

  show = (messageKey: string) => {
    this.translate.get(messageKey).subscribe({
      next: (message: string) => {
        this.snackBar.open(message, "DISMISS", {
          duration: 3000
        });
      }
    });
  };
}
