Feature: Sample scenarios

  @MSITest
  Scenario: Amazon Account creation with scenario
    Given user launches Amazon webapp
    And User 'fills' The home page 'acceptCookiesButton:click##accountListHyperlink:click' values
    And validate the Page Title as 'Amazon Sign In'
    And User 'fills' The signin page 'createAccountButton:click' values
    And validate the Page Title as 'Amazon Registration'
    And User 'fills' the Account Creation Page with 'yourNameTextField:SyedTest##emailAddressTextField:test@test.com##passwordTextField:S3curity##confirmPasswordTextField:S3curity' values
    And User validates the following field and Values in the 'amazonregisterpage' screen
      |passwordHintLabel:  Passwords must be at least 6 characters.|
    And User 'fills' the Account Creation Page with 'continueButton:click' values