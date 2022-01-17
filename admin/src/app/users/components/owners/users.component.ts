import {Component, OnInit} from '@angular/core';
import {User} from '../../services/user.model';
import {UsersService} from '../../services/users.service';

@Component({
  selector: 'app-owners',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  displayedColumns: string[] = [ 'id', 'name', 'actions'];
  dataSource: User[] = [];
  user: User = {name: ""};

  constructor(private readonly service: UsersService) {
  }

  ngOnInit(): void {
    this.service.getUsers().subscribe(this.handleUsers);
  }

  submitUser() {
    this.service.saveUser(this.user).subscribe({
      next: this.handleUser,
      error: console.error
    });
  }

  deleteUser = (id: number) => {
    this.service.deleteUserById(id).subscribe();
    this.dataSource = this.dataSource.filter(u => u.id !== id);
  };

  private handleUser = (user: User) => {
    this.dataSource.push(user);
    this.dataSource = [...this.dataSource];
  };

  private handleUsers = (owners: User[]) => {
    this.dataSource = owners;
  };

}
