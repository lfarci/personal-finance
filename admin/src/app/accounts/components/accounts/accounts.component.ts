import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmitionDialogComponent } from 'src/app/shared/confirmation-dialog/confirmation-dialog.component';
import { BankAccount } from '../../services/account.model';
import { BankAccountService } from '../../services/accounts.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'owner', 'iban', 'balance', 'actions'];
  dataSource: BankAccount[] = [];

  constructor(
    private readonly dialog: MatDialog,
    private readonly snackBar: MatSnackBar,
    private readonly service: BankAccountService
  ) { }

  ngOnInit(): void {
    this.service.getBankAccounts().subscribe(this.handleBankAccounts);
  }

  onEdit = (bankAccount: BankAccount): void => {
    console.log("edit: ", bankAccount);
  };

  private openConfirmationDialog = (bankAccount: BankAccount): void => {
    const dialogRef = this.dialog.open(ConfirmitionDialogComponent, {
      data: {
        title: "Bank account deletion",
        message: `Do you really want to delete the bank account named "${bankAccount.name}"? It won't be possible to recover it.`
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      this.handleConfirmationDialogResult(bankAccount, result);
    });
  };

  handleConfirmationDialogResult = (bankAccount: BankAccount, confirmed: boolean | undefined) => {
    if (confirmed) {
      this.onDeleteConfirmation(bankAccount);
    }
  }

  onDeleteConfirmation = (bankAccount: BankAccount) => {
    this.service.deleteById(bankAccount.id).subscribe(() => this.afterDelete(bankAccount));
  };

  afterDelete = (bankAccount: BankAccount) => {
    const message = `Bank account named "${bankAccount.name} has been deleted."`;
    this.snackBar.open(message, "Dismiss");
  }

  onDelete = (bankAccount: BankAccount): void => {
    this.openConfirmationDialog(bankAccount);
  };

  private handleBankAccounts = (bankAccounts: BankAccount[]) => {
    this.dataSource = bankAccounts;
  };

}
