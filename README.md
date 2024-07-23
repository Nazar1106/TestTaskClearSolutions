Java practical test assignment

The task has two parts:
1. Using the resources listed below learn what is RESTful API and what are the best practices to implement it.
2. According to the requirements implement the RESTful API based on the web Spring Boot application: controller, responsible for the resource named Users. 

Resources:
RESTful API Design. Best Practices in a Nutshell. 
Error Handling for REST with Spring | Baeldung 
Testing in Spring Boot | Baeldung 
Testing | Spring 

Requirements:
1. It has the following fields:
- Email (required).
- First name (required);
- Last name (required);
- Birth date (required). Value must be earlier than current date;
- Address (optional);
- Phone number (optional);

2. It has the following functionality:
- Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
- Update one/some user fields;
- Update all user fields;
- Delete user;
- Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects 

4. Code is covered by unit tests using Spring 
5. Code has error handling for REST;
6. API responses are in JSON format;
7. Use of database is not necessary. The data persistence layer is not required.
8. Any version of Spring Boot. Java version of your choice.
9. You can use Spring Initializer utility to create the project: Spring Initializr.
