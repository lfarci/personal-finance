import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatDialogModule} from '@angular/material/dialog';
import { ConfirmitionDialogComponent } from './confirmation-dialog/confirmation-dialog.component';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [
    ConfirmitionDialogComponent
  ],
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ]
})
export class SharedModule { }
