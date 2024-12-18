import { Component, OnInit } from '@angular/core';
import { HomeService } from '../../services/home.service';
import { AuthService } from '../../services/auth.service';
import {FormsModule} from '@angular/forms';

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
  joinErrorMessage = '';
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  successMessage: string = '';
  isSuccessPopupVisible: boolean = false;


  newCommunity = {
    communityName: '',
    communityNote: '',
    budget: null,
    deadline: null,
  };


  constructor(private homeService: HomeService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadAccountInfo();
    this.loadCommunities();
  }


  loadAccountInfo() {
    this.homeService.getAccountInfo().subscribe({
      next: (data) => (this.accountInfo = data),
      error: (err) => console.error('Errore nel caricamento del profilo:', err),
    });
  }


  loadCommunities() {
    this.homeService.getUserCommunities().subscribe({
      next: (data) => {
        console.log('Community Data:', data);
        this.communities = data;
      },
      error: (err) =>
        console.error('Errore nel caricamento delle community:', err),
    });
  }



  joinCommunity() {
    if (!this.joinCode.trim()) {
      this.joinErrorMessage = 'Inserisci un codice valido';
      return;
    }
    this.homeService.joinCommunity(this.joinCode).subscribe({
      next: (message) => {
        alert(message);
        this.joinErrorMessage = '';
        this.loadCommunities();
      },
      error: (err) =>
        (this.joinErrorMessage =
          err.error || 'Errore durante l’unione alla community'),
    });
  }


  createCommunity() {
    if (!this.newCommunity.communityName) {
      alert('Il nome della community è obbligatorio.');
      return;
    }
    this.homeService.createCommunity(this.newCommunity).subscribe({
      next: (message) => {
        alert(message);
        this.loadCommunities();
        this.newCommunity = {
          communityName: '',
          communityNote: '',
          budget: null,
          deadline: null,
        };
      },
      error: (err) =>
        alert(err.error || 'Errore durante la creazione della community'),
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

  showSuccessPopup() {
    this.isSuccessPopupVisible = true;
    setTimeout(() => {
      this.isSuccessPopupVisible = false; // Nasconde automaticamente il popup dopo 3 secondi
    }, 3000);
  }
}
