declare namespace Cypress {
  interface Chainable {
    signOut(): void
    formAuth(): void;
    jwtAuth(): void;
    tokenAuth(): void;
    basicAuth(): void;
    formMultiAuth(): void;
    attemptSearch(value: string): void;
  }
}
