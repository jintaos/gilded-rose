### Summary

Prepare a little summary of your application [here](SUMMARY.md).

- [X] Describe and explain your surge pricing design.
  - 1: When an Item is viewed (via the By name or By list of Available items API , not the Inventory API), the item name and a timestamp will be saved to table Item_viewed;
  - 2: When an Item is viewed (via the By name or By list of Available items API , not the Inventory API), increase the price of each item by 10% if it has been viewed more than 10 times in an hour.

- [X] Describe your API endpoints design and model. Include one example of a request and response for each API call.
  - Please find the information form the  swagger-UI URL:  http://localhost:8088/api/gilded-rose/swagger-ui/index.html
    The Sagger Doc includes the example of a request and response for each API call, and more

- [X] Explain the authentication mechanism you chose, and why.
  - Okta’s OAuth 2.0 token verification is selected here
  - Why Okta? Okta is an identity provider that makes it easy to add authentication and authorization into apps. It’s populated and relative easy to implement authentication.After integrating Okta, the API will require the user to pass in an OAuth 2.0 access token. This token will be checked by Okta for validity and authenticity.
  - Sorry, I don't have one or two more extra days to implement it fully
    - Included code (but commented):
      - okta-spring dependency in the pom.xml file
      - okta OAuth config Adapter in the GildedRoseApplication file
      - okta config in the application.yml file
    - Code need to be enhanced in the inventoryController:
      - The security section of the @Operation to add the required permissions and roles for endpoint getInventory and purchase
      - The @RequestHeader's required property is set to false for the authorization parameter for endpoint getInventory and purchase, it should be ture for a real application
    - the Inventory and Purchase endpoint need token, but it's not been verified now.

- [X] Explain your testing, what you covered, and why.
  - Unit Tests for controllers and services are included but not repositories
  - Integration tests are included for most user cases
  - the code coverage: 100% for methods; more than 92% for lines [here](CodeCoverage.PNG).
  - I paid more attention to the unit tests of services.  
  - Since there is no special customized method is added to the repositories, I think I don't need to create test for the spring Data JPA
  

- [X] Describe the features you would include if this was an actual business application. 
  - Thinking like a traditional supply chain system, to implement an efficient order system to keep an optimized inventory leave, when/what item(s)/how many to order...
  - We implemented some simple surge pricing model to increase the price for those hot items, need a discount pricing model to decrease the price for the item is not that populated too.