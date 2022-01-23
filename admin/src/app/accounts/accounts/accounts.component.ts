import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BankAccount } from '../services/account.model';
import { BankAccountService } from '../services/accounts.service';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {filter, map, mergeMap} from "rxjs/operators";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'owner', 'iban', 'balance', 'actions'];
  dataSource: BankAccount[] = [];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly snackBar: MatSnackBar,
    private readonly service: BankAccountService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      map((params: ParamMap) => params.get("userId")),
      filter(id => id !== null),
      mergeMap(userId => this.service.getBankAccounts(userId!!))
    ).subscribe({
        next: this.handleBankAccounts,
        error: this.handleError
    });
  }

  private handleBankAccounts = (bankAccounts: BankAccount[]) => {
    this.dataSource = bankAccounts;
  };

  private handleError = () => {
    this.showMessage("Failed to load accounts.");
  };

  private showMessage = (message: string) => {
    this.snackBar.open(message, "DISMISS", {
      duration: 3000
    });
  };
}
