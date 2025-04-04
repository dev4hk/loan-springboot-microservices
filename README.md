# 💼 Loan Service Project

A full-stack loan processing system built with **Spring Boot** and **Angular**, showcasing a modular microservices architecture.  
This project simulates real-world loan workflows — from counseling and application to judgment, approval, repayment, and dashboard tracking — with dedicated roles for both customers and managers.

---

## 🧰 Tech Stack

### 🔧 Backend
- **Spring Boot 3.4.1**
- **Spring Cloud** – Service discovery & config
- **Resilience4j** – Circuit breaking and fault tolerance
- **Keycloak** – Authentication & authorization
- **RabbitMQ** / **Kafka** – Messaging
- **H2** – In-memory database (for local/testing)
- **Docker** – Containerization
- **Observability Stack**:
    - **Grafana**, **Loki**, **Alloy**, **Prometheus**, **Tempo**
    - **OpenTelemetry** – Distributed tracing & metrics

### 🎨 Frontend
- **Angular 19.1.4**
- **Keycloak-js** – Client-side auth
- **Angular Material** / **Bootstrap** – UI frameworks
- **ApexCharts.js** – Data visualization & charts

---

## 🎯 Project Highlights

- Modular microservice architecture for each domain (counsel, application, judgment, etc.)
- Real-time async communication using Kafka/RabbitMQ
- Role-based access control using Keycloak (Customer / Manager)
- Real-time monitoring and distributed tracing via OpenTelemetry stack
- Interactive dashboards and flow-based UI for both customers and managers

---

## ✅ Prerequisites

To run this project locally, you'll need:

- **Java 21**
- **Maven**
- **Node.js + npm**
- **Docker** (for supporting services like Keycloak, RabbitMQ, etc.)

---

## 📐 Architecture

### 🗂️ System Architecture
![Architecture Diagram](screenshots/architecture.png)

### 🔁 Application Flow
![Flowchart Diagram](screenshots/flowchart.png)

---

## 👤 Customer Workflow

### ✅ Input Validation
![Customer Input Validation Demo](screenshots/customer_input_validation.gif)

### 💬 Counseling
![Customer Counsel Demo](screenshots/customer_counsel.gif)

### 📝 Application Submission
![Customer Application Demo](screenshots/customer_application.gif)

### 📄 Contract Generation
![Customer Loan Contract Demo](screenshots/customer_contract.gif)

### 💸 Repayment Process
![Customer Repayment Demo](screenshots/customer_repayment.gif)

### 📘 Final Repayment Summary
![Customer Repayment Final Demo](screenshots/customer_repayment_final.gif)

### 📊 Dashboard Overview
![Customer Dashboard Demo](screenshots/customer_dashboard.gif)

---

## 🧑‍💼 Manager Workflow

### 🗣️ Reviewing Counseling Sessions
![Manager Counsel Demo](screenshots/manager_counsel.gif)

### 🧐 Application Judgment
![Manager Judgement Demo](screenshots/manager_Judgement.gif)

### ✅ Approving Loan Decisions
![Manager Grant Judgement Demo](screenshots/manager_grant_judgement.gif)

### 💵 Final Payout
![Manager Payout Demo](screenshots/manager_payout.gif)

### 🧾 Final Application Review
![Manager Application Final Demo](screenshots/manager_application_final.png)

### 📈 Manager Dashboard
![Manager Dashboard Demo](screenshots/manager_dashboard.gif)
