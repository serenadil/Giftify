import { Wish } from "./Wish";
import {Account} from "./Account";

export class WishList {
  id: number;
  wishes: Wish[];
  user: Account;

  constructor(id: number, user: Account) {
    this.id = id;
    this.wishes = [];
    this.user = user;
  }

  addWish (wish: Wish) {
    return this.wishes.push(wish);
  }

  removeWish (wish: Wish) {
    this.wishes.filter(s => s.id !== wish.id);
  }
}
