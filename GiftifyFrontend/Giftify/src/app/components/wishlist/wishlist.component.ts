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
  wishlistInfo: any = null;
  wishes: any[] = [];
  newWish = {
    name: '',
    imagePath: ''
  }
  communityId: number = 0;
  newWishName: string = '';
  newWishImage: string = '';

  constructor(private wishService: WishService) {}

  addWish() {
    if (!this.newWish.name) {
      alert('Il nome del desiderio è obbligatorio.');
      return;
    }
    this.wishService.addWish(this.communityId, this.newWish).subscribe({
      next: (message) => {
        alert(message);
        this.newWish = {
          name: '',
          imagePath: ''
        };
      },
      error: err => {
        alert(err.error||'Errore durante la creazione del desiderio');
      }
    });
  }

  deleteWish() {
    this.wishService.deleteWish(this.communityId).subscribe({
      next: (message) => {
        alert(message);
      },
      error: err => {
        alert(err.error);
      }
    });
  }

  editWish() {
    alert('ancora da implementare')
  }
}
