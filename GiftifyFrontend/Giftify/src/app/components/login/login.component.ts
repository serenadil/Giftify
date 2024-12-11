import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {LoginRequest} from '../../../model/Auth/LoginRequest';
import {HttpErrorResponse} from '@angular/common/http'


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: false,
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {
  }

  onLogin(): void {
    this.errorMessage = null;
    const loginRequest = new LoginRequest(this.email, this.password);
    this.authService.login(loginRequest).subscribe({
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

