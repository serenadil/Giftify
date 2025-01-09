import {Account} from './Account';

export class AccountCommunityName {

  id: number;

  account: Account;

  userCommunityName: string;

  constructor(
    id: number,
    account: Account,
    userCommunityName: string
  ) {
    this.id = id;
    this.account = account;
    this.userCommunityName = userCommunityName;
  }
}
