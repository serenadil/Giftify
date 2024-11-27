import {WishList} from "./WishList";

export class Wish {
  id: number;
  name: string;
  imagePath: string;
  wishList: WishList;

  constructor(id: number, name: string, imagePath: string, wishList: WishList) {
    this.id = id;
    this.name = name;
    this.imagePath = imagePath;
    this.wishList = wishList;
  }
}
