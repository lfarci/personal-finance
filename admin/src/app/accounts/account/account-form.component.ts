import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {BankAccount} from "@rest-client/model/bankAccount";
import {BankAccountSubmission} from "@rest-client/model/bankAccountSubmission";
import {User} from "@rest-client/model/user";

@Component({
  selector: 'app-account',
  templateUrl: './account-form.component.html',
  styleUrls: ['./account-form.component.scss']
})
export class AccountFormComponent implements OnInit {

  user?: User;
  bankAccount?: BankAccount;

  bankAccountForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(1)]),
    iban: new FormControl(),
    balance: new FormControl(0),
  });

  @Output() onConfirm = new EventEmitter<boolean>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    if (data && data.resource) {
      this.bankAccount = data.resource;
    }
    if (data && data.user) {
      this.user = data.user;
    }
  }

  get edit() { return this.bankAccount !== undefined; }

  get titleKey() { return this.edit ? 'accounts.account.editTitle' : 'accounts.account.createTitle'; }

  get valid() { return this.bankAccountForm.valid; }

  isValid = (propertyName: string): boolean => {
    const control = this.bankAccountForm.get(propertyName);
    return control !== null && control.valid;
  };

  ngOnInit(): void {
    if (this.bankAccount) {
      this.bankAccountForm.patchValue(this.bankAccount);
    }
  }

  get submittedBankAccount(): BankAccountSubmission {
    const account = {...this.bankAccount, ...this.bankAccountForm.value};
    if (this.user) {
      account.userId = this.user.id;
    }
    return account;
  }

}
