import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatDialogModule} from '@angular/material/dialog';
import { ConfirmitionDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { MatButtonModule } from '@angular/material/button';
import { TableComponent } from './table/table.component';
import {MatTableModule} from "@angular/material/table";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import { TitleCasePipe } from './title-case.pipe';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { SelectPipe } from './select.pipe';
import {MatPaginatorModule} from "@angular/material/paginator";

@NgModule({
  declarations: [
    ConfirmitionDialogComponent,
    TableComponent,
    TitleCasePipe,
    SelectPipe,
  ],
  exports: [
    TableComponent,
  ],
    imports: [
        CommonModule,
        MatTableModule,
        MatDialogModule,
        MatButtonModule,
        MatMenuModule,
        MatIconModule,
        MatSnackBarModule,
        MatPaginatorModule
    ]
})
export class SharedModule { }
