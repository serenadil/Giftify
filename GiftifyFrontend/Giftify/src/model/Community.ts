import {Account} from './Account';
import {WishList} from './WishList';

export class Community {
  id: number;
  userList: Account [];
  accessCode: string;
  admin: Account;
  communityName: string;
  communityNote: string;
  budget: number;
  deadline: Date;
  giftAssignments: Map<Account, Account>;
  wishlists: Map<Account, WishList>;
  active: boolean;
  closed: boolean;

  constructor(id: number, accessCode: string, admin: Account, communityName: string, communityNote: string, budget: number, deadline: Date) {
    this.id = id;
    this.userList = [];
    this.accessCode = accessCode;
    this.admin = admin;
    this.communityName = communityName;
    this.communityNote = communityNote;
    this.budget = budget;
    this.deadline = deadline;
    this.wishlists = new Map();
    this.giftAssignments = new Map();
    this.active = true;
    this.closed = false;
  }

  addUser (user: Account, wishList : WishList) {
    this.userList.push(user);
    if (!this.wishlists) {
      this.wishlists = new Map();
    }
    this.wishlists.set(user, wishList);
  }

  removeUser (user: Account) {
    this.userList = this.userList.filter(s => s.id !== user.id );
    this.wishlists.delete(user);
  }

  getGiftReceiver (userGiver: Account): Account | undefined {
    return this.giftAssignments.get(userGiver);
  }

  getUserWishList (user : Account): WishList | undefined {
    return this.wishlists.get(user);
  }
}
