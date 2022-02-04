import {Inject, LOCALE_ID, Pipe, PipeTransform} from '@angular/core';
import {DatePipe} from "@angular/common";

type WithProperties= { [key: string]: any };

@Pipe({
  name: 'select'
})
export class SelectPipe implements PipeTransform {

  private _datePipe: DatePipe;

  constructor(
    @Inject(LOCALE_ID) public locale: string
  ) {
    this._datePipe = new DatePipe(locale);
  }

  private static isValidDate(d: string): boolean {
    return !isNaN(Date.parse(d));
  }

  transform<Resource extends WithProperties>(resource: Resource, columnName: string, dateFormat: string = "medium"): string | null {
    if (SelectPipe.isValidDate(resource[columnName])) {
      return this._datePipe.transform(resource[columnName], dateFormat);
    } else {
      return resource[columnName];
    }
  }

}
