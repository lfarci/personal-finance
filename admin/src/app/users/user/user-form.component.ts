import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {User} from "../services/user.model";
import {FormControl, Validators} from "@angular/forms";

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

  get isEdit() { return this.user !== undefined; }

  get action() { return this.isEdit ? 'edit' : 'create'; }

  ngOnInit(): void {
    this.firstName.setValue(this.user?.firstName);
    this.lastName.setValue(this.user?.lastName);
  }

  get submittedUser(): User {
    return {
      ...this.user,
      firstName: this.firstName.value,
      lastName: this.lastName.value
    };
  }

}
