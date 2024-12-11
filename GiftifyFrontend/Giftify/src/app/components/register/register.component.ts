import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {RegisterRequest} from '../../../model/Auth/RegisterRequest';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  email: string = '';
  username: string = '';
  password: string = '';
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {
  }

  onRegister(): void {
    this.errorMessage = null;
    const registerRequest = new RegisterRequest(this.email, this.password, this.username);
    this.authService.register(registerRequest).subscribe({
      next: (response) => {
        this.authService.saveTokens(response);
        this.router.navigate(['/home']);
      }, error: (error: HttpErrorResponse) => {
        if (error.error && typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else {
          this.errorMessage = 'Error Code: ' + error.status + '\nMessage: ' + error.message;
        }
      },
    });
  }

}
