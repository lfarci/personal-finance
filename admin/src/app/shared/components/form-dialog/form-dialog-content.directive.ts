import {Directive, TemplateRef} from '@angular/core';

@Directive({
  selector: '[appFormDalogContent]'
})
export class FormDialogContentDirective {
  constructor(public template: TemplateRef<any>) { }
}
