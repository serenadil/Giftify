import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest } from '../../model/Auth/LoginRequest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: false,
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    const loginRequest = new LoginRequest(this.email, this.password);
    this.authService.login(loginRequest).subscribe({
      next: (response) => {
        this.authService.saveTokens(response);
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.errorMessage = 'Credenziali non valide';
      },
    });
  }
}

