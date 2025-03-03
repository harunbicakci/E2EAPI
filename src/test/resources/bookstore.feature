Feature: Bookstore API Testing

    Scenario: Create a new user and generate token
        Given a persona with username "testuser" and password "Test@111"
        When the persona is registered via the API
        Then the persona receives a userId and token

    Scenario Outline: Retrieve book details by ISBN
        Given a registered persona with token
        When the persona requests book details for ISBN "<isbn>"
        Then the book title should be "<title>" and author should be "<author>"

        Examples:
        |isbn               |title                          |author                 |
        | 9781449325862     | Git Pocket Guide              | Richard E. Silverman  |
        | 9781449331818      | Learning JavaScript Design   | Ethan Brown           |