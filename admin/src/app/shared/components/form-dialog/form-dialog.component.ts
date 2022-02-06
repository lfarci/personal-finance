import {Component, ContentChild, EventEmitter, Input, Output} from '@angular/core';
import {FormDialogContentDirective} from "./form-dialog-content.directive";

@Component({
  selector: 'app-form-dialog',
  templateUrl: './form-dialog.component.html',
  styleUrls: ['./form-dialog.component.scss']
})
export class FormDialogComponent<Resource> {

  @Input() edit = true;
  @Input() title = "";
  @Input() closeResult!: Resource;
  @Input() valid = true;
  @Output() cancel = new EventEmitter<void>();

  @ContentChild(FormDialogContentDirective) content!: FormDialogContentDirective;

  constructor() {}

  get primaryActionLabel() { return this.edit ? 'edit' : 'create'; }

}
