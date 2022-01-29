import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatDialogModule} from '@angular/material/dialog';
import {ConfirmitionDialogComponent} from './components/confirmation-dialog/confirmation-dialog.component';
import {MatButtonModule} from '@angular/material/button';
import {TableComponent} from './components/table/table.component';
import {MatTableModule} from "@angular/material/table";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {TitleCasePipe} from './pipes/title-case.pipe';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {SelectPipe} from './pipes/select.pipe';
import {MatPaginatorIntl, MatPaginatorModule} from "@angular/material/paginator";
import {TableHeaderComponent} from './components/table-header/table-header.component';
import {MessageService} from "./services/message.service";
import {FormDialogService} from "./services/form-dialog.service";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {TablePaginatorLabels} from "./services/table-paginator-labels.service";

@NgModule({
  declarations: [
    ConfirmitionDialogComponent,
    TableComponent,
    TitleCasePipe,
    SelectPipe,
    TableHeaderComponent,
  ],
  exports: [
    TableComponent,
    TableHeaderComponent,
    TranslateModule
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
    TranslateModule
  ],
  providers: [
    MessageService,
    FormDialogService,
    {provide: MatPaginatorIntl, useClass: TablePaginatorLabels}
  ]
})
export class SharedModule {
}
