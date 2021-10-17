import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AccountsRoutingModule } from './accounts-routing.module';
import { AccountsComponent } from './components/accounts/accounts.component';
import { MatTableModule } from '@angular/material/table';

@NgModule({
  declarations: [
    AccountsComponent
  ],
  imports: [
    CommonModule,
    AccountsRoutingModule,
    AccountsRoutingModule,
    MatTableModule
  ]
})
export class AccountsModule { }
