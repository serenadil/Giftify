import {Account} from './Account';
import {WishList} from './WishList';
import {AccountCommunityName} from './AccountCommunityName';
import {GiftAssignment} from './GiftAssignment';

export class Community {
  id: string;
  userList: Account [];
  accessCode: string;
  admin: Account;
  communityName: string;
  communityNote: string;
  budget: number;
  deadline: Date;
  giftAssignments: Set<GiftAssignment>;
  wishlists: Set<WishList>;
  communityNames: Set<AccountCommunityName>;

  active: boolean;
  closed: boolean;

  constructor(id: string, accessCode: string, admin: Account, communityName: string, communityNote: string, budget: number, deadline: Date) {
    this.id = id;
    this.userList = [];
    this.accessCode = accessCode;
    this.admin = admin;
    this.communityName = communityName;
    this.communityNote = communityNote;
    this.budget = budget;
    this.deadline = deadline;
    this.wishlists = new Set();
    this.giftAssignments = new Set();
    this.communityNames = new Set();
    this.active = true;
    this.closed = false;
  }

  getAccountCommunityNameByAccount(account: Account): string | null {
    for (const acn of this.communityNames) {
      if (acn.account === account) {
        return acn.userCommunityName;
      }
    }
    return null;
  }

}
