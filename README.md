# clear-solutions
This project is a test task developed for Clear Solutions. The assignment involves the creation of a RESTful API based on a Spring Boot web application, focusing on the "Users" resource.

## Requirements
The API should be responsible for managing the "Users" resource. The following are the specific requirements for this implementation:
### Resource Fields:

1.1 Email (required): The API should validate the email field against the email pattern.

1.2 First Name (required): No additional validation is specified.

1.3 Last Name (required): No additional validation is specified.

1.4 Birth Date (required): The value must be earlier than the current date.

1.5 Address (optional): No specific validation mentioned.

1.6 Phone Number (optional): No specific validation mentioned.

### Functionality
2.1. Create User: The API should allow the registration of users who are more than 18 years old. The age limit of 18 should be configurable and sourced from a properties file.

2.2. Update One/Some User Fields: The API should provide the functionality to update one or more fields of a user.

2.3. Update All User Fields: The API should allow for the complete update of user data.

2.4. Delete User: The API should support deliting user records.

2.5. Search for Users by Birth Date Range: The API should enable searching for users based on a birth date range. It should validate that the "From" date is less than the "To" date and return a list of user objects that fall within the specified range.

## Test Coverage
The project includes comprehensive testing to ensure its functionality and reliability. The test coverage for the project is shown in the image below:
![image](https://github.com/VovaMitsura/clear-solutions/assets/95585344/b1c24d87-3333-4b97-b56e-fbf7acd77531)

### How to Run manually

This application is packaged as a jar that has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary.</br> You run it using the ```java -jar``` command.

* Clone this repository
* Make sure you are using JDK 1.17 and Maven 3.x
* Make sure you are using MySQL 8.0
* Create a MySQL database
``` 
create database "your_database_name"
```
*  Change MySQL username, password, and datasource as per your installation
    - open `src/main/resources/application.properties`
    - change `spring.datasource.username`, `spring.datasource.password`, and `spring.datasource.url` as per your MySQL installation

