import { Component } from '@angular/core';
import {WishService} from '../../services/wish.service';
import {Wish} from '../../../model/Wish';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-wishlist',
  standalone: false,

  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.css'
})
export class WishlistComponent {
  wishes: any[] = [];
  newWish = {
    name: '',
    imagePath: ''
  }
  constructor(private wishService: WishService) {}

  // onAddWish() : {
  // }


}
