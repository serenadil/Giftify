import { Component, OnInit } from '@angular/core';
import { HomeService } from '../../services/home.service';
import { AuthService } from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  standalone: false,
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],

})
export class HomeComponent implements OnInit {
  accountInfo: any = null;
  communities: any[] = [];
  joinCode = '';
  userCommunityName = '';
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  joinErrorMessage = '';
  createErrorMessage = '';
  createSuccessMessage = '';
  isSuccessPopupVisible: boolean = false;



  newCommunity = {
    communityName: '',
    communityNote: '',
    budget: null,
    deadline: null,
    userCommunityName: ''

  };
  joinSuccessMessage: string ='';
  isErrorAccountPopupVisible: boolean = false;


  constructor(private homeService: HomeService, private authService: AuthService, private router : Router) {}

  ngOnInit(): void {
    this.loadAccountInfo();
    this.loadCommunities();
  }


  loadAccountInfo() {
    this.homeService.getAccountInfo().subscribe({
      next: (data) => {
        this.accountInfo = data;

      },
      error: (err) => {
        console.error('Errore nel caricamento del profilo:', err);
        this.isErrorAccountPopupVisible = true;
      }
    });
  }


  loadCommunities() {
    this.homeService.getUserCommunities().subscribe({
      next: (data) => {
        this.communities = data;
      },
      error: (err) =>
        console.error('Errore nel caricamento delle community:', err),
    });
  }



  joinCommunity() {
    if (!this.joinCode.trim()) {
      this.joinErrorMessage = 'Inserisci un codice valido';
      this.joinSuccessMessage = '';
      return;
    }

    this.homeService.joinCommunity(this.joinCode, this.userCommunityName).subscribe({
      next: (message) => {
        this.joinSuccessMessage = message;
        this.joinErrorMessage = '';
        this.loadCommunities();
      },
      error: (err) => {
        this.joinErrorMessage = err.error || 'Errore durante l’unione alla community';
        this.joinSuccessMessage = '';
      },
    });
  }



  createCommunity() {
    if (!this.newCommunity.communityName) {
      this.createErrorMessage = 'Il nome della community è obbligatorio.';
      return;
    }
    this.homeService.createCommunity(this.newCommunity).subscribe({
      next: (message) => {
        this.createSuccessMessage = message;
        this.loadCommunities();
        this.newCommunity = {
          communityName: '',
          communityNote: '',
          budget: null,
          deadline: null,
          userCommunityName: ''
        };

        this.createErrorMessage = '';

        setTimeout(() => {
          this.createSuccessMessage = '';
        }, 3000);
      },
      error: (err) =>
        (this.createErrorMessage =
          err.error || 'Errore durante la creazione della community'),
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


  logout() {
    this.authService.logout();
  }


  onSettingsLinkClick($event: MouseEvent) {

  }


  closeErrorPopup() {
    this.isErrorAccountPopupVisible = false;
    this.logout()
    this.router.navigate(['/login']);
  }
}
