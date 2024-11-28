import {Component, OnInit} from '@angular/core';
import {BackendService} from './services/backend.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Giftify';
  message: string = '';

  constructor(private backendService: BackendService) {}

  ngOnInit(): void {
    // Chiama il backend e assegna il risultato a 'message'
    this.backendService.getHelloMessage().subscribe((response) => {
      this.message = response;
    });
  }

}
