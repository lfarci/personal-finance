import {Component} from '@angular/core';
import {LangChangeEvent, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  languageOptions = [
    { code: 'fr', labelKey: 'menu.languages.french' },
    { code: 'en', labelKey: 'menu.languages.english' }
  ];

  constructor(
    readonly translate: TranslateService
  ) {
    const defaultLanguage = localStorage.getItem('language') || 'en';
    translate.addLangs(this.languageOptions.map(o => o.code));
    translate.setDefaultLang(defaultLanguage);
    translate.use(defaultLanguage);
    translate.onLangChange.subscribe((languageChange: LangChangeEvent) => {
      localStorage.setItem('language', languageChange.lang);
    });
  }

  get currentLanguage() { return this.translate.currentLang; }

  changeLanguage = (lang: string) => {
    this.translate.use(lang);
  };
}
