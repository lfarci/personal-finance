import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OwnersComponent } from './components/owners/owners.component';
import { OwnersRoutingModule } from './owners-routing.module';
import { MatTableModule } from '@angular/material/table';


@NgModule({
  declarations: [
    OwnersComponent
  ],
  imports: [
    CommonModule,
    OwnersRoutingModule,
    MatTableModule
  ]
})
export class OwnersModule { }
