import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersComponent } from './components/owners/users.component';
import { UsersRoutingModule } from './users-routing.module';
import { MatTableModule } from '@angular/material/table';
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatCard, MatCardModule} from "@angular/material/card";


@NgModule({
  declarations: [
    UsersComponent
  ],
  imports: [
    CommonModule,
    UsersRoutingModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    FormsModule,
    MatIconModule,
    MatCardModule
  ]
})
export class UsersModule { }
