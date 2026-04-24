# Real-Time Ecommerce Analytics Platform

A full-stack real-time analytics platform built with Java, Spring Boot, Apache Kafka, and React. The system processes ecommerce events as they happen and provides live business intelligence through an interactive dashboard.

## Features

- **Real-Time Order Processing** — Kafka-powered event streaming processes orders instantly as they are placed
- **Live Sales Dashboard** — React frontend displays live revenue, order volume, and product performance
- **Top Product Analytics** — Tracks and ranks best-selling products by category in real time
- **Suspicious Order Detection** — Flags potentially fraudulent orders based on behavioral patterns such as unusual order amounts, high frequency ordering, and multiple address changes
- **Revenue Tracking** — Monitors revenue trends across daily, weekly, and monthly timeframes
- **Event-Driven Architecture** — Decoupled microservices communicate via Kafka topics for scalability and resilience

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3.x
- Apache Kafka — event streaming and message brokering
- Spring Data JPA
- PostgreSQL — persistent data storage
- Spring Security with JWT authentication
- Maven

**Frontend**
- React.js
- Chart.js — real-time data visualization
- Axios — API communication

**DevOps**
- Docker and Docker Compose
- AWS EC2 — deployment
- Git and GitHub

## Architecture

```
Order Service → Kafka Topic → Analytics Consumer → PostgreSQL → REST API → React Dashboard
                           ↘ Fraud Detection Consumer → Alert Service
```

- **Order Producer** — publishes order events to Kafka topic on every new order
- **Analytics Consumer** — consumes events and updates real-time sales metrics
- **Fraud Detection Consumer** — independently consumes same events and flags suspicious patterns
- **REST API Layer** — Spring Boot exposes aggregated analytics via REST endpoints
- **React Dashboard** — polls API and renders live charts and metrics

## Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- PostgreSQL 8+
- Apache Kafka 3+
- Docker (optional)

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/ecommerce-analytics
cd ecommerce-analytics
```

**2. Configure database**
```bash
# Update src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:3306/ecommerce_analytics
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**3. Start Kafka**
```bash
docker-compose up -d kafka zookeeper
```

**4. Run backend**
```bash
mvn spring-boot:run
```

**5. Run frontend**
```bash
cd frontend
npm install
npm start
```

**6. Access dashboard**
```
http://localhost:3000
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/analytics/sales/live | Get real-time sales metrics |
| GET | /api/analytics/products/top | Get top selling products |
| GET | /api/analytics/revenue/summary | Get revenue summary by period |
| GET | /api/fraud/alerts | Get flagged suspicious orders |
| POST | /api/orders | Place a new order (triggers Kafka event) |

## Key Technical Decisions

**Why Kafka?**
Traditional REST calls between services create tight coupling and fail under high order volume. Kafka decouples producers from consumers, handles high throughput, and allows multiple consumers to independently process the same events — analytics and fraud detection run simultaneously without interfering with each other.

**Why separate fraud detection consumer?**
Fraud detection has different processing requirements and scaling needs than analytics. Keeping them as independent consumers allows each to scale separately and fail independently without affecting the other.

**Why PostgreSQL over NoSQL?**
Analytics queries require complex aggregations and joins across time periods and categories. PostgreSQL's relational model and SQL aggregation functions handle these queries efficiently and reliably.

## What I Would Improve Next

- Add Redis caching layer for frequently accessed analytics queries
- Implement Kafka Streams for more complex real-time aggregations
- Add WebSocket support for true push-based live dashboard updates
- Implement dead letter queue for failed message processing
- Add comprehensive JUnit test coverage

## Screenshots

*(Add dashboard screenshots here)*

## License

MIT License
