import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output, ViewChild,
} from '@angular/core';
import {RowOption} from "../row-option.model";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmitionDialogComponent} from "../confirmation-dialog/confirmation-dialog.component";
import {PageEvent} from "@angular/material/paginator";

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
  emptyTableMessage: string = "Empty table.";
  @Input()
  dateFormat: string = "medium";
  @Input()
  pageSizeOptions = [5, 10, 20];
  @Input()
  pageIndex: number = 0;
  @Input()
  pageSize: number = 5;
  @Input()
  totalNumberOfResources: number = 0;

  @Output()
  create = new EventEmitter<void>();
  @Output()
  edit = new EventEmitter<Resource>();
  @Output()
  delete = new EventEmitter<Resource>();
  @Output()
  page = new EventEmitter<PageEvent>();

  constructor(private readonly dialog: MatDialog) {}

  get empty() { return this.resources.length === 0; }

  get columns() { return this.displayedColumns.filter(c => c !== 'options'); }

  get showRowOptions() { return this.displayedColumns.includes('options'); };

  openConfirmationDialog = (resource: Resource): void => {
    const dialogRef = this.dialog.open(ConfirmitionDialogComponent, {
      data: {
        title: "Deletion",
        message: `Do you really want to delete this item? It won't be possible to recover it.`
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleConfirmationDialogResult(resource, result);
    });
  };

  handleConfirmationDialogResult = (resource: Resource, confirmed: boolean | undefined) => {
    if (confirmed) {
      this.handleDeleteConfirmation(resource);
    }
  }

  handleDeleteConfirmation(resource: Resource) {
    this.delete.emit(resource);
  }

  handleCreate() {
    this.create.emit();
  }

  handleEdit(resource: Resource) {
    this.edit.emit(resource);
  }

  handlePageChange(pageEvent: PageEvent) {
    this.page.emit(pageEvent);
  }
}
