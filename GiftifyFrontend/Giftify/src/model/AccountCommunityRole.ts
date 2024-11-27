import {Role} from './Role';
import {Community} from './Community';
import {Account} from './Account';

export class AccountCommunityRole {
  id: number;
  community: Community;
  user: Account;
  role: Role;

  constructor(id: number, community: Community, user: Account, role: Role) {
    this.id = id;
    this.community = community;
    this.user = user;
    this.role = role;
  }
}
