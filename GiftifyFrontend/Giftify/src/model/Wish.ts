import {WishList} from "./WishList";
import {WishCategory} from './WishCategory';

export class Wish {
  id: number;
  name: string;
  category: WishCategory;
  wishList: WishList;

  constructor(id: number, name: string, category: WishCategory, wishList: WishList) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.wishList = wishList;
  }
}
