import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CommunityService} from '../../services/community.service';
import {HomeService} from '../../services/home.service';
import {AuthService} from '../../services/auth.service';
import {WishService} from '../../services/wish.service';
import {Role} from '../../../model/Role';
import {CacheService} from '../../services/cacheService';

@Component({
  selector: 'app-community',
  standalone: false,
  templateUrl: './community.component.html',
  styleUrls: ['./community.component.css']
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
  successCloseMessage: string | null = null;
  errorCloseMessage: string | null = null;
  successRemoveUserMessage: string | null = null;
  errorRemoveUserMessage: string | null = null;
  successDeleteMessage: string | null = null;
  errordeleteMessage: string | null = null;
  successRemovedMessage: string | null = null;
  errorRemovedMessage: string | null = null;
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
  isConfirmationPopupOpen: boolean = false;
  isDeleteConfirmPopupVisible: boolean = false;
  isDeleteUserConfirmPopupVisible: boolean = false;
  userToDelete: string | null = null;

  constructor(
    private communityService: CommunityService,
    private homeService: HomeService,
    private authService: AuthService,
    private wishService: WishService,
    private route: ActivatedRoute,
    private router: Router,
    private cacheService: CacheService
  ) {}

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
    this.loadMyWishList();
  }

  loadAccountInfo() {
    const cachedAccountInfo = this.cacheService.get('accountInfo');
    if (cachedAccountInfo) {
      this.accountInfo = cachedAccountInfo;
    } else {
      this.communityService.getAccountInfo().subscribe({
        next: (data) => {
          this.accountInfo = data;
          this.cacheService.set('accountInfo', data);
        },
        error: (err) => {
          console.error('Errore nel caricamento del profilo:', err);
        }
      });
    }
  }

  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      const cachedCommunityInfo = this.cacheService.get(`communityInfo-${communityId}`);
      if (cachedCommunityInfo) {
        this.communityInfo = cachedCommunityInfo;
        this.filteredParticipants = this.communityInfo.communityNames.filter(
          (participant: any) => participant.userCommunityName !== this.userCommunityName
        );
        if (this.communityInfo.close) {
          this.viewDrawnName();
        }
        return; // Usa la cache e esci dalla funzione
      }
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {
          this.communityInfo = data;
          this.cacheService.set(`communityInfo-${communityId}`, data);
          if (this.communityInfo.communityNames) {
            this.filteredParticipants = this.communityInfo.communityNames.filter(
              (participant: any) => participant.userCommunityName !== this.userCommunityName
            );
          }
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
    this.loadMyWishList();
  }

  getCategoryImage(category: string): string | null {
    const foundCategory = this.categories.find(cat => cat.key === category);
    return foundCategory ? foundCategory.img : null;
  }

  viewDrawnName(): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      const cachedDrawnName = this.cacheService.get(`drawnName-${communityId}`);
      if (cachedDrawnName) {
        this.drawnName = cachedDrawnName;
        return;
      }
      this.communityService.viewDrawnName(communityId).subscribe({
        next: (name) => {
          this.drawnName = name;
          this.cacheService.set(`drawnName-${communityId}`, name);
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
          this.successCloseMessage = 'Community chiusa con successo';

          this.router.navigate([this.router.url]).then(() => {
            setTimeout(() => {
              this.successCloseMessage = '';
            }, 3000);
          });
        },
        error: (err) => {
          this.errorCloseMessage = err.error || 'Si è verificato un errore.';
          setTimeout(() => {
            this.errorCloseMessage = '';
          }, 3000);
        }
      });
    } else {
      this.errorCloseMessage = 'Errore nel trovare l\'ID della community.';
      setTimeout(() => {
        this.errorCloseMessage = '';
      }, 3000);
    }
  }


  removeUserFromCommunity(userToRemove: string | null) {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.removeUserFromCommunity(communityId, userToRemove).subscribe({
        next: () => {
          this.successRemoveUserMessage = 'Partecipante rimosso con successo';
          this.cacheService.clear(`communityInfo-${communityId}`);
        },
        error: (err) => {
          this.errorRemoveUserMessage = err.error || 'Si è verificato un errore.' + err;
        }
      });
    }
    this.loadCommunity();
  }

  deleteCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.deleteCommunity(communityId).subscribe({
        next: () => {
          this.successDeleteMessage = 'Community eliminata con successo';
          this.cacheService.clear(`communityInfo-${communityId}`);
        },
        error: (err) => {
          this.errordeleteMessage = err.error || 'Si è verificato un errore.';
        }
      });
    }
    this.isDeleteConfirmPopupVisible = true;
  }

  loadMyWishList() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      const cachedMyWishList = this.cacheService.get(`myWishList-${communityId}`);
      if (cachedMyWishList) {
        this.myWishList = cachedMyWishList;
        return;
      }
      this.communityService.viewUserCommunityName(communityId).subscribe({
        next: (data) => {
          this.userCommunityName = data;
          this.communityService.viewParticipantList(communityId, this.userCommunityName).subscribe({
            next: (data) => {
              this.myWishList = data;
              this.cacheService.set(`myWishList-${communityId}`, data);
            },
            error: (err) => {
              this.errorMessage = err.error || 'Errore nel caricamento del nome ';
            }
          });
        },
        error: (err) => {
          this.errorMessage = err.error || 'Errore nel caricamento del nome ';
        }
      });
    }
  }

  viewParticipantWishlist(accountCommunityName: string): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      const cachedUserWishList = this.cacheService.get(`userWishList-${communityId}-${accountCommunityName}`);
      if (cachedUserWishList) {
        this.userWishList = cachedUserWishList;
        this.openWishListModal();
        return;
      }
      this.communityService.viewParticipantList(communityId, accountCommunityName).subscribe({
        next: (data) => {
          this.userWishList = data;
          this.cacheService.set(`userWishList-${communityId}-${accountCommunityName}`, data);
          this.openWishListModal();
        },
        error: (err) => {
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
          this.cacheService.clear(`myWishList-${communityId}`);
          this.loadMyWishList();
        },
        error: (err) => {
          this.createErrorMessage = err.error || 'Si è verificato un errore durante l\'aggiunta del desiderio.';
        }
      });
    } else {
      this.createErrorMessage = 'Errore nel recupero dell\'ID della community';
    }
  }


  deleteWish(wishId: number): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.wishService.deleteWish(wishId).subscribe({
        next: () => {

          this.successMessage = 'Desiderio rimosso con successo';

          this.cacheService.clear(`myWishList-${communityId}`);
          this.loadMyWishList();
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore durante la rimozione del desiderio.';
          setTimeout(() => {
            this.errorMessage = '';
          }, 3000);
        }
      });
    } else {
      this.errorMessage = 'ID della community non trovato.';
      setTimeout(() => {
        this.errorMessage = '';
      }, 3000);
    }
  }
  confirmDeleteUser() {
    if (this.userToDelete) {
      this.removeUserFromCommunity(this.userToDelete);
      this.isDeleteUserConfirmPopupVisible = false;

    }
  }
  deleteUser(userCommunityName: string) {
    this.userToDelete = userCommunityName;
    this.isDeleteUserConfirmPopupVisible = true; }

  cancelDeleteUser() {
    this.isDeleteUserConfirmPopupVisible = false;
  }

  confirmDeleteCommunity() {
    this.deleteCommunity();
    this.isDeleteConfirmPopupVisible = false;
    this.goBack();
  }

  openDeleteModal() {
    this.isDeleteConfirmPopupVisible = true;
  }


  cancelDelete() {
    this.isDeleteConfirmPopupVisible = false;
  }

  isUserInCommunity(userCommunityName: string): boolean {
    return this.filteredParticipants.some(
      (participant) => participant.userCommunityName === userCommunityName
    );
  }

  get isEvenNumberOfParticipants() {
    return (this.filteredParticipants.length + 1) % 2 === 0;
  }

  openConfirmationPopup() {
    this.isConfirmationPopupOpen = true;
  }


  closeConfirmationPopup() {
    this.isConfirmationPopupOpen = false;
  }


  confirmCloseCommunity() {
    this.closeCommunity();
    this.isConfirmationPopupOpen = false;

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

  onSettingsLinkClick($event: MouseEvent) {

  }
}
