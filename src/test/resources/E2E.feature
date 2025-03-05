@E2E
  Feature: Bookstore API End-to-End Testing
    As a user of BookStore API
    I want to perform a complete workflow from user creation to deletion
    So that I can validate the entire system functionality

    Scenario: End-to-End workflow for user and book management
      # Step 1: User Creation
      Given a persona with username "user001" and password "Test@1111"
      When the persona is registered
      Then the persona receives a userId and token
      And the registration response status is 201

      # Step 2: Order a Book
      When the persona orders a book with ISBN "9781449325862"
      Then the book is successfully added to the collection
      And the order response status is 201
      And the collection has 1 book

      # Step 3: Add an Extra Book to the Existing Order
      When the persona orders a book with ISBN "9781449331818"
      Then the book is successfully added to the collection
      And the order response status is 201
      And the collection has 2 book

      # Step 4: Delete the User and Validate
      When the persona is deleted
      Then the deletion is successful with status 204
      And the persona's collection is no longer accessible