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
