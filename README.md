# Clinic Appointment Booking System

**COMP-3220 | Project Deliverable 1 | Group 10**

A small prototype for a medical clinic's appointment system. For Deliverable 1, it lets a receptionist register a patient and see the patient's information on screen.

## Group Members

- Funmilayo, David
- Nduhukire, Roldan
- Qureshi, Mudasir
- Simic, Marko

## Scope (PD1)

**Included**
- Registering a patient
- Assigning each patient an internal patient ID
- Entering the patient's name and phone number
- Displaying the patient's information
- Simple JavaFX interface

**Not included yet**
- Appointment booking and cancellation
- Doctor schedules and doctor management
- Login and authentication
- Appointment history and conflict checking
- Database or file storage (patients are kept in memory only and are lost when the app closes)

## Project Structure

```
src/main/java/clinic/
├── Patient.java     # Stores a patient's ID, name, and phone number
└── ClinicApp.java   # JavaFX window; creates and displays a Patient on registration
pom.xml              # Maven build file (JavaFX 17, Java 17)
```

## Tech Stack

- Java (JDK 17+)
- JavaFX 17 (`javafx-controls`)
- Maven

## How to Run

**Requirements:** JDK 17 or newer and Maven.

**From the command line**

```bash
git clone https://github.com/MAQ030/COMP3220-Clinic-Booking-System.git
cd COMP3220-Clinic-Booking-System
mvn javafx:run
```

**From Eclipse**

1. Clone the repository, then choose *File > Import > Maven > Existing Maven Projects* and select the project folder.
2. Right-click the project, then *Run As > Maven build...*, enter the goal `javafx:run`, and click Run.

## How to Use

1. Enter the patient's name and phone number.
2. Click **Register Patient**.
3. The patient's ID, name, and phone number appear below the button.

## AI Usage

AI tools were used for parts of this project. See the AI usage log in the Deliverable 1 report and the disclosure comments at the top of each source file.

## Repository Notes

Compiled output and IDE files (`bin/`, `target/`, `.classpath`, `.project`, `.settings/`) are excluded through `.gitignore`.
