# Clinic Appointment Booking System

**COMP-3220 | Project Deliverable 3 | Group 10**  
University of Windsor, Fall 2026

## Overview

A JavaFX desktop application for managing patient registration, appointment bookings, and doctor schedules. The system uses SQLite for persistent local storage and provides separate interfaces for patients, doctors, and receptionists.

## Team Members

- David Funmilayo
- Roldan Nduhukire
- Mudasir Qureshi
- Marko Simic

## Features

- **Receptionist:** Register patients and doctors and configure doctor availability.
- **Patient:** Update contact information, search for doctors, and book or cancel appointments.
- **Doctor:** View appointment schedules within a specified date range.
- **System:** Persistent storage, input validation, and prevention of duplicate active bookings.

## Getting Started

### Requirements

- JDK 17
- Apache Maven

### Run the Application

```bash
git clone --branch main https://github.com/MAQ030/COMP3220-Clinic-Booking-System.git
cd COMP3220-Clinic-Booking-System
mvn javafx:run
```

The application automatically creates a local `clinic.db` database. An internet connection may be required on the first run to download Maven dependencies.

## Project Structure

```text
src/main/java/clinic/
├── database/     # Database initialization
├── domain/       # Domain entities
├── repository/   # Data access
├── service/      # Business logic
└── ui/           # JavaFX interfaces
```

The application uses a layered architecture to separate user interaction, business logic, domain modelling, and data persistence.

## Technologies

- Java 17
- JavaFX
- SQLite and JDBC
- Maven
- Visual Paradigm

## Project Development

The system was developed across three project deliverables:

- **D1:** Initial patient registration prototype.
- **D2:** Problem-domain analysis and implementation revision.
- **D3:** Software design and implementation of the clinic booking system.

Detailed design decisions, UML models, and implementation comparisons are covered in the project report.

## Repository

[COMP3220 Clinic Appointment Booking System](https://github.com/MAQ030/COMP3220-Clinic-Booking-System)
