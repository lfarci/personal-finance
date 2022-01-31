import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {User} from "../services/user.model";
import {FormControl, Validators} from "@angular/forms";
import {UserSubmission} from "@rest-client/model/userSubmission";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit {

  user?: User;

  firstName = new FormControl("", [
    Validators.required,
    Validators.minLength(1),
  ]);

  lastName = new FormControl("", [
    Validators.required,
    Validators.minLength(1),
  ]);

  @Output() onConfirm = new EventEmitter<boolean>();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    if (data && data.resource) {
      this.user = data.resource;
    }
  }

  get edit() { return this.user !== undefined; }

  get titleKey() { return this.edit ? 'users.user.editTitle' : 'users.user.createTitle'; }

  get valid() { return this.firstName.valid && this.lastName.valid; }

  ngOnInit(): void {
    this.firstName.setValue(this.user?.firstName);
    this.lastName.setValue(this.user?.lastName);
  }

  get submittedUser(): UserSubmission {
    return {
      ...this.user,
      firstName: this.firstName.value,
      lastName: this.lastName.value
    };
  }

}
