describe('empty spec', () => {
  it('can visit hidden page', () => {
    cy.visit('/hidden/cypress');
    cy.title().should('include', 'cypress tests')
  })
})