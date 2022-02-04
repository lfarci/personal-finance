import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmitionDialogComponent implements OnInit {

  title!: string;
  message!: string;
  @Output() onConfirm = new EventEmitter<boolean>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<ConfirmitionDialogComponent>
  ) {
    this.title = data.title;
    this.message = data.message;
  }

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
  }

}
