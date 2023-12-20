Technical summary of Drink dispenser application
1.	Application has been developed with Java 11 and spring boot framework and maven as build tool.
2.	Developed with layered architecture and covered with respective test cases.
3.	Junit and Mockito used for writing testcases.
4.	Java 8 features like stream, lambda functions, optional have been used.
5.	Configured drinks in Json file for transactions – advantage being changes can go to configuration rather than redeployment.
6.	OOPS concepts used,<br /> 
  a.	Encapsulation for data binding. <br />
  b.	Inheritance for dispenser handling.   
7.	Solid principles used,<br />
  a.	SRP - Handling all individual operations in separate class/method.<br />
  b.	ISP - Uses interface for defining dispense the product, interface can be extended for other product dispense.<br />
  c.	DIP - Used allArgsConstructor to inject the dependencies.
8.	API Endpoints - followed resourced based rest design principles <br />
  a.	Insert coins – POST http://localhost:8080/api/order?coin=5_CENTS <br />
  b.	Cancel order – GET http://localhost:8080/api/order <br />
  c.	Dispense Order – GET http://localhost:8080/api/drink?drinkCode=WH&noOfItems=2 <br />
  d.	Delete Product – DELETE - http://localhost:8080/api/product/{drinkCode}<br />
  e.	Add and Update Drink – POST and PUT http://localhost:8080/api/product <br />
      Request body -  {"drinkCode" : "TE", "drinkName" : "TEST", "productPrice" : 1.0,"maxLimit" : 200, "availableQuantity" : 200 }  	


