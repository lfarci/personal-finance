import {Component, OnInit} from '@angular/core';
import {User} from '../services/user.model';
import {UsersService} from '../services/users.service';
import {RowOption} from "../../shared/models/row-option.model";
import {Router} from "@angular/router";
import {UserFormComponent} from "../user/user-form.component";
import {Page} from "../../shared/models/page.model";
import {PageEvent} from "@angular/material/paginator";
import {Title} from "@angular/platform-browser";
import {MessageService} from "../../shared/services/message.service";
import {FormDialogService} from "../../shared/services/form-dialog.service";

@Component({
  selector: 'app-owners',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  displayedColumns: string[] = [ 'firstName', 'lastName', 'creationDate', 'updateDate', 'options'];
  dataSource: User[] = [];

  rowOptions: RowOption<User>[];

  pageIndex: number = 0;
  pageSize: number = 10;
  totalNumberOfElements: number = 0;

  create$ = this.service.saveUser;
  edit$ = this.service.updateUser;

  constructor(
    private readonly title: Title,
    private readonly service: UsersService,
    private readonly router: Router,
    private readonly message: MessageService,
    private readonly formDialog: FormDialogService<UserFormComponent, User>
  ) {
    this.title.setTitle("Admin: users");
    this.rowOptions = [
      {
        label: "Bank Accounts",
        icon: "account_balance_wallet",
        action: this.showBankAccountsFor
      }
    ];
  }

  get creationResult$() {
    return this.formDialog.open(UserFormComponent, this.create$);
  }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers = () => {
    this.service.getUsers(this.pageIndex, this.pageSize).subscribe(this.handlePage);
  };

  showBankAccountsFor = async (user: User) => {
    await this.router.navigate(['users', user.id, 'accounts']);
  };

  create = () => {
    this.creationResult$.subscribe({
      next: this.afterSuccessfulCreate,
      error: this.afterFailedCreate
    });
  };

  edit = (user: User) => {
    this.editionResultFor(user).subscribe({
      next: this.afterSuccessfulEdit,
      error: () => this.afterFailedEdit(user)
    });
  };

  delete = (user: User) => {
    if (user.id) {
      this.service.deleteUserById(user.id).subscribe({
        next: () => this.afterSuccessfulDelete(user),
        error: () => this.afterFailedDelete(user)
      });
    }
  };

  private editionResultFor(user: User) {
    const config = { data: { resource: user }};
    return this.formDialog.open(UserFormComponent, this.edit$, config);
  }

  private afterSuccessfulCreate = (user: User) => {
    this.dataSource.unshift(user);
    this.dataSource = [...this.dataSource];
    this.message.show(`User "${user.firstName}" has been created.`);
  };

  private afterFailedCreate = () => {
    this.message.show(`User could not be created.`);
  };

  private afterSuccessfulEdit = (user: User) => {
    const index = this.dataSource.findIndex(u => u.id === user.id);
    this.dataSource[index] = user;
    this.dataSource = [...this.dataSource];
    this.message.show(`User "${user.firstName}" has been updated.`);
  };

  private afterFailedEdit = (user: User) => {
    this.message.show(`User "${user.firstName}" could not be updated.`);
  };

  private afterSuccessfulDelete = (user: User) => {
    this.dataSource = this.dataSource.filter(u => u.id !== user.id);
    this.message.show(`User "${user.firstName}" has been successfully deleted.`);
  }

  private afterFailedDelete = (user: User) => {
    this.message.show(`User "${user.firstName}" could not be deleted.`);
  }

  private handlePage = (page: Page<User>) => {
    this.totalNumberOfElements = page.totalElements;
    this.handleUsers(page.content);
  };

  private handleUsers = (owners: User[]) => {
    this.dataSource = owners;
  };

  handlePageChange($event: PageEvent) {
    this.pageSize = $event.pageSize;
    this.pageIndex = $event.pageIndex;
    this.getUsers();
  }
}
