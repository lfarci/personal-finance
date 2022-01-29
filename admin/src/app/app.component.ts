import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  languageOptions = [
    { code: 'fr', labelKey: 'language.french' },
    { code: 'en', labelKey: 'language.english' }
  ];

  constructor(
    readonly translate: TranslateService
  ) {
    translate.setDefaultLang('en');
    translate.use('en');
  }

  changeLanguage = (lang: string) => {
    this.translate.use(lang);
  };
}
