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
  participants: any[] = [];
  drawnNameWishList: any = null;
  myWishList: any = null;
  isCommunityClosed: boolean = false;
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  isSuccessPopupVisible: boolean = false;
  accountInfo: any = null;
  communities: any[] = [];
  userRole: string | null = null;
  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService, private wishService: WishService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
  }

  loadAccountInfo() {
    this.communityService.getAccountInfo().subscribe({
      next: (data) => {
        console.log('Dati dell\'account ricevuti:', data); // Aggiungi questo log per confermare
        this.accountInfo = data;
        console.log('palla'+this.accountInfo.email);
      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo:', err);
      },
    });
  }


  loadMyWishList() {

  }

  isClosed(): boolean {
    return this.isCommunityClosed;
  }

  viewDrawnName(): void {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      if (!this.isClosed()) {
        this.errorMessage = 'Ops! La community non è stata ancora chiusa.';
      }
      this.communityService.viewDrawnName(communityId).subscribe({
        next: (name) =>
          this.drawnName = name,
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
          console.error(err);
        }
      });
    } else {
      this.errorMessage = 'ID della community non trovato.';
    }
  }

  // viewDrawnNameList() {
  //   this.communityService.viewDrawnNameList(this.communityId).subscribe({
  //     next: (data) => (this.drawnNameWishList = data),
  //     error: (err) => (console.error('Errore durante il caricamento: ', err)),
  //   })
  // }

  // closeCommunity() {
  //   const communityId = this.route.snapshot.paramMap.get('id');
  //   if (communityId) {
  //     if (this.accountInfo.getRoleForCommunity(this.communityInfo)===Role.ADMIN) {
  //       this.communityService.closeCommunity(communityId).subscribe({
  //         next: (message) => {
  //           alert (message);
  //           this.successMessage = 'Community chiusa con successo'
  //         },
  //         error: (err) => {
  //           this.errorMessage = err.error || 'Si è verificato un errore.';
  //         }
  //       });
  //     }
  //   }
  //}

  // closeCommunity() {
  //   const communityId = this.route.snapshot.paramMap.get('id');
  //   if (communityId) {
  //     this.communityService.closeCommunity(communityId).subscribe({
  //       next: (message) => {
  //         alert(message);
  //         this.successMessage = 'Community chiusa con successo';
  //       },
  //       error: (err) => {
  //         if (err.status === 403) {
  //           this.errorMessage = 'Non sei autorizzato a chiudere questa community.';
  //         } else {
  //           this.errorMessage = err.error || 'Si è verificato un errore.';
  //         }
  //       }
  //     });
  //   } else {
  //     this.errorMessage = 'ID della community non trovato.';
  //   }
  // }


  // loadParticipants() {
  //   this.communityService.getParticipants(this.communityId).subscribe({
  //     next: (data) => (this.participants = data),
  //     error: (err) =>
  //       console.error('Errore nel caricamento dei partecipanti: ', err),
  //   })
  // }
  //
  // loadUserList(): void {
  //   const selectedUser = this.participants.find(participant => participant.isSelected);
  //   if (selectedUser) {
  //     this.communityService.viewParticipantList(this.communityId, selectedUser.id).subscribe(
  //       (wishList) => {
  //         this.userWishList = wishList;
  //       },
  //       (error) => {
  //         console.error('Errore nel caricare la wishlist', error);
  //       }
  //     );
  //   }
  // }
  //
  // onParticipantClick(participantId: number): void {
  //   this.participants.forEach(participant => participant.isSelected = (participant.id === participantId));
  //   this.loadUserList();
  // }

  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {
          this.communityInfo = data;
          this.errorMessage = null;
          // Ora carica anche il ruolo dell'utente per questa community
          this.communityService.getRoleForCommunity(communityId).subscribe({
            next: (roleData) => {
              this.userRole = roleData;  // Salva il ruolo dell'utente
            },
            error: (err) => {
              this.errorMessage = 'Errore nel caricare il ruolo dell\'utente.';
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
    if (communityId ) {
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

  logout() {
    this.authService.logout();
  }

  protected readonly Role = Role;
}
