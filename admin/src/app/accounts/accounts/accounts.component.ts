import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Deprecated_BankAccountService } from '../services/accounts.service';
import {ActivatedRoute, ParamMap} from "@angular/router";
import {BankAccountService} from "@rest-client/api/bankAccount.service";
import {BankAccount} from "@rest-client/model/bankAccount";
import {AppTitleService} from "../../shared/services/app-title.service";
import {filter, map, mergeMap} from "rxjs/operators";
import {BankAccountPage} from "@rest-client/model/bankAccountPage";
import {Observable} from "rxjs";
import {RowOption} from "../../shared/models/row-option.model";
import {PageEvent} from "@angular/material/paginator";
import {User} from "@rest-client/model/user";
import {UserService} from "@rest-client/api/user.service";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  user?: User;

  displayedColumns: string[] = ['name', 'owner', 'iban', 'balance', 'options'];
  dataSource: BankAccount[] = [];

  rowOptions: RowOption<BankAccount>[];

  pageIndex: number = 0;
  pageSize: number = 10;
  totalNumberOfElements: number = 0;

  constructor(
    private readonly title: AppTitleService,
    private readonly route: ActivatedRoute,
    private readonly dialog: MatDialog,
    private readonly snackBar: MatSnackBar,
    private readonly service: Deprecated_BankAccountService,
    private readonly bankAccountService: BankAccountService,
    private readonly userService: UserService,
  ) {
    this.title.setTitle("accounts.documentTitle");
    this.rowOptions = [];
  }

  ngOnInit(): void {
    this.getCurrentUser();
    this.getCurrentBankAccountPage();
  }

  get currentUser$(): Observable<User> {
    return this.route.paramMap.pipe(
      map((params: ParamMap) => params.get("userId")),
      filter(userId => userId !== null),
      mergeMap(userId => this.userService.findById(parseInt(userId!!)))
    );
  }

  get currentBankAccountPage$(): Observable<BankAccountPage> {
    return this.route.paramMap.pipe(
      map((params: ParamMap) => params.get("userId")),
      filter(userId => userId !== null),
      mergeMap(userId => this.bankAccountService.findAll(parseInt(userId!!), this.pageIndex, this.pageSize))
    );
  }

  handlePageChange($event: PageEvent) {
    this.pageSize = $event.pageSize;
    this.pageIndex = $event.pageIndex;
    this.getCurrentBankAccountPage();
  }

  private getCurrentBankAccountPage = () => {
    this.currentBankAccountPage$.subscribe({
        next: this.handlePage,
        error: this.handleError
    });
  };

  private getCurrentUser = () => {
    this.currentUser$.subscribe({
      next: user => this.user = user,
      error: this.handleError
    });
  };

  private handlePage = (page: BankAccountPage) => {
    this.totalNumberOfElements = page.totalElements;
    this.handleBankAccounts(page.content)
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

  create() {
    console.log("create")
  }
}
