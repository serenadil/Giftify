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

  addOrUpdateRoleForCommunity(accountCommunityRole: AccountCommunityRole) {
    const existingRole = this.communityRoles.find(role => role.community.id === accountCommunityRole.community.id);
    if (existingRole) {
      existingRole.role = accountCommunityRole.role;
    } else {
      this.communityRoles.push(accountCommunityRole);
    }
  }

  addCommunity(community: Community) {
    if (this.userCommunities.some(comm => comm.id !== community.id)) {
      this.userCommunities.push(community);
    }
  }

  removeCommunity(community: Community) {
    this.userCommunities.filter(comm => comm.id !== community.id);
  }

  getRoleForCommunity(community: Community): Role {
    const role = this.communityRoles.find(r => r.community.id === community.id);
    return role ? role.role : Role.STANDARD;
  }

  static checkEmail (email: string): boolean {
    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;
    return emailRegex.test(email);
  }

  static checkPassword (password: string): boolean {
    return password.length >= 8 && /\d/.test(password);
  }


}
