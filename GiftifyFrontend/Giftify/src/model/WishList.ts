import { Wish } from "./Wish";
import {Account} from "./Account";
import {Community} from './Community';

export class WishList {
  id: number;
  wishes: Wish[];
  user: Account;

  constructor(id: number, user: Account) {
    this.id = id;
    this.wishes = [];
    this.user = user;
  }

}
