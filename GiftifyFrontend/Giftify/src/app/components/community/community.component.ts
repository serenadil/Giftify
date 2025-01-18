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
  userCommunityName: string | null = null;
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
  categories = [
    { key: 'KITCHEN', img: '../../../assets/pics/kitchen-pic.png' },
    { key: 'CARE', img: '../../../assets/pics/care-pic.png' },
    { key: 'GYM', img: '../../../assets/pics/gym-pic.png' },
    { key: 'READING', img: '../../../assets/pics/book-pic.png' },
    { key: 'CINEMA', img: '../../../assets/pics/cinema-pic.png' },
    { key: 'CLOTHES', img: '../../../assets/pics/hat-pic.png' },
    { key: 'IT', img: '../../../assets/pics/laptop-pic.png' },
    { key: 'OTHER', img: '../../../assets/pics/other-pic.png' }
  ];
  filteredParticipants: any[] = [];
  createSuccessMessage: string | null = null;
  createErrorMessage: string | null = null;


  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService, private wishService: WishService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
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
  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {
          this.communityInfo = data;
          this.errorMessage = null;

          if (this.communityInfo.communityNames) {
            this.filteredParticipants = this.communityInfo.communityNames.filter(
              (participant: any) => participant.userCommunityName !== this.userCommunityName
            );
          }
          console.log(this.filteredParticipants);

          if (this.communityInfo.close) {
            this.viewDrawnName();
          }
          this.communityService.getRoleForCommunity(communityId).subscribe({
            next: (roleData) => {
              this.userRole = roleData;
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

  getCategoryImage(category: string): string | null {
    const foundCategory = this.categories.find(cat => cat.key === category);
    return foundCategory ? foundCategory.img : null;
  }


  viewDrawnName(): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.viewDrawnName(communityId).subscribe({
        next: (name) =>  {
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
    }this.loadMyWishList();
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
      this.communityService.viewUserCommunityName(communityId).subscribe({
        next: (data) => {
          this.userCommunityName = data;
          console.log(this.userCommunityName)
          this.communityService.viewParticipantList(communityId, this.userCommunityName).subscribe({
            next: (data) => {
              this.myWishList = data;
              console.log(this.myWishList)
            },
            error: (err) => {
              this.errorMessage = err.error || 'Errore nel caricamento del nome ';
            }
          })
        },
        error: (err) => {
          this.errorMessage = err.error || 'Errore nel caricamento del nome ';
        }
      })
    }
  }

  viewParticipantWishlist(accountCommunityName: string): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.viewParticipantList(communityId, accountCommunityName).subscribe({
        next: (data) => {
          console.log('Dati ricevuti dalla wishlist del partecipante:', data);
          this.userWishList = data; // Assicurati che "data" sia un array valido
          this.openWishListModal();
        },
        error: (err) => {
          console.error('Errore durante il recupero della wishlist del partecipante:', err);
          this.errorMessage = err.error || 'Errore nel recupero della wishlist del partecipante.';
        }
      });
    } else {
      console.error('Community ID mancante nella route!');
    }
  }

  addWish() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      if (!this.newWish.name || !this.newWish.category) {
        this.createErrorMessage = 'Inserisci tutte le informazioni richieste.';
        return;
      }
      this.wishService.addWish(communityId, this.newWish).subscribe({
        next: (message) => {
          this.createSuccessMessage = message;
          this.newWish = {
            name: '',
            category: ''
          };
          this.createErrorMessage = '';
          setTimeout(() => {
            this.createSuccessMessage = '';
          }, 3000);
        },
        error: (err) => {
          this.createErrorMessage = err.error || 'Errore durante la creazione del desiderio.';
        }
      });
      this.loadMyWishList();
    }
  }

  deleteWish(wishId: number) {
    this.wishService.deleteWish(wishId).subscribe({
      next: (message) => {
        this.successMessage = 'Desiderio eliminato con successo';
        this.errorMessage = '';
        this.loadMyWishList();
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => {
        this.errorMessage = err.error || 'Errore durante l\'eliminazione del desiderio';
        this.successMessage = '';
        setTimeout(() => {
          this.errorMessage = '';
        }, 3000);
      }
    });
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
