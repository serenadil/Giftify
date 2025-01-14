import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CommunityService} from '../../services/community.service';
import {HomeService} from '../../services/home.service';
import {AuthService} from '../../services/auth.service';
import {WishService} from '../../services/wish.service';
import {Role} from '../../../model/Role';


@Component({
  selector: 'app-community',
  standalone: false,

  templateUrl: './community.component.html',
  styleUrl: './community.component.css'
})
export class CommunityComponent implements OnInit {
  communityInfo: any = null;
  drawnName: string | null = null;
  userWishList: any = null;
  drawnNameWishList: any = null;
  myWishList: any = null;
  participants: any = [];
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  isWishListModalOpen = false;
  isNewWishFormOpen: boolean = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  isSuccessPopupVisible: boolean = false;
  accountInfo: any = null;
  userRole: string | null = null;
  newWish = {
    name: '',
    category: '',
  }
  createSuccessMessage: string | null = null;
  createErrorMessage: string | null = null;


  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService, private wishService: WishService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
    this.viewDrawnName();
    this.loadMyWishList()
  }

  loadAccountInfo() {
    this.communityService.getAccountInfo().subscribe({
      next: (data) => {
        this.accountInfo = data;
      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo:', err);
      },
    });
  }

  viewDrawnName(): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.viewDrawnName(communityId).subscribe({
        next: (name) =>  {
          console.log(name);
        this.drawnName = name;
      },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
          console.error(err);
        }
      });
    } else {
      this.errorMessage = 'ID della community non trovato.';
    }
  }

  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {
          this.communityInfo = data;
          this.errorMessage = null;
          this.communityService.getRoleForCommunity(communityId).subscribe({
            next: (roleData) => {
              this.userRole = roleData;  // Salva il ruolo dell'utente
            },
            error: (err) => {
              this.errorMessage = err.error || 'Errore nel caricare il ruolo dell\'utente.';
            }
          });
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
        }
      });
    } else {
      this.errorMessage = 'ID della community non trovato.';
    }
  }

  closeCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.closeCommunity(communityId).subscribe({
        next: (message) => {
          alert(message);
          this.successMessage = 'Community chiusa con successo';
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
        }
      });
    } else {
      this.errorMessage = 'errore';
    }
  }

  removeUserFromCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.removeUserFromCommunity(communityId, this.communityInfo).subscribe({
        next: (message) => {
          alert(message);
          this.successMessage = 'Partecipante rimosso con successo';
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
        }
      });
    }
  }

  deleteCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.deleteCommunity(communityId).subscribe({
        next: (message) => {
          alert(message);
          this.successMessage = 'Community eliminata con successo';
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
        }
      });
    }
  }

  loadMyWishList() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.wishService.viewMyWishlist(communityId).subscribe({
        next: (data) => {
          this.myWishList = data;
          console.log(this.myWishList)
        },
        error: (err) => {
          this.errorMessage = err.error || 'Errore nel caricamento della wishlist.';
        }
      })
    }
  }

  addWish() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      if (!this.newWish.name || !this.newWish.category) {
        alert('Inserisci le informazioni.');
        return;
      }
      this.wishService.addWish(communityId, this.newWish).subscribe({
        next: (message) => {
          alert(message);
          this.newWish = {
            name: '',
            category: ''
          };
          this.createErrorMessage = '';
          setTimeout(() => {
            this.createSuccessMessage = '';
          }, 3000);
        },
        error: err => {
          this.createErrorMessage = err.error || 'Errore durante la creazione del desiderio';
        }
      });
    }
  }

  deleteWish(wishId: number) {
    this.wishService.deleteWish(wishId).subscribe({
      next: (message) => {
        alert(message);
        this.successMessage = 'Community eliminata con successo';
      },
      error: (err) => {
        alert(err.error || 'Errore durante l\'eliminazione del desiderio')
      }
    })
  }


  openProfileModal() {
    this.isProfileModalOpen = true;
  }

  closeProfileModal() {
    this.isProfileModalOpen = false;
  }

  openSettingsModal() {
    this.isSettingsModalOpen = true;
  }

  closeSettingsModal() {
    this.isSettingsModalOpen = false;
  }

  openWishListModal() {
    this.isWishListModalOpen = true;
  }

  closeWishListModal() {
    this.isWishListModalOpen = false;
  }

  openNewWishForm() {
    this.isNewWishFormOpen = true;
  }

  closeNewWishForm() {
    this.isNewWishFormOpen = false;
  }

  logout() {
    this.authService.logout();
  }

  goBack() {
    this.communityService.goBack();
  }

  protected readonly Role = Role;
}
