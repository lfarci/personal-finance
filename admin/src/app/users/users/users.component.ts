import {Component, OnInit} from '@angular/core';
import {OutdatedUserService} from '../services/outdated-user.service';
import {RowOption} from "../../shared/models/row-option.model";
import {Router} from "@angular/router";
import {UserFormComponent} from "../user/user-form.component";
import {PageEvent} from "@angular/material/paginator";
import {MessageService} from "../../shared/services/message.service";
import {FormDialogService} from "../../shared/services/form-dialog.service";
import {AppTitleService} from "../../shared/services/app-title.service";
import {UserService} from "@rest-client/api/user.service";
import {UserPage} from "@rest-client/model/userPage";
import {User} from "@rest-client/model/user";
import {UserSubmission} from "@rest-client/model/userSubmission";
import {Observable} from "rxjs";

@Component({
  selector: 'app-owners',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss'],
  providers: [FormDialogService]
})
export class UsersComponent implements OnInit {

  displayedColumns: string[] = [ 'firstName', 'lastName', 'creationDate', 'updateDate', 'options'];
  dataSource: User[] = [];

  rowOptions: RowOption<User>[];

  pageIndex: number = 0;
  pageSize: number = 10;
  totalNumberOfElements: number = 0;

  edit$ = this.userService.updateById;

  constructor(
    private readonly title: AppTitleService,
    private readonly service: OutdatedUserService,
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly message: MessageService,
    private readonly formDialog: FormDialogService<UserFormComponent>
  ) {
    this.title.setTitle("users.documentTitle");
    this.formDialog.use(UserFormComponent);
    this.rowOptions = [
      {
        label: "users.accounts",
        icon: "account_balance_wallet",
        action: this.showBankAccountsFor
      }
    ];
  }

  ngOnInit(): void {
    this.getCurrentUserPage();
  }

  create = () => {
    this.afterCreated().subscribe({
      next: this.afterSuccessfulCreation,
      error: this.afterCreationFailed
    });
  };

  edit = (user: User) => {
    this.formDialog.afterClosed(user).subscribe({
      next: this.afterSubmitted,
      error: this.afterSubmissionFailed
    });
  };

  delete = (user: User) => {
    this.service.deleteUserById(user.id).subscribe({
      next: () => this.afterDeleted(user),
      error: () => this.afterDeleteFailed(user)
    });
  };

  showBankAccountsFor = async (user: User) => {
    await this.router.navigate(['users', user.id, 'accounts']);
  };

  handlePageChange($event: PageEvent) {
    this.pageSize = $event.pageSize;
    this.pageIndex = $event.pageIndex;
    this.getCurrentUserPage();
  }

  private getCurrentUserPage = () => {
    this.userService.findAll(this.pageIndex, this.pageSize).subscribe(this.handlePage);
  };

  private afterCreated(): Observable<User> {
    const handler = this.userService.create.bind(this.userService);
    return this.formDialog.afterCreated<UserSubmission, User>(handler);
  }

  private afterSubmitted = (user: User) => {
    this.userService.updateById(user.id, user).subscribe({
      next: () => this.afterUpdated(user),
      error: () => this.afterUpdateFailed(user)
    });
  }

  private afterUpdated = (user: User): void => {
    const index = this.dataSource.findIndex(u => u.id === user.id);
    user.updateDate = new Date().toISOString();
    this.dataSource[index] = user;
    this.dataSource = [...this.dataSource];
    this.message.info(`${UsersComponent.format(user)} has been updated.`);
  };

  private afterUpdateFailed = (user: User): void => {
    this.message.info(`${UsersComponent.format(user)} could not be updated.`);
  };

  private afterSuccessfulCreation = (user: User) => {
    this.dataSource.unshift(user);
    this.dataSource = [...this.dataSource];
    this.message.info(`${UsersComponent.format(user)} has been created.`);
  };

  private afterCreationFailed = () => {
    this.message.info(`User could not be created.`);
  };

  private afterSubmissionFailed = (user: User) => {
    this.message.info(`${UsersComponent.format(user)} could not be updated.`);
  };

  private afterDeleted = (user: User) => {
    this.dataSource = this.dataSource.filter(u => u.id !== user.id);
    this.message.info(`${UsersComponent.format(user)} has been successfully deleted.`);
  }

  private afterDeleteFailed = (user: User) => {
    this.message.info(`${UsersComponent.format(user)} could not be deleted.`);
  }

  private handlePage = (page: UserPage) => {
    this.totalNumberOfElements = page.totalElements;
    this.handleUsers(page.content);
  };

  private handleUsers = (owners: User[]) => {
    this.dataSource = owners;
  };

  private static format = (user: User) => {
    return `User "${user.firstName} ${user.lastName}"`;
  };

}
