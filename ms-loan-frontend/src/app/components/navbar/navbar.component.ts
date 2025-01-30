import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { KeycloakService } from '../../utils/keycloak/keycloak.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  constructor(private keycloakService: KeycloakService) {}

  logout() {
    this.keycloakService.logout();
  }

  get fullname() {
    return this.keycloakService.fullName;
  }

  get isManager() {
    return this.keycloakService.isManager;
  }
}
