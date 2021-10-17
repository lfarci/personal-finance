import { Component, OnInit } from '@angular/core';
import { Owner } from '../../services/owner.model';
import { OwnersService } from '../../services/owners.service';

@Component({
  selector: 'app-owners',
  templateUrl: './owners.component.html',
  styleUrls: ['./owners.component.scss']
})
export class OwnersComponent implements OnInit {

  owners: Owner[] = [];

  constructor(private readonly service: OwnersService) { }

  ngOnInit(): void {
    this.service.getOwners().subscribe(this.handleOwners);
  }

  private handleOwners = (owners: Owner[]) => {
    this.owners = owners;
  };

}
