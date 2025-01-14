import {Account} from './Account';

export class AccountCommunityName {

  id: number;

  account: Account;

  userCommunityName: String;

  constructor(
    id: number,
    account: Account,
    userCommunityName: String
  ) {
    this.id = id;
    this.account = account;
    this.userCommunityName = userCommunityName;
  }
}
