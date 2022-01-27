import {Component, OnInit} from '@angular/core';
import {User} from '../services/user.model';
import {UsersService} from '../services/users.service';
import {RowOption} from "../../shared/row-option.model";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {UserFormComponent} from "../user/user-form.component";
import {filter, mergeMap} from "rxjs/operators";

@Component({
  selector: 'app-owners',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  displayedColumns: string[] = [ 'firstName', 'lastName', 'creationDate', 'updateDate', 'options'];
  dataSource: User[] = [];

  rowOptions: RowOption<User>[];

  constructor(
    private readonly service: UsersService,
    private readonly router: Router,
    private readonly snackBar: MatSnackBar,
    private readonly dialog: MatDialog
  ) {
    this.rowOptions = [
      {
        label: "Bank Accounts",
        icon: "account_balance_wallet",
        action: this.showBankAccountsFor
      }
    ];
  }

  ngOnInit(): void {
    this.service.getUsers().subscribe(this.handleUsers);
  }

  showBankAccountsFor = (user: User) => {
    this.router.navigate(['users', user.id, 'accounts']);
  };

  create = () => {
    const dialogRef = this.dialog.open(UserFormComponent);
    dialogRef.afterClosed().pipe(
      filter(user => user !== undefined),
      mergeMap(this.service.saveUser),
    ).subscribe({
      next: this.afterSuccessfulCreate,
      error: this.afterFailedCreate
    });
  };

  private afterSuccessfulCreate = (user: User) => {
    this.dataSource.push(user);
    this.dataSource = [...this.dataSource];
    this.showMessage(`User "${user.firstName}" has been created.`);
  };

  private afterFailedCreate = () => {
    this.showMessage(`User could not be created.`);
  };

  edit(user: User) {
    const config = { data: { resource: user }};
    const dialogRef = this.dialog.open(UserFormComponent, config);
    dialogRef.afterClosed().pipe(
      filter(user => user !== undefined),
      mergeMap(editedUser => this.service.updateUser(editedUser.id!!, editedUser)),
    ).subscribe({
      next: this.afterSuccessfulEdit,
      error: () => this.afterFailedEdit(user)
    });
  }

  private afterSuccessfulEdit = (user: User) => {
    const index = this.dataSource.findIndex(u => u.id === user.id);
    this.dataSource[index] = user;
    this.dataSource = [...this.dataSource];
    this.showMessage(`User "${user.firstName}" has been updated.`);
  };

  private afterFailedEdit = (user: User) => {
    this.showMessage(`User "${user.firstName}" could not be updated.`);
  };

  delete = (user: User) => {
    if (user.id) {
      this.service.deleteUserById(user.id).subscribe({
        next: () => {
          this.afterSuccessfulDelete(user);
        },
        error: () => {
          this.afterFailedDelete(user);
        }
      });
    }
  };

  afterSuccessfulDelete = (user: User) => {
    this.dataSource = this.dataSource.filter(u => u.id !== user.id);
    this.showMessage(`User "${user.firstName}" has been successfully deleted.`);
  }

  afterFailedDelete = (user: User) => {
    this.showMessage(`User "${user.firstName}" could not be deleted.`);
  }

  private handleUsers = (owners: User[]) => {
    this.dataSource = owners;
  };

  private showMessage = (message: string) => {
    this.snackBar.open(message, "DISMISS", {
      duration: 3000
    });
  };

}
