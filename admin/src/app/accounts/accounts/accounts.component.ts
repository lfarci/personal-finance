import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Deprecated_BankAccountService } from '../services/accounts.service';
import {ActivatedRoute} from "@angular/router";
import {BankAccountService} from "@rest-client/api/bankAccount.service";
import {BankAccount} from "@rest-client/model/bankAccount";
import {AppTitleService} from "../../shared/services/app-title.service";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'owner', 'iban', 'balance', 'actions'];
  dataSource: BankAccount[] = [];

  constructor(
    private readonly title: AppTitleService,
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly snackBar: MatSnackBar,
    private readonly service: Deprecated_BankAccountService,
    private readonly bankAccountService: BankAccountService
  ) {
    this.title.setTitle("accounts.documentTitle");

  }

  ngOnInit(): void {
    // this.route.paramMap.pipe(
    //   map((params: ParamMap) => params.get("userId")),
    //   filter(id => id !== null),
    //   mergeMap(userId => this.service.getBankAccounts(userId!!))
    // ).subscribe({
    //     next: this.handleBankAccounts,
    //     error: this.handleError
    // });
  }

  getCurrentBankAccountPage = () => {
    // this.service.findAll(this.pageIndex, this.pageSize).subscribe(this.handlePage);

  };

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
