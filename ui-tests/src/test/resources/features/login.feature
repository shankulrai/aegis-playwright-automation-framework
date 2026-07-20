Feature: Demo login

  Scenario: Successful hybrid login flow
    Given the demo login page is opened
    And a local health api is available
    When the user logs in with username "demo" and password "demo123"
    And the health api is requested
    Then the status should be "SUCCESS"
    And the api response status should be 200
    And the api response should contain "UP"
