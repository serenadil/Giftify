import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CommunityService} from '../../services/community.service';
import {HomeService} from '../../services/home.service';
import {AuthService} from '../../services/auth.service';
import {WishService} from '../../services/wish.service';
import {defaultEquals} from '@angular/core/primitives/signals';

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
  isCommunityClosed : boolean = false;
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  isSuccessPopupVisible: boolean = false;
  accountInfo: any = null;
  communities: any[] = [];

  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService, private wishService : WishService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
  }

  loadAccountInfo() {
    this.homeService.getAccountInfo().subscribe({
      next: (data) => (this.accountInfo = data),
      error: (err) => console.error('Errore nel caricamento del profilo:', err),
    });
  }

  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {
          this.communityInfo = data;
          this.errorMessage = null;
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

  loadMyWishList() {

  }

  isClosed() : boolean {
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
  //     this.communityService.closeCommunity(communityId).subscribe({
  //       next: (message) => {
  //         this.successMessage = message;
  //         this.isCommunityClosed = true;
  //         this.successMessage = 'Chiusa con successo';
  //         this.router.navigate([`/community/${communityId}`]);
  //       },
  //       error: (err) => {
  //         switch (err.status) {
  //           case 403:
  //             this.errorMessage = 'Non sei autorizzato a chiudere questa community. Solo l\'amministratore può farlo.';
  //             break;
  //           default:
  //             this.errorMessage = err.error || 'Si è verificato un errore.';
  //             console.error(err);
  //         }
  //       }
  //     });
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


openProfileModal()
{
  this.isProfileModalOpen = true;
}

closeProfileModal()
{
  this.isProfileModalOpen = false;
}

openSettingsModal()
{
  this.isSettingsModalOpen = true;
}

closeSettingsModal()
{
  this.isSettingsModalOpen = false;
}
logout()
{
  this.authService.logout();
}

}
