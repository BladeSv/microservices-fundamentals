Feature: the version can be retrieved

  Scenario: clint make call to GET mp3 file by id
    When the client calls /resources/1
    Then the client receives status code of 200
    And the client receives mp3 file with Metallica name