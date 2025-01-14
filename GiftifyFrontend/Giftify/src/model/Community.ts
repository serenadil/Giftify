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
  closed: boolean;

  constructor(
    id: string,
    accessCode: string,
    admin: Account,
    communityName: string,
    communityNote: string,
    budget: number,
    deadline: Date,
    active:boolean,
    closed: boolean
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
    this.closed = closed;
  }
}
