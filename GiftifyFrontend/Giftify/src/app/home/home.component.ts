import { Component, OnInit } from '@angular/core';
import { HomeService } from '../services/home.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: false
})
export class HomeComponent implements OnInit {
  accountInfo: any = null;
  communities: any[] = [];
  joinCode = '';
  joinErrorMessage = '';
  isDropdownOpen = false;
  newCommunity = {
    name: '',
    description: '',
    budget: 0,
    deadline: '',
  };

  constructor(private homeService: HomeService) {}

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
      next: (data) => (this.communities = data),
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
        this.loadCommunities(); // Ricarica l'elenco delle community
      },
      error: (err) =>
        (this.joinErrorMessage =
          err.error || 'Errore durante l’unione alla community'),
    });
  }

  createCommunity() {
    const communityData = {
      communityName: prompt('Inserisci il nome della community:'),
      note: prompt('Inserisci una descrizione per la community:'),
      budget: parseFloat(prompt('Inserisci il budget:') || '0'),
      deadline: prompt('Inserisci una data di scadenza (yyyy-MM-dd):'),
    };

    if (!communityData.communityName) {
      alert('Il nome della community è obbligatorio.');
      return;
    }

    this.homeService.createCommunity(communityData).subscribe({
      next: (message) => {
        alert(message);
        this.loadCommunities();
      },
      error: (err) =>
        alert(err.error || 'Errore durante la creazione della community'),
    });
  }

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  openSettings() {
    alert('Impostazioni non implementate.');
  }

  logout() {
    alert('Logout effettuato.');
  }
}
