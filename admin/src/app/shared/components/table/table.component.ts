import {
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import {RowOption} from "../../models/row-option.model";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ConfirmitionDialogComponent} from "../confirmation-dialog/confirmation-dialog.component";
import {PageEvent} from "@angular/material/paginator";
import {TranslateService} from "@ngx-translate/core";

interface DeleteDialogTranslation {
  title: string;
  message: string;
}

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent<Resource> {

  @Input()
  displayedColumns: string[] = [];
  @Input()
  resources: Resource[] = [];
  @Input()
  rowOptions: RowOption<Resource>[] = [];
  @Input()
  dateFormat: string = "medium";
  @Input()
  pageSizeOptions = [10, 20, 30];
  @Input()
  pageIndex: number = 0;
  @Input()
  pageSize: number = 10;
  @Input()
  totalNumberOfResources: number = 0;

  @Output()
  edit = new EventEmitter<Resource>();
  @Output()
  delete = new EventEmitter<Resource>();
  @Output()
  page = new EventEmitter<PageEvent>();

  constructor(
    private readonly dialog: MatDialog,
    private readonly translate: TranslateService
  ) {}

  get columns() { return this.displayedColumns.filter(c => c !== 'options'); }

  get noDataRowColSpan() { return this.displayedColumns.length; }

  get showRowOptions() { return this.displayedColumns.includes('options'); };

  afterDeleteClicked = (resource: Resource): void => {
    this.translate.get("shared.dialog.delete").subscribe({
      next: t => this.afterDeleteDialogTranslationResolved(resource, t)
    });
  };

  openDeleteDialog = (translation: DeleteDialogTranslation): MatDialogRef<any> => {
    return this.dialog.open(ConfirmitionDialogComponent, { data: translation });
  };

  afterDeleteDialogTranslationResolved = (resource: Resource, translation: DeleteDialogTranslation) => {
    const deleteDialogRef = this.openDeleteDialog(translation);
    deleteDialogRef.afterClosed().subscribe(result => {
      this.afterDeleteDialogClosed(resource, result);
    });
  };

  afterDeleteDialogClosed = (resource: Resource, confirmed: boolean | undefined) => {
    if (confirmed) {
      this.afterDelete(resource);
    }
  }

  afterDelete(resource: Resource) {
    this.delete.emit(resource);
  }

  handleEdit(resource: Resource) {
    this.edit.emit(resource);
  }

  handlePageChange(pageEvent: PageEvent) {
    this.page.emit(pageEvent);
  }
}
