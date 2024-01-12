## EOSIO Proxy Service

![Latest master workflow](https://github.com/nkrivenko/eosio-proxy-service/actions/workflows/pipeline.yml/badge.svg)
![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/nkrivenko/eosio-proxy-service?sort=semver)
![Coverage](./.github/badges/jacoco.svg)

### Purpose

EOSIO Proxy Service is a Java-based application designed to interact with the EOSIO blockchain. It is built using the Spring Framework and utilizes Gradle as its build automation tool. This service acts as an intermediary, providing a layer of abstraction for applications that wish to communicate with the EOSIO blockchain from a specific account.

### Prerequisites

- OpenJDK 17 or later
- Gradle 7.3 or later (if not using the Gradle Wrapper)
- An EOSIO node accessible via HTTP API

### Installation

1. Clone the repository:

```bash
git clone https://github.com/your-username/eosio-proxy-service.git
cd eosio-proxy-service
```

2. Build the project using Gradle:

```bash
./gradlew build
```

3. Run the service:

```bash
./gradlew bootRun
```

Alternatively, you can build a JAR file and run it:

```bash
./gradlew build
java -jar build/libs/eosio-proxy-service-0.1.0.jar
```

### Usage

After starting the EOSIO Proxy Service, it will be accessible at http://localhost:8080 (default port).

For now, service provides a single endpoint `/v1/chain/push_transaction` taking unsigned action DTO:

```json
{
    "contractAccount": "abcde.wax",
    "contractMethod": "helloworld",
    "data": {
        "name": "Test"
    }
}
```

