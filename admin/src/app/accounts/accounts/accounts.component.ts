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
import {FormDialogService} from "../../shared/services/form-dialog.service";
import {AccountFormComponent} from "../account/account-form.component";
import {BankAccountSubmission} from "@rest-client/model/bankAccountSubmission";
import {HttpErrorResponse} from "@angular/common/http";
import {MessageService} from "../../shared/services/message.service";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  user?: User;

  displayedColumns: string[] = ['name', 'iban', 'balance', 'creationDate', 'updateDate', 'options'];
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
    private readonly message: MessageService,
    private readonly formDialog: FormDialogService<AccountFormComponent>
  ) {
    this.title.setTitle("accounts.documentTitle");
    this.formDialog.use(AccountFormComponent);
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
        error: this.afterCreationFailed
    });
  };

  private getCurrentUser = () => {
    this.currentUser$.subscribe({
      next: user => this.user = user,
      error: this.afterCreationFailed
    });
  };

  private afterCreated(): Observable<BankAccount> {
    const handler = (bankAccount: BankAccountSubmission) => {
      return this.bankAccountService.create(this.user?.id!!, bankAccount);
    };
    return this.formDialog.afterCreated<BankAccountSubmission, BankAccount>(handler, {
      data: {
        user: this.user
      }
    });
  }

  private handlePage = (page: BankAccountPage) => {
    this.totalNumberOfElements = page.totalElements;
    this.handleBankAccounts(page.content)
  };

  private handleBankAccounts = (bankAccounts: BankAccount[]) => {
    this.dataSource = bankAccounts;
  };

  private afterCreationFailed = (error: HttpErrorResponse) => {
    if (error && error.status === 409) {
      this.message.info(`accounts.errors.creationConflict`);
    } else {
      this.message.info(`accounts.errors.created`);
    }
  };

  create = () => {
    this.afterCreated().subscribe({
      next: this.afterSuccessfulCreation,
      error: this.afterCreationFailed
    });
  };

  edit(bankAccount: BankAccount) {
    this.formDialog.afterClosed(bankAccount).subscribe({
      next: (submitted: BankAccount) => this.afterSubmitted(submitted),
      error: () => this.afterSubmissionFailed(bankAccount)
    });
  }

  delete = (bankAccount: BankAccount) => {
    this.bankAccountService.deleteById(bankAccount.userId, bankAccount.id).subscribe({
      next: () => this.afterDeleted(bankAccount),
      error: this.afterCreationFailed
    });
  };

  private afterSuccessfulCreation = (bankAccount: BankAccount) => {
    this.dataSource.unshift(bankAccount);
    this.dataSource = [...this.dataSource];
    this.message.info(`accounts.messages.created`, {
      name: bankAccount.name
    });
  };

  private afterSubmitted = (bankAccount: BankAccount) => {
    if (this.user && this.user.id) {
      this.bankAccountService.updateForUserId(this.user.id, bankAccount.id, bankAccount).subscribe({
        next: () => this.afterUpdated(bankAccount),
        error: (error: HttpErrorResponse) => this.afterUpdateFailed(bankAccount, error)
      });
    } else {
      this.afterUpdateFailed(bankAccount);
    }
  }

  private afterUpdated = (bankAccount: BankAccount): void => {
    const index = this.dataSource.findIndex(b => b.id === bankAccount.id);
    bankAccount.updateDate = new Date().toISOString();
    this.dataSource[index] = bankAccount;
    this.dataSource = [...this.dataSource];
    this.message.info(`accounts.messages.updated`, {
      name: bankAccount.name,
    });
  };

  private afterUpdateFailed = (bankAccount: BankAccount, error?: HttpErrorResponse): void => {
    if (error && error.status === 409) {
      this.message.info(`accounts.errors.updateConflict`, {
        name: bankAccount.name
      });
    } else {
      this.message.info(`accounts.errors.updated`, {
        name: bankAccount.name
      });
    }
  };

  private afterSubmissionFailed = (bankAccount: BankAccount): void => {
    this.message.info(`accounts.errors.updated`, {
      name: bankAccount.name
    });
  };

  private afterDeleted(bankAccount: BankAccount) {
    this.getCurrentBankAccountPage();
    this.message.info(`accounts.messages.deleted`, {
      name: bankAccount.name
    });
  }

}
