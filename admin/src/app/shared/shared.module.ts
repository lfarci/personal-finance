import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatDialogModule} from '@angular/material/dialog';
import {ConfirmitionDialogComponent} from './components/confirmation-dialog/confirmation-dialog.component';
import {MatButtonModule} from '@angular/material/button';
import {TableComponent} from './components/table/table.component';
import {MatTableModule} from "@angular/material/table";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {SelectPipe} from './pipes/select.pipe';
import {MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {TableHeaderComponent} from './components/table-header/table-header.component';
import {MessageService} from "./services/message.service";
import {FormDialogService} from "./services/form-dialog.service";
import {TranslateModule} from "@ngx-translate/core";
import {TablePaginatorLabels} from "./services/table-paginator-labels.service";
import { FormDialogComponent } from './components/form-dialog/form-dialog.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {ReactiveFormsModule} from "@angular/forms";
import { FormDialogContentDirective } from './components/form-dialog/form-dialog-content.directive';

@NgModule({
  declarations: [
    ConfirmitionDialogComponent,
    TableComponent,
    SelectPipe,
    TableHeaderComponent,
    FormDialogComponent,
    FormDialogContentDirective,
  ],
  exports: [
    TableComponent,
    TableHeaderComponent,
    TranslateModule,
    FormDialogComponent,
    FormDialogContentDirective
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatDialogModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    MatSnackBarModule,
    MatPaginatorModule,
    TranslateModule,
    MatFormFieldModule,
    ReactiveFormsModule
  ],
  providers: [
    MessageService,
    FormDialogService,
    {provide: MatPaginatorIntl, useClass: TablePaginatorLabels}
  ]
})
export class SharedModule {
}
