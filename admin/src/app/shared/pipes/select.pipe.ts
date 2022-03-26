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

  private static formatAsDate(value: any): boolean {
    return typeof value == "string" && SelectPipe.isValidDate(value as string);
  }


  /**
   * I take the assumption that dates are represented by a string in the resource parameter.
   */
  transform<Resource extends WithProperties>(resource: Resource, columnName: string, dateFormat: string = "medium"): string | null {
    if (SelectPipe.formatAsDate(resource[columnName])) {
      return this._datePipe.transform(resource[columnName], dateFormat);
    } else {
      return resource[columnName];
    }
  }

}
