import {Account} from './Account';
import {WishList} from './WishList';
import {AccountCommunityName} from './AccountCommunityName';
import {GiftAssignment} from './GiftAssignment';
export class Community {
  id: string;
  userList: Account[];
  accessCode: string;
  admin: Account;
  communityName: string;
  communityNote: string;
  budget: number;
  deadline: Date;
  giftAssignments: Set<GiftAssignment>;
  wishlists: Set<WishList>;
  communityNames: AccountCommunityName[];


  active: boolean;
  close: boolean;

  constructor(
    id: string,
    accessCode: string,
    admin: Account,
    communityName: string,
    communityNote: string,
    budget: number,
    deadline: Date,
    active:boolean,
    close: boolean
  ) {
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
    this.communityNames = [];
    this.active = active;
    this.close = close;
  }

  getUserWishListByCommunityName(accountCommunityName: string): WishList | null {
    const account = this.getAccountByCommunityName(accountCommunityName);
    console.log(account?.email)
    if (!account) return null;

    for (const wishList of this.wishlists) {
      if (wishList.user.email === account.email) {
        console.log(wishList);
        return wishList;
      }
    }
    return null;
  }

  getCommunityNameByAccount(account: Account): AccountCommunityName | null {
    console.log(account);
    for (const acn of this.communityNames) {
      if (acn.account.email === account.email) {
        return acn;
      }
    }
    return null;
  }


  getAccountByCommunityName(communityName: string): Account | null {
    for (const acn of this.communityNames) {
      if (acn.userCommunityName === communityName) {
        return acn.account;
      }
    }
    return null;
  }
}
