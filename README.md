# Cinema Room REST Service
This is a REST service for managing a cinema room with 81 seats (9 rows and 9 columns), built as a learning project 
to practice backend development with Java and Spring Boot. This project is a part of a learning course provided by 
Hyperskill.

## Tech Stack
1. Java 23
2. Spring Boot
3. Gradle
4. Postman

## API Endpoints
- GET /seats - returns available seats with row, column, and price
- POST /purchase - purchases a ticket and returns a UUID purchase token
- POST /return - refunds a ticket using the purchase token
- GET /stats - returns the statistics, i.e. income, available seats and purchased seats (password-protected)

## How to Run
- Build command: `./gradlew build`
- Run command: `./gradle bootRun`
- Default port: 28852

## Project Structure
- `controller/` - REST endpoints
- `service/` - services containing the business logic
- `repository/` - in-memory database
- `model/` - domain entities
- `dto/` - request/response transfer objects
- `exception/` - custom exceptions and error messages
- `util/` - utility classes

## Additional Information
- This project uses an in-memory database.
- The password which the /stats endpoint is protected with is *"super_secret"*.
- Rows 1-4 cost 10, rows 5-9 cost 8.

## Lessons learned
- **REST API Layering** - Learned about the three main layers (controllers, services, repositories), what logic
belongs to which layer and how the layers interact.
- **Parallel API Calls** - Learned that REST APIs can be called in parallel. As such, resources must be protected.
This is demonstrated using synchronized methods (since the project uses an in-memory database).
- **Data Consistency and Server Crash Scenarios** - Recognized that multiple-step operations can leave the system in
an inconsistent state if the server crashes mid-operation. Learned about database transactions and how they can solve
this type of problems.
- **Global Exception Handling** - Utilized error handling using `@ControllerAdvice`.

## Known Limitations / Areas for Improvement
- **In-Memory Storage Limitations** - All data is lost on server restart. An actual database would also provide
transactional support.
- **Coarse-Grained Locking** - All service methods are synchronized. While this decision was made to prevent
simultaneous reads and writes, this severely limits throughput.
- **Better Separation of Concerns** - The statistics are currently handled by the `CinemaService`. It would be better
to move this logic into a separate service. Also, the `CinemaRepository` stores all of the data, even though with a
real database, the data would be separated into individual tables.
- **N+1 Queries** - If a real database was used, some of the queries would fall into the *"N+1 problem"* category.
Such queries would have to be fixed using join operators.
- **Lack of Unit and Integration Tests** - Even though this project didn't require the writing of tests, in a
real-life situation such tests would be essential.
- **Hardcoded Values** - Configuration values (mainly those that are related to the repository) are hard-coded.
