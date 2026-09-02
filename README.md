# Amber IoT Platform

Secure • Scalable • Real-Time IoT Backend Platform

Built with Spring Boot Microservices, MQTT, PostgreSQL, WebSockets, Docker, and JWT Authentication.

---

## Features

### Authentication & User Management

- JWT-based authentication
- User registration and login
- Email verification 
- Secure session management

### Device Communication

- MQTT-based device connectivity
- Real-time command delivery
- Device telemetry 
- Bidirectional device communication

### Microservices Architecture

- Service discovery with Eureka
- API Gateway routing
- Independent service deployment
- Scalable distributed architecture

### Real-Time Infrastructure

- WebSocket support
- STOMP messaging integration
- Event-driven communication

### Platform Services

- User Service
- Bridge Service
- Device Service
- Automation Service
- API Gateway
- Service Registry

---

## Architecture

```text
                     ┌─────────────────┐
                     │   Mobile App    │
                     └────────┬────────┘
                              │
                              ▼
                    ┌───────────────────┐
                    │    API Gateway    │
                    └────────┬──────────┘
                             │
         ┌───────────────────┼───────────────────┐
         ▼                   ▼                   ▼
 ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
 │ User Service │   │Bridge Service│   │Device Services│
 └──────┬───────┘   └──────┬───────┘   └──────────────┘
        │                  │
        ▼                  ▼
 ┌─────────────┐     ┌─────────────┐
 │ PostgreSQL  │     │ MQTT Broker │
 └─────────────┘     └──────┬──────┘
                            │
                            ▼
                      ┌──────────┐
                      │ IoT Edge │
                      └──────────┘

                Eureka Service Registry
```

---

## Technology Stack

### Core Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Cloud Gateway
- Spring Cloud Eureka

### Database

- PostgreSQL
- Redis

### Messaging

- MQTT
- WebSocket
- STOMP

### Infrastructure

- Docker
- Docker Compose

### Security

- JWT Authentication
- BCrypt Password Encoding
- Role-Based Authorization

### Development Tools

- Maven
- vs code

---

## Microservices

### Registry Service

Responsible for service discovery and registration.

**Port:** `9000`

Features:

- Service registration
- Service discovery
- Health monitoring

---

### Gateway Service

Central entry point for all clients.

**Port:** `8080`

Features:

- Request routing
- Security integration
- Cross-service communication


---

### Bridge Service

Responsible for IoT communication.

**Port:** `8081`

Features:

- MQTT communication
- Backend-driven device command processing
- Command acknowledgement and retry handling
- Device shadow and state persistence with Redis
- Kafka event consumption
- Telemetry routing
- Real-time updates

---

### User Service

Handles authentication and user management.

**Port:** `8082`

Features:

- Registration
- Login
- JWT generation
- Email verification
- Profile management 
---

### Device Service

Responsible for managing registered IoT devices.

**Port:** `8083`

Features:

- Device onboarding and registration
- Device ownership mapping
- Device status tracking
- Feature-based command support
- Device metadata management

---

### Automation Service

Manages backend-driven scenes and scheduled device actions.

**Port:** `8084`

Features:

- Scene creation, update, execution, and deletion
- Schedule creation, update, enable/disable, and deletion
- Kafka-based command publishing
- Quartz-backed scheduled execution

---
## Security Features

- JWT Access Token Authentication
- BCrypt Password Hashing
- Email OTP Verification
- Password Reset Workflow
- Stateless Session Management
- Route-Level Authorization
- Protected Microservice Communication


## Current Status

### Implemented

✅ JWT Authentication

✅ User Registration

✅ Login & Logout

✅ Email Verification

✅ Password Reset

✅ Eureka Service Discovery

✅ API Gateway Routing

✅ MQTT Integration

✅ Dockerized Services

✅ PostgreSQL Integration

✅ Spring Security

✅ WebSocket Infrastructure

✅ User Profiles

✅ Redis Integration

✅ Backend-Driven Device Controls

✅ Device Shadow and Command Acknowledgements

✅ Kafka Messaging Infrastructure

✅ Automation Service (Scenes and Schedules)

✅ Notification APIs (Read, Unread, and Mark as Read)

✅ Device Management APIs

---

### In Progress

🚧 Role-Based Access Control (RBAC)

---

### Planned

📌 Device Provisioning

📌 Monitoring & Analytics Dashboard

📌 OTA Update Support

---

## Project Structure

```text
BACKEND_MICROSERVICE
│
├── registry
│
├── gateway
│
├── user
│
├── bridge
│
├── device
│
├── automation
│
├── security
│
├── docker-compose.services.yml
│
└── docker-compose.routes.yml
```

---

## Architecture Highlights

- API Gateway as the single entry point
- Service discovery using Eureka Registry
- Stateless authentication with JWT
- MQTT-based low-latency device communication
- Kafka event-driven automation and command delivery
- Redis-backed device state and shadow data
- Backend-controlled scenes and schedules
- WebSocket support for real-time updates
- Independent service deployment and scaling
- Containerized infrastructure using Docker


## Deployment Architecture

```text
Docker Containers
│
├── Registry Service
├── Gateway Service
├── User Service
├── Bridge Service
├── Device Service
├── Automation Service
├── PostgreSQL
├── Redis
├── Kafka
└── MQTT Broker (external HiveMQ configuration)
```


## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker
- Docker Compose
- PostgreSQL
- Redis
- Kafka
- An MQTT broker configured in the Bridge service

---

### Clone Repository

```bash
git clone https://github.com/vivek-amber/amber-iot-backend.git

cd amber-iot-backend
```

---

### Build Services

```bash
docker compose -f docker-compose.services.yml build
```

Build specific service:

```bash
docker compose -f docker-compose.services.yml build user
```

---

### Start Services

```bash
docker compose -f docker-compose.services.yml up -d
```

Start the registry and gateway separately:

```bash
docker compose -f docker-compose.routes.yml up -d
```

The services compose file currently starts User, Device, Bridge, Automation,
Kafka, and Redis. The routes compose file starts Registry and Gateway.

To rebuild and start only the Bridge service with its dependencies:

```bash
docker compose -f docker-compose.services.yml up -d --build bridge
```

---

### Check Running Containers

```bash
docker ps
```

---

## Service Endpoints

| Service | Port |
|----------|------|
| Registry | 9000 |
| Gateway | 8080 |
| Bridge | 8081 |
| User | 8082 |
| Device | 8083 |
| Automation | 8084 |
| Kafka | 9092 |
| Redis | 6379 |

Registry and Gateway are started from `docker-compose.routes.yml`.

---

## Authentication Flow

```text
Register User
      │
      ▼
Email OTP Verification
      │
      ▼
Account Activated
      │
      ▼
Login
      │
      ▼
JWT Token Issued
      │
      ▼
Protected APIs
```

## Backend-Driven Control Flow

```text
Client
  |
  v
API Gateway
  |
  v
Automation Service -- publishes commands --> Kafka
                                             |
                                             v
                                       Bridge Service
                                             |
                                             v
                                        MQTT Device
                                             |
                                             v
                                  Acknowledgement / Shadow
                                             |
                                             v
                                           Redis
```

Scenes and schedules are managed through the Automation Service. Commands are
published to Kafka, consumed by the Bridge Service, and delivered to devices
over MQTT. The Bridge tracks command acknowledgements and maintains device
state in Redis.

---

## Security

### Authentication

- JWT Access Tokens
- BCrypt Password Hashing
- Stateless Authentication

### Verification

- Email OTP Verification
- Password Reset OTP
- Session Validation

### API Protection

```text
/user/auth/**
        ↓
 Public Endpoints

/all other routes
        ↓
 JWT Protected
```


## Roadmap

### Phase 1

- Microservice Foundation
- Eureka Discovery
- API Gateway
- MQTT Integration

### Phase 2

- User Authentication
- Real-Time Communication
- Device Management APIs
- Redis Integration

### Phase 3

- Notifications
- Monitoring & Analytics

### Phase 4

- Advanced Automation
- Enterprise Features

---

## Author

### Vivek

Backend Engineer focused on building scalable IoT platforms using:

- Spring Boot Microservices
- MQTT & Real-Time Communication
- PostgreSQL
- WebSockets/STOMP
- Docker & Containerization
- JWT Security
- Distributed System Design

## Vision

Amber IoT Platform aims to provide a scalable, secure, and real-time backend ecosystem for smart devices including air conditioners, air purifiers, and future connected appliances.

The platform is designed with a microservices-first architecture to support rapid feature development, independent service scaling, and large-scale device connectivity.