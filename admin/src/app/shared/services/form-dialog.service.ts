import { Injectable } from '@angular/core';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {filter, mergeMap} from "rxjs/operators";
import {ComponentType} from "@angular/cdk/overlay";
import {Observable} from "rxjs";

export type ResultHandler<Resource> = (r: Resource) => Observable<Resource>;

@Injectable({
  providedIn: 'root'
})
export class FormDialogService<Component, Resource> {

  constructor(private readonly dialog: MatDialog) {}

  open = (component: ComponentType<Component>, handler: ResultHandler<Resource>, config?: MatDialogConfig) => {
    const dialogRef = this.dialog.open(component, config);
    return dialogRef.afterClosed().pipe(
      filter(result => result !== undefined),
      mergeMap(handler)
    );
  };

}
