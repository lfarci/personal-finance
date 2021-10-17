import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OwnersComponent } from './components/owners/owners.component';
import { OwnersRoutingModule } from './owners-routing.module';



@NgModule({
  declarations: [
    OwnersComponent
  ],
  imports: [
    CommonModule,
    OwnersRoutingModule
  ]
})
export class OwnersModule { }
