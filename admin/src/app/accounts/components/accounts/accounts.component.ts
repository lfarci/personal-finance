import { Component, OnInit } from '@angular/core';
import { BankAccount } from '../../services/account.model';
import { BankAccountService } from '../../services/accounts.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss']
})
export class AccountsComponent implements OnInit {

  displayedColumns: string[] = ['name', 'owner', 'iban', 'balance'];
  dataSource: BankAccount[] = [];

  constructor(private readonly service: BankAccountService) { }

  ngOnInit(): void {
    this.service.getBankAccounts().subscribe(this.handleBankAccounts);
  }

  private handleBankAccounts = (bankAccounts: BankAccount[]) => {
    this.dataSource = bankAccounts;
  };

}
