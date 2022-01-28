import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-table-header',
  templateUrl: './table-header.component.html',
  styleUrls: ['./table-header.component.scss']
})
export class TableHeaderComponent {

  @Input()
  title!: string;

  @Input()
  createButtonLabel: string = "Create";

  @Output()
  create = new EventEmitter<void>();

  constructor() { }

}
