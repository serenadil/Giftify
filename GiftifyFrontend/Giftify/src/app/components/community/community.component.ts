import {Component, OnInit} from '@angular/core';
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
  budget: any = null;
  deadline: any = null;
  userWishList: any = null;
  participants: any[] = [];
  communityId: number = 0;
  drawnNameWishList: any[] = [];
  myWishList: any = null;

  errorMessage: string = '';

  isDropdownOpen = false;
  accountInfo: any = null;
  communities: any[] = [];

  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService) {
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
    this.communityService.getGeneralInfo(this.communityId).subscribe({
      next: (data) => (this.communityInfo = data),
      error: (err) =>
        console.error('Errore nel caricamento della community:', err),
    });
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

  loadCommunities() {
    this.homeService.getUserCommunities().subscribe({
      next: (data) => (this.communities = data),
      error: (err) =>
        console.error('Errore nel caricamento delle community:', err),
    });
  }

  loadParticipants(){
    this.communityService.getParticipants(this.communityId).subscribe({
      next: (data) => (this.participants = data),
      error: (err) =>
        console.error('Errore nel caricamento dei partecipanti: ', err),
    })
  }


  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  logout() {
    this.authService.logout();
  }




}
