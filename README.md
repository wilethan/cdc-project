# CDC Project

This project demonstrates file chunking and compression using various utilities. It processes different file types and calculates storage savings.

## Prerequisites

- Java 8 or higher
- Maven 3.6.0 or higher

## Dependencies

The project uses the following dependencies:

- JUnit 3.8.1 (for testing)
- Bouncy Castle (for cryptographic operations)
- PostgreSQL JDBC Driver (for database operations)
- Snappy Java (for compression)

## How to Build

To build the project, run the following command in the project root directory:

```sh
mvn clean install
```

## How to Run

To run the project, use the following command:

```sh
mvn exec:java -Dexec.mainClass="com.example.Main"
```

## Main Class

The main class is `Main`. It processes a list of test files, chunks them, and calculates storage savings.

## File Processing

The project processes the following file types:

- `textfile.txt`
- `data.csv`
- `image.jpg`
- `binary.bin`
- `logs.log`
- `archive.zip`

## Utilities

The project includes several utility classes:

- `FileChunker`: Handles file chunking.
- `CompressionUtil`: Handles compression.
- `DatabaseUtil`: Handles database operations.
- `RabinFingerprint`: Implements Rabin fingerprinting.
- `HashUtil`: Provides hashing utilities.

## SQL Database

The project uses PostgreSQL for database operations. Follow these steps to set up the database:

1. Install PostgreSQL from [here](https://www.postgresql.org/download/).
2. Create a new database named `cdc_project`.
3. Create a new user with access to the `cdc_project` database.

### Database Schema

Run the following SQL commands to create the necessary tables:

```sql
CREATE TABLE file_chunks (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    chunk_data BYTEA NOT NULL,
    chunk_hash VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE compression_stats (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_size BIGINT NOT NULL,
    compressed_size BIGINT NOT NULL,
    compression_ratio DECIMAL(5, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Database Configuration

Update the `DatabaseUtil` class with your database connection details:

```java
// filepath: /c:/Users/wilet/Documents/Mastere_CTO_Tech_Lead/Java2025/cdc-project/src/main/java/com/example/DatabaseUtil.java
// ...existing code...
private static final String URL = "jdbc:postgresql://localhost:5432/cdc_project";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
// ...existing code...
```

Ensure that the PostgreSQL JDBC Driver is included in your `pom.xml` dependencies:

```xml
// filepath: /c:/Users/wilet/Documents/Mastere_CTO_Tech_Lead/Java2025/cdc-project/pom.xml
// ...existing code...
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.2.5</version>
</dependency>
// ...existing code...
```
