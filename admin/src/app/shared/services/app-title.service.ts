import { Injectable } from '@angular/core';
import {Title} from "@angular/platform-browser";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class AppTitleService {

  constructor(private title: Title, private translate: TranslateService) {}

  private setTranslatedTitle = (titleKey: string) => {
    this.title.setTitle(this.translate.instant(titleKey));
  };

  setTitle = (titleKey: string) => {
    this.setTranslatedTitle(titleKey);
    this.translate.onLangChange.subscribe(() => {
      this.setTranslatedTitle(titleKey);
    });
  };
}
