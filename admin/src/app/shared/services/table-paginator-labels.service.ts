import { Injectable } from '@angular/core';
import {MatPaginatorIntl} from "@angular/material/paginator";
import {TranslateService} from "@ngx-translate/core";

@Injectable()
export class TablePaginatorLabels extends MatPaginatorIntl {

  constructor(readonly translate: TranslateService) {
    super();
    this.translate.onLangChange.subscribe(language => {
      this.translateLabels();
      this.changes.next();
    });
    this.translateLabels();
  }

  private label = (key: string, params?: any) => {
    return this.translate.instant(`shared.table.paginator.${key}`, params);
  };

  private translateLabels = () => {
    this.itemsPerPageLabel = this.label('itemsPerPage');
    this.nextPageLabel = this.label('nextPage');
    this.previousPageLabel = this.label('previousPage');
    this.firstPageLabel = this.label('firstPage');
    this.lastPageLabel = this.label('lastPage');
  };

  getRangeLabel = (page: number, pageSize: number, length: number): string => {
    const pageNumber = Math.ceil(length / pageSize);
    return this.label("range", {
      page: page + 1,
      length: pageNumber
    });
  };


}
