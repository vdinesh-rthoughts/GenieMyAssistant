Feature: Sms Feature

  Scenario: Send sms
    Given I launch app
    And I click SEND MESSAGE button
    And I see Send Message screen
    And I enter message recipient number 8124550344
    And I enter sms message Hi Genie
    When I click SEND button


  Scenario Outline: Check contact number
    Given I launch app
    And I click CHECK CONTACT button
    And I enter contact number <number>
    When I click GET CONTACT DISPLAY NAME button
    Then I see contact name <name>

    Examples:
      | number     | name |
      | 9787342474 | Dad  |