import {Component, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {CommunityService} from '../../services/community.service';
import {HomeService} from '../../services/home.service';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-community',
  standalone: false,

  templateUrl: './community.component.html',
  styleUrl: './community.component.css'
})
export class CommunityComponent {
  communityInfo: any = null;
  name: string = '';
  description: string = '';
  budget: number = 0;
  deadline: any = null;
  userWishList: any = null;
  participants: any[] = [];
  drawnNameWishList: any = null;
  myWishList: any = null;
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  errorMessage: string = '';
  communityId: number = 0;
  isSuccessPopupVisible: boolean = false;
  accountInfo: any = null;
  communities: any[] = [];

  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService, private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    // this.communityId = +this.route.snapshot.paramMap.get('id');
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
    this.communityService.getGeneralInfo(this.communityId).subscribe({
      next: (data) => (this.communityInfo = data),
      error: (err) =>
        console.error('Errore nel caricamento della community:', err),
    });
  }

  loadMyWishList() {

  }

  viewDrawnName(): void {
    this.communityService.viewDrawnName(this.communityId).subscribe({
      next: (message) => {
        alert(message);
        this.errorMessage = '';
        this.viewDrawnNameList();
      },
      error: (err) => (this.errorMessage = err.error || 'Errore durante la procedura'),
    });
  }

  viewDrawnNameList() {
    this.communityService.viewDrawnNameList(this.communityId).subscribe({
      next: (data) => (this.drawnNameWishList = data),
      error: (err) => (console.error('Errore durante il caricamento: ', err)),
    })
  }

  closeCommunity() {
    this.communityService.closeCommunity(this.communityId).subscribe({
      next: (data) => (this.communityInfo = data),
      error: (err) => (console.error('Errore nella chiusura della community: ', err)),
    })
  }

  loadParticipants() {
    this.communityService.getParticipants(this.communityId).subscribe({
      next: (data) => (this.participants = data),
      error: (err) =>
        console.error('Errore nel caricamento dei partecipanti: ', err),
    })
  }

  loadUserList(): void {
    const selectedUser = this.participants.find(participant => participant.isSelected);
    if (selectedUser) {
      this.communityService.viewParticipantList(this.communityId, selectedUser.id).subscribe(
        (wishList) => {
          this.userWishList = wishList;
        },
        (error) => {
          console.error('Errore nel caricare la wishlist', error);
        }
      );
    }
  }

  onParticipantClick(participantId: number): void {
    this.participants.forEach(participant => participant.isSelected = (participant.id === participantId));
    this.loadUserList();
  }


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
