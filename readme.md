
# PatelBNB

## Description
**PatelBNB** is a property rental platform built with **Java Spring Boot**, offering features to list, search, and book accommodations. The backend is designed to manage reservations, users, and property data, utilizing **PostgreSQL** as the database, with **Liquibase** for database migrations, and **Docker** for containerized deployments.

## Technologies
- **Java 21**: The core programming language used for the backend.
- **Spring Boot 3.3.3**: For building the backend APIs and managing application components.
  - **Spring Data JPA**: For database interaction and ORM functionality.
  - **Spring Security**: For managing authentication and authorization.
  - **OAuth2 Client**: For integrating with OAuth2 providers like Okta and Auth0.
  - **Spring Boot Validation**: For input validation.
- **Auth0 SDK**: For managing authentication and token verification using Auth0.
- **Liquibase**: For database versioning and migrations.
- **Docker**: For containerizing the application and PostgreSQL database.
- **PostgreSQL**: The relational database used to store application data.
- **MapStruct 1.5.5.Final**: For object mapping between DTOs and entities.
- **Maven**: For dependency management and building the project.
- **JUnit and Spring Boot Test**: For unit and integration testing.
- **Docker Compose**: To orchestrate Docker containers for local development.

---

[Frontend Project Documentation](https://github.com/Aarju2308/patenbnb_frontend) includes detailed information on the frontend's Technologies, Installation and Project Structure.

---

## Installation

### Prerequisites
Make sure you have the following installed:
- Java 21
- Maven
- Docker
- PostgreSQL (optional, if not using Docker)

### Clone the Repository
```bash
git clone https://github.com/Aarju2308/patelbnb_backend
cd PatelBNB
```

### Set Up Docker for PostgreSQL
Ensure Docker is running and set up the PostgreSQL container:
```bash
docker-compose up -d
```
This will pull the PostgreSQL image and run the database container as per the `docker-compose.yml` file.

### Configure Environment Variables
Create an `.env` file in the root directory with the following content:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/patelbnb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password

# Auth0 Configuration
AUTH0_CLIENT_ID=YOUR_CLIENT_ID
AUTH0_CLIENT_SECRET=YOUR_CLIENT_SECRET

# Server Port
SERVER_PORT=1323
```

### Running the Application
Once dependencies are installed and the database is running (via Docker), start the Spring Boot application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:1323`.

---

## API Endpoints

### Authentication Endpoints
- `GET /api/auth/get-authenticated-user` - Retrieve the currently authenticated user.
- `POST /api/auth/logout` - Log out the current user.

### Landlord Listing Endpoints
- `POST /api/landlord-listing/create` - Create a new listing (Multipart form data required).
- `PUT /api/landlord-listing/update` - Update an existing listing (Multipart form data required).
- `GET /api/landlord-listing/get-all` - Retrieve all listings for the landlord.
- `GET /api/landlord-listing/get-single` - Retrieve a single listing.
- `DELETE /api/landlord-listing/delete` - Delete a listing.

### Tenant Listing Endpoints
- `GET /api/tenant-listing/get-all-by-category` - Retrieve all listings by category.
- `GET /api/tenant-listing/get-one` - Retrieve a single listing.
- `POST /api/tenant-listing/search` - Search listings based on criteria.

### Booking Endpoints
- `POST /api/booking/create` - Create a new booking.
- `GET /api/booking/check-availability` - Check the availability of a listing.
- `GET /api/booking/get-booked-listing` - Retrieve all booked listings for the tenant.
- `DELETE /api/booking/cancel-booking` - Cancel a booking.
- `GET /api/booking/get-booked-listing-for-landlord` - Retrieve all booked listings for the landlord.

---

## Configuration

### Docker Configuration
- The `docker-compose.yml` file is configured to set up a **PostgreSQL** container automatically.
- To stop the Docker containers:
  ```bash
  docker-compose down
  ```

### Database Configuration
- You can find the database properties in `src/main/resources/application.properties` or configure them via environment variables, such as in the `.env` file mentioned above.

### Liquibase Migrations
Liquibase change logs are located in `src/main/resources/db/changelog/`. You can modify or add new migration scripts to evolve the database schema.

---

## Testing
Run the unit tests and integration tests with the following command:
```bash
mvn test
```

---

## Contributing
If you'd like to contribute, please follow these guidelines:
1. Fork the repository and create a feature branch.
2. Ensure code adheres to existing code formatting and conventions.
3. Run tests before submitting a PR.
4. Submit a Pull Request with a detailed description of the changes.

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

## Contact
For any inquiries or issues, feel free to reach out at `patelbnb_support@example.com`.

---

