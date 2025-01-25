import {AccountCommunityRole} from './AccountCommunityRole';
import {Community} from "./Community";
import {Role} from './Role';

export class Account {
  id: number;
  username: string;
  email: string;
  password: string;
  userCommunities: Community[];
  communityRoles: AccountCommunityRole[];

  constructor(id: number, username: string, email: string, password: string) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.userCommunities = [];
    this.communityRoles = [];
  }




}
