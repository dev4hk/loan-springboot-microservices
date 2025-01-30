import {
  CanActivateFn,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import { inject } from '@angular/core';
import { KeycloakService } from '../utils/keycloak/keycloak.service';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);

  const expectedRole = route.data['expectedRole'];
  const userRoles =
    keycloakService.keycloak.tokenParsed?.realm_access?.roles || [];

  if (userRoles.includes(expectedRole)) {
    return true;
  } else {
    router.navigate(['/']);
    return false;
  }
};
