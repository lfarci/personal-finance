import {Injectable} from '@angular/core';
import {MatDialog, MatDialogConfig, MatDialogRef} from "@angular/material/dialog";
import {filter, mergeMap} from "rxjs/operators";
import {ComponentType} from "@angular/cdk/overlay";
import {Observable} from "rxjs";

export type CreationHandler<Result, Output> = (r: Result) => Observable<Output>;
export type Handler<Resource> = (r: Resource) => Observable<void>;

/**
 * This should be scoped to the calling component because different instances
 * should have different internal component.
 */
@Injectable()
export class FormDialogService<Component> {

  private _component?: ComponentType<Component> | null = null;

  constructor(private readonly dialog: MatDialog) {
  }

  get hasComponent() {
    return this._component !== null;
  }

  /**
   * The form dialog component should be provided in the constructor of the
   * calling component.
   *
   * If no value is provided then the open observable always resolves null.
   */
  use = (component: ComponentType<Component>): void => {
    this._component = component;
  };

  open = (config?: MatDialogConfig): MatDialogRef<Component> => {
    if (!this.hasComponent) {
      throw new Error('Required component was null when calling edit.');
    }
    return this.dialog.open(this._component!!, config);
  };

  afterClosed = <FormModel>(entity?: FormModel): Observable<FormModel> => {
    let config = undefined;
    if (entity) {
      config = { data: { resource: entity }};
    }
    return this.open(config).afterClosed().pipe(filter(result => result !== undefined));
  };

  /**
   * Construct an observable result for the form dialog.
   *
   * When the user clicks the submit button, the dialog content is sent to the
   * backend. The server answers with the created entity (with id and stuff).
   * This creates an observable for the created entity.
   *
   * When the user cancels the submission null is returned.
   */
  afterCreated = <Form, Response = Form>(handler: CreationHandler<Form, Response>, config?: MatDialogConfig): Observable<Response> => {
    return this.open(config).afterClosed().pipe(
      filter(result => result !== undefined),
      mergeMap(handler)
    );
  };

}
