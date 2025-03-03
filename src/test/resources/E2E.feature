@E2E
  Feature: Bookstore API End-to-End Testing
    As a user of BookStore API
    I want to perform a couple workflow from user creation to deletion
    So that I can validate the entore system functionality

    Scenario: End-to-End workflow for user and book management
      # Step 1: User Creation
      Given a persona with username "e2e_user" and password "Test@111"
      When the persona is registered via the API
      Then the persona receives a userId and token
      And the response status code is 201

      # Step 2: Order a Book
      When the persona orders a book with ISBN "9781449325862"
      Then the book is added to the persona's collection
      And the response status code is 201
      And the collection has 1 book

      # Step 3: Add an Extra Book to the Existing Order
      When the persona orders a book with ISBN "9781449331818"
      Then the book is added to the persona's collection
      And the response status code is 201
      And the collection has 2 books

      # Step 4: Delete the User and Validate
      When the persona is dleted via the API
      Then the deletion is successful with status code 204
      And the persona's collection is empty when queried