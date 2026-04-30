About:

The Hotel Management System allows clients to reserve rooms and venues for a specific date and time. Users provide details such as the number of guests and select suitable rooms based on their needs.
An administrator can manage reservations by accepting, rejecting, or suggesting alternative dates.
This backend is developed using Java with the Spring Boot framework and a PostgreSQL database.

Technologies:
Java,
Spring Boot,
PostgreSQL,

Clone the project:
git clone https://github.com/Daipha00/SuzaHotelBack.git
cd SuzaHotelBack

Configure database:
Create a PostgreSQL database and update
spring.datasource.url=jdbc:postgresql://localhost:5432/hotel_management
spring.datasource.username=your_username
spring.datasource.password=your_password

Run the application:
mvn spring-boot:run

Access:
http://localhost:9090

