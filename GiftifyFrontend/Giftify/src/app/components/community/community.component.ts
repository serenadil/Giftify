import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CommunityService} from '../../services/community.service';
import {HomeService} from '../../services/home.service';
import {AuthService} from '../../services/auth.service';
import {WishService} from '../../services/wish.service';
import {Role} from '../../../model/Role';
import {ChangeDetectorRef} from '@angular/core';


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
    {key: 'KITCHEN', img: '../../../assets/pics/kitchen-pic.png'},
    {key: 'CARE', img: '../../../assets/pics/care-pic.png'},
    {key: 'GYM', img: '../../../assets/pics/gym-pic.png'},
    {key: 'READING', img: '../../../assets/pics/book-pic.png'},
    {key: 'CINEMA', img: '../../../assets/pics/cinema-pic.png'},
    {key: 'CLOTHES', img: '../../../assets/pics/hat-pic.png'},
    {key: 'IT', img: '../../../assets/pics/laptop-pic.png'},
    {key: 'OTHER', img: '../../../assets/pics/other-pic.png'}
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
    private cdr: ChangeDetectorRef
  ) {
  }

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
  }

  loadAccountInfo() {
    this.communityService.getAccountInfo().subscribe({
      next: (data) => {
        this.accountInfo = data;
        console.log(this.accountInfo);

      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo:', err);
      }
    });
  }


  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {


          this.communityInfo = data;

          this.userCommunityName = this.communityInfo.communityNames?.find((participant: any) =>
            participant.accountEmail === this.accountInfo?.email
          )?.userCommunityName || null;
          this.myWishList = this.communityInfo.wishlists?.find((wishList: any) =>
            wishList.userEmail === this.accountInfo?.email
          ) || null;
          this.userRole = this.accountInfo.communityRoles.find((role: any) =>
            role.community === this.communityInfo?.id
          ).role || null;

          if (this.communityInfo.communityNames) {
            this.filteredParticipants = this.communityInfo.communityNames.filter(
              (participant: any) => participant.userCommunityName !== this.userCommunityName
            );

          }
          if (this.communityInfo.close) {
            const giftAssignment = this.communityInfo.giftAssignments.find((drawn: any) =>
              drawn.giverEmail === this.accountInfo?.email
            );
            if (giftAssignment) {

              const receiver = this.communityInfo.communityNames.find((participant: any) =>
                participant.accountEmail === giftAssignment.receiverEmail
              );
              this.drawnName = receiver ? receiver.userCommunityName : null;


            }
          }
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
    console.log('view');
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.viewDrawnName(communityId).subscribe({
        next: (response) => {
          this.drawnName = response || null;
          console.log('Nome estratto aggiornato:', this.drawnName);
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
          console.error('Errore durante il recupero del nome estratto:', err);
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
          setTimeout(() => {
            this.successCloseMessage = '';
          }, 3000);
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
    setTimeout(() => {
      window.location.reload();
    }, 500);
  }


  removeUserFromCommunity(userToRemove: string | null) {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.removeUserFromCommunity(communityId, userToRemove).subscribe({
        next: () => {
          this.successRemoveUserMessage = 'Partecipante rimosso con successo';
          this.filteredParticipants = this.filteredParticipants.filter(
            (participant: any) => participant.userCommunityName !== userToRemove
          );
          console.log("topi" + this.filteredParticipants.length);

        },
        error: (err) => {
          this.errorRemoveUserMessage = err.error || 'Si è verificato un errore.' + err;
        }
      });
    }

  }

  deleteCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.deleteCommunity(communityId).subscribe({
        next: () => {
          this.successDeleteMessage = 'Community eliminata con successo';

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

      this.communityService.viewUserCommunityName(communityId).subscribe({
        next: (data) => {
          this.userCommunityName = data;
          this.communityService.viewParticipantList(communityId, this.userCommunityName).subscribe({
            next: (data) => {
              this.myWishList = data;

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
      this.communityService.viewParticipantList(communityId, accountCommunityName).subscribe({
        next: (data) => {

          this.userWishList = data || {wishes: []};
          this.openWishListModal();
        },
        error: (err) => {
          console.error('Errore durante il recupero della wishlist:', err);
          this.userWishList = {wishes: []};
          this.openWishListModal();
        }
      });
    } else {
      console.error('ID community non trovato nella route.');
      this.userWishList = {wishes: []};
      this.openWishListModal();
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
      this.wishService.deleteWish(communityId, wishId).subscribe({
        next: () => {
          console.log('scemo')
          this.successMessage = 'Desiderio rimosso con successo';


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
    this.isDeleteUserConfirmPopupVisible = true;
  }

  cancelDeleteUser() {
    this.isDeleteUserConfirmPopupVisible = false;
  }

  confirmDeleteCommunity() {
    this.deleteCommunity();
    this.isDeleteConfirmPopupVisible = false;
    this.goBackReload();
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
    this.cdr.detectChanges();
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

  goBackReload() {

    window.location.href = '/home';

    setTimeout(() => {
      window.location.reload();
    }, 500);
  }

  goBack() {
    this.communityService.goBack();
  }

  protected readonly Role = Role;

  onSettingsLinkClick($event: MouseEvent) {

  }
}
