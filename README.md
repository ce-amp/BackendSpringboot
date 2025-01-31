# Quiz Application Backend

## Prerequisites

- Java JDK 17 or later
- Maven 3.6+
- Docker
- Docker Compose

## Docker Setup

### 1. Build and Run with Docker Compose

The application is containerized using Docker with two services:

- MySQL database
- Spring Boot backend

To start the application using Docker:

```bash
# Build and start all services
docker compose up --build

# Run in detached mode (background)
docker compose up -d

# Scale backend service (optional)
docker compose up -d --scale backend=3
```

### 2. Docker Services Configuration

#### MySQL Container

- Port: 3307 (host) -> 3306 (container)
- Database: quiz_db
- Username: quizuser
- Password: 123mamad
- Persistent volume: mysql-data

#### Backend Container

- Port: 8000
- Built from source using multi-stage Dockerfile
- Automatically connects to MySQL container
- Scalable using Docker Compose

### 3. Container Management

```bash
# Stop all containers
docker compose down

# View running containers
docker ps

# View container logs
docker compose logs backend
docker compose logs mysql

# Access MySQL container
docker exec -it quiz-mysql mysql -u quizuser -p123mamad

# Remove all containers and volumes
docker compose down -v
```

### 4. Environment Variables

The application uses environment variables for configuration. Default values are provided but can be overridden:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/quiz_db
SPRING_DATASOURCE_USERNAME=quizuser
SPRING_DATASOURCE_PASSWORD=123mamad
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
```

### 5. Docker Network

Services communicate through a dedicated network 'quiz-network' created by Docker Compose.

## Database Setup

### 1. Install MySQL

```bash
# MacOS (using Homebrew)
brew install mysql

# Start MySQL Service
brew services start mysql

# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server

# Start MySQL Service on Ubuntu
sudo systemctl start mysql
```

### 2. Configure Database

Connect to MySQL (default root password is empty, just press Enter):

```bash
mysql -u root -p
```

Run these SQL commands:

```sql
# Create database
CREATE DATABASE quiz_db;

# Create user
CREATE USER 'quizuser'@'localhost' IDENTIFIED BY '123mamad';

# Grant privileges
GRANT ALL PRIVILEGES ON quiz_db.* TO 'quizuser'@'localhost';
FLUSH PRIVILEGES;

# Verify
SHOW DATABASES;
```

## Application Setup

### 1. Clone the repository

```bash
git clone [your-repository-url]
cd [repository-name]
```

### 2. Configure Application Properties

The `application.properties` is already configured with:

- Database connection (MySQL)
- JWT settings
- Server port (8000)
- Logging configurations

### 3. Build the Application

```bash
# Clean and install dependencies
mvn clean install
```

### 4. Run the Application

```bash
# Start the application
mvn spring-boot:run
```

The application will start on `http://localhost:8000`

## Verification Steps

1. Check if containers are running:

```bash
docker ps
```

2. Check application health:

```bash
curl http://localhost:8000/actuator/health
```

3. Test database connection:

```bash
docker exec -it quiz-mysql mysql -u quizuser -p123mamad -e "SHOW DATABASES;"
```

## Troubleshooting Docker Issues

### Container Issues

1. Check container logs:

```bash
docker compose logs backend
docker compose logs mysql
```

2. Restart containers:

```bash
docker compose restart
```

3. Reset everything:

```bash
docker compose down -v
docker compose up --build
```

### Database Issues

1. Check MySQL container status:

```bash
docker exec quiz-mysql mysqladmin -u quizuser -p123mamad ping
```

2. Access MySQL container:

```bash
docker exec -it quiz-mysql bash
```

### Network Issues

1. Inspect network:

```bash
docker network ls
docker network inspect quiz-network
```

2. Check container connectivity:

```bash
docker exec quiz-backend ping mysql
```

## Troubleshooting

### Database Issues

1. Verify MySQL is running:

```bash
# MacOS
brew services list

# Ubuntu
sudo systemctl status mysql
```

2. Reset MySQL root password if needed:

```bash
# Stop MySQL
brew services stop mysql

# Start in safe mode
mysql.server start --skip-grant-tables

# Connect and reset password
mysql -u root
```

Then run:

```sql
USE mysql;
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_new_password';
FLUSH PRIVILEGES;
```

### Application Issues

- Check logs in `application.log`
- Verify port 8000 is not in use: `lsof -i :8000`
- Ensure all Maven dependencies are downloaded: `mvn dependency:tree`

## API Documentation

- API documentation available at: `http://localhost:8000/swagger-ui.html`
- Health check endpoint: `http://localhost:8000/actuator/health`

## Security

- JWT authentication is enabled
- Default token expiration: 24 hours
- Protected endpoints require "Bearer" token in Authorization header

## Environment Variables (Optional)

You can override default configurations:

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_USER=quizuser
export MYSQL_PASSWORD=123mamad
```

## Logs

- Application logs: `logs/application.log`
- SQL logs are enabled for debugging
- Security debug logs are enabled

```

This README provides a complete guide for setting up and running your Quiz application. Let me know if you need any clarification or additional sections!
```
