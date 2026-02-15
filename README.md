# Tennis Club Manager

A comprehensive management system for tennis clubs, designed to simplify booking management, academy lessons, court maintenance, and member administration.

## Core Features

The project offers a complete suite of features accessible through an intuitive Command Line Interface (CLI):

- **Authentication and Roles**: Secure login and registration system with role management (Member, Coach, Admin).
- **Booking Management**: Real-time court booking with automatic availability and conflict checks.
- **Tennis Academy**: Lesson schedule management, student enrollment, and feedback/notes system for coaches.
- **Court Maintenance**: Technical intervention scheduling with automatic cancellation of impacted bookings and user notifications.
- **Administration**: Control panel for managing users, courts, and system settings.

## Tech Stack

- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Testing**: JUnit 5
- **Architecture**: Layered Architecture (Domain, ORM/DAO, Business Logic, View)

## Project Structure

The code is organized following object-oriented programming and clean code principles:

- `it.tennis_club.domain_model`: POJO classes representing domain entities.
- `it.tennis_club.orm`: Data Access Objects (DAO) for interacting with the PostgreSQL database.
- `it.tennis_club.business_logic`: Services implementing business rules and application logic.
- `it.tennis_club.view`: CLI-based user interface organized in modular menus.

## Configuration and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 13+

### 1. Database
Create a PostgreSQL database and configure credentials in the file:
`src/main/resources/db.properties` or `application.properties`

Execute SQL scripts in order:
1. `src/main/resources/schema.sql` (Table structure)
2. `src/main/resources/default.sql` (Optional initial data)

For reset database:
3. `src/main/resources/reset.sql`

### 2. Build and Execution
Compile the project with Maven:
```bash
mvn clean install
```

Run the application:
```bash
mvn exec:java
```

## Testing

To run the complete suite of unit and integration tests:
```bash
mvn test
```
