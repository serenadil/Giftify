import { Component } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {RegisterRequest} from '../../model/Auth/RegisterRequest';

@Component({
  selector: 'app-register',
  standalone: false,

  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  email: string = '';
  password: string = '';
  username: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister(): void {
    const registerRequest = new RegisterRequest(this.email, this.password, this.username);
    this.authService.register(registerRequest).subscribe({
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
