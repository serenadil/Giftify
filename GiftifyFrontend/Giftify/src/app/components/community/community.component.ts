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
  drawnNameWishList: any = null;
  myWishList: any = null;
  participants: any = [];
  isCommunityClosed: boolean = false;
  isProfileModalOpen = false;
  isSettingsModalOpen = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  isSuccessPopupVisible: boolean = false;
  accountInfo: any = null;
  userRole: string | null = null;

  constructor(private communityService: CommunityService, private homeService: HomeService, private authService: AuthService, private wishService: WishService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.loadAccountInfo();
    this.loadCommunity();
    this.loadMyWishList()
    this.loadUserList()
  }

  loadAccountInfo() {
    this.communityService.getAccountInfo().subscribe({
      next: (data) => {
        console.log('Dati dell\'account ricevuti:', data); // Aggiungi questo log per confermare
        this.accountInfo = data;
        console.log('palla' + this.accountInfo.email);
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

  loadUserList() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.viewParticipantList(communityId, this.participants.getAccountCommunityNameByAccount(this.accountInfo)).subscribe({
        next: (data) => {
          this.userWishList = data;
        },
        error: (err) => {
          this.errorMessage = err.error || 'Si è verificato un errore.';
        }
      })
    }
  }

  loadCommunity() {
    const communityId = this.route.snapshot.paramMap.get('id');
    if (communityId) {
      this.communityService.getGeneralInfo(communityId).subscribe({
        next: (data) => {
          this.communityInfo = data;
          this.errorMessage = null;
          this.communityService.getRoleForCommunity(communityId).subscribe({
            next: (roleData) => {
              this.userRole = roleData;  // Salva il ruolo dell'utente
            },
            error: (err) => {
              this.errorMessage = err.error ||'Errore nel caricare il ruolo dell\'utente.';
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
    }
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

  deleteCommunity(){
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

  goBack() {
    this.communityService.goBack();
  }

  protected readonly Role = Role;
}
