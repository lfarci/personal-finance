import { Injectable } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {TranslateService} from "@ngx-translate/core";
import {forkJoin, Observable} from "rxjs";

interface Message {
  message: string;
  action: string;
}

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private static MESSAGE_DURATION = 3000;

  constructor(
    private readonly translate: TranslateService,
    private readonly snackBar: MatSnackBar
  ) {}

  private afterMessageResolved = (messageKey: string, params: any): Observable<Message> => {
    return forkJoin({
      message: this.translate.get(messageKey, params),
      action: this.translate.get("close")
    });
  };

  private showMessage = (message: Message): void => {
    this.snackBar.open(message.message, message.action.toUpperCase(), {
      duration: MessageService.MESSAGE_DURATION
    });
  };

  info = (messageKey: string, params: any = {}) => {
    this.afterMessageResolved(messageKey, params).subscribe({
      next: this.showMessage
    });
  };
}
