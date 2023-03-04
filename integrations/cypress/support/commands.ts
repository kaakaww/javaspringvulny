// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
const APP_HOST = Cypress.config().baseUrl
const loginPassword = Cypress.env("LOGIN_PASSWORD")
const loginUsername = Cypress.env("LOGIN_USERNAME")
const tokenName = Cypress.env("TOKEN_NAME")
const tokenStaticValue = Cypress.env("TOKEN_VALUE")
Cypress.Commands.add('attemptSearch', (value: string) => {
    const searchButton = '#items'
    const resultsPane = '#results'
    const searchField = '#search'
    cy.get('body').then((body) => {
        if(value && body.find(searchField).length > 0) {
            body.find(searchField).text(value);
        }
        if(body.find(searchButton).length > 0) {
            body.find(searchButton).trigger('click');
            body.find(resultsPane).first().hasClass('alert-success')
        }
    })
})
Cypress.Commands.add('signOut', () => {
    const signoutButton = 'button:contains("Sign Out")'
    const logoutButton = '#logout'
    cy.get('body').then((body) => {
        if(body.find(signoutButton).length > 0) {
            body.find(signoutButton).trigger('click');
        }
        if(body.find(logoutButton).length > 0) {
            body.find(logoutButton).trigger('click');
        }
    })
})
Cypress.Commands.add('formAuth', () => {
    cy.visit(`${APP_HOST}/login`)
    cy.get('#username').type(loginUsername);
    cy.get('#password').type(loginPassword).wait(10);
    cy.get('button:contains("Submit")').click();
})
// -- This is a parent command --
Cypress.Commands.add('jwtAuth', () => {
    cy.visit(`${APP_HOST}/jwt-auth`)
    cy.get('button[data-toggle="modal"]').click()
    cy.get('.modal-dialog').should('be.visible')
    cy.get('#username').clear().type(loginUsername);
    cy.get('#password').clear().type(loginPassword).wait(10);
    cy.get('button#login').click();
    cy.get('#login-message').should('contain.text', '200 OK')
    cy.get('button.btn-secondary').click();
})
// -- This is a parent command --
Cypress.Commands.add('tokenAuth', () => {
    cy.visit(`${APP_HOST}/token-auth`)
    cy.get('#token-name').type(tokenName);
    cy.get('#token-value').type(tokenStaticValue).wait(10);
})
// -- This is a parent command --
Cypress.Commands.add('basicAuth', () => {
    cy.visit(`${APP_HOST}/basic-auth`)
    cy.get('#username').type(loginUsername);
    cy.get('#password').type(loginPassword).wait(10);
})
// -- This is a parent command --
Cypress.Commands.add('formMultiAuth', () => {
    cy.visit(`${APP_HOST}/login-form-multi`);
    cy.get('#username').type(loginUsername);
    cy.get('#password').type(loginPassword).wait(10);
    cy.get('#remember').click();
    cy.get('button:contains("Submit")').click();
    cy.get('button:contains("Sign Out")').should('be.visible');
})
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })