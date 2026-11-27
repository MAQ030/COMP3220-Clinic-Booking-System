
package clinic.ui;

import java.time.LocalDate;
import java.util.List;

import clinic.domain.Patient;
import clinic.domain.Doctor;
import clinic.domain.TimeSlot;
import clinic.domain.Appointment;

import clinic.service.PatientService;
import clinic.service.DoctorService;
import clinic.service.AppointmentService;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// This code was developed with assistance from AI.
// Review, test, and understand the code before submission.

public class PatientView {

    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;

    public PatientView(PatientService patientService,
                       DoctorService doctorService,
                       AppointmentService appointmentService) {

        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    public void show() {

        Stage stage = new Stage();

        // Patient profile

        TextField patientIdField = new TextField();
        TextField nameField = new TextField();
        TextField phoneField = new TextField();

        Button viewProfileButton = new Button("View Profile");
        Button updateProfileButton = new Button("Update Profile");

        Label profileDisplay = new Label();

        viewProfileButton.setOnAction(event -> {

            try {
                int patientId = Integer.parseInt(patientIdField.getText());

                Patient patient = patientService.getPatient(patientId);

                if (patient == null) {
                    profileDisplay.setText("Patient not found.");
                    return;
                }

                nameField.setText(patient.getName());
                phoneField.setText(patient.getPhone());

                profileDisplay.setText("Patient information loaded.");

            } catch (RuntimeException e) {
                profileDisplay.setText("Could not load patient.");
            }
        });

        updateProfileButton.setOnAction(event -> {

            try {
                int patientId = Integer.parseInt(patientIdField.getText());

                patientService.updatePatient(patientId,
                        nameField.getText(), phoneField.getText());

                profileDisplay.setText("Profile updated.");

            } catch (RuntimeException e) {
                profileDisplay.setText("Could not update profile.");
            }
        });

        // Find doctors

        TextField specialtyField = new TextField();
        Button searchButton = new Button("Find Doctors");
        Label doctorDisplay = new Label();

        searchButton.setOnAction(event -> {

            try {
                List<Doctor> doctors = doctorService.findDoctorsBySpecialty(
                        specialtyField.getText().trim());

                String result = "";

                for (Doctor doctor : doctors) {
                    result = result
                            + "Doctor ID: " + doctor.getDoctorId()
                            + " | " + doctor.getName()
                            + " | " + doctor.getSpecialty() + "\n";
                }

                if (result.isEmpty()) {
                    result = "No doctors found.";
                }

                doctorDisplay.setText(result);

            } catch (RuntimeException e) {
                doctorDisplay.setText("Could not find doctors.");
            }
        });

        // Available time slots

        TextField doctorIdField = new TextField();
        TextField dateField = new TextField();
        TextField slotIdField = new TextField();

        Button availableButton = new Button("View Available Slots");
        Label slotDisplay = new Label();

        availableButton.setOnAction(event -> {

            try {
                int doctorId = Integer.parseInt(doctorIdField.getText());
                LocalDate date = LocalDate.parse(dateField.getText());

                List<TimeSlot> slots =
                        appointmentService.getAvailableSlots(doctorId, date);

                String result = "";

                for (TimeSlot slot : slots) {
                    result = result
                            + "Slot ID: " + slot.getSlotId()
                            + " | " + slot.getStartTime()
                            + " - " + slot.getEndTime() + "\n";
                }

                if (result.isEmpty()) {
                    result = "No available slots.";
                }

                slotDisplay.setText(result);

            } catch (RuntimeException e) {
                slotDisplay.setText("Could not retrieve available slots.");
            }
        });

        // Book appointments

        Button bookButton = new Button("Book Appointment");
        Label appointmentDisplay = new Label();

        bookButton.setOnAction(event -> {

            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                int slotId = Integer.parseInt(slotIdField.getText());
                LocalDate date = LocalDate.parse(dateField.getText());

                Appointment appointment =
                        appointmentService.bookAppointment(
                                patientId, slotId, date);

                appointmentDisplay.setText(
                        "Appointment booked. ID: "
                        + appointment.getAppointmentId());

            } catch (RuntimeException e) {
                appointmentDisplay.setText(
                        "Could not book appointment: " + e.getMessage());
            }
        });

        // View appointments

        Button viewButton = new Button("View My Appointments");

        viewButton.setOnAction(event -> {

            try {
                int patientId = Integer.parseInt(patientIdField.getText());

                List<Appointment> appointments =
                        appointmentService.getPatientAppointment(patientId);

                String result = "";

                for (Appointment appointment : appointments) {
                    result = result
                            + "ID: " + appointment.getAppointmentId()
                            + " | " + appointment.getDate()
                            + " | " + appointment.getTimeSlot().getStartTime()
                            + " | " + appointment.getStatus() + "\n";
                }

                if (result.isEmpty()) {
                    result = "No appointments found.";
                }

                appointmentDisplay.setText(result);

            } catch (RuntimeException e) {
                appointmentDisplay.setText("Could not retrieve appointments.");
            }
        });

        // Cancel appointments

        TextField appointmentIdField = new TextField();
        Button cancelButton = new Button("Cancel Appointment");

        cancelButton.setOnAction(event -> {

            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                int appointmentId =
                        Integer.parseInt(appointmentIdField.getText());

                List<Appointment> appointments =
                        appointmentService.getPatientAppointment(patientId);

                boolean found = false;

                for (Appointment appointment : appointments) {
                    if (appointment.getAppointmentId() == appointmentId) {
                        found = true;
                    }
                }

                if (!found) {
                    appointmentDisplay.setText("Appointment not found.");
                    return;
                }

                appointmentService.cancelAppointment(appointmentId);

                appointmentDisplay.setText("Appointment cancelled.");

            } catch (RuntimeException e) {
                appointmentDisplay.setText("Could not cancel appointment.");
            }
        });

        VBox layout = new VBox(
                10,
                new Label("Patient Profile"),
                new Label("Patient ID"),
                patientIdField,
                viewProfileButton,
                new Label("Name"),
                nameField,
                new Label("Phone"),
                phoneField,
                updateProfileButton,
                profileDisplay,

                new Label("Find Doctors"),
                new Label("Specialty"),
                specialtyField,
                searchButton,
                doctorDisplay,

                new Label("Appointment Booking"),
                new Label("Doctor ID"),
                doctorIdField,
                new Label("Date (YYYY-MM-DD)"),
                dateField,
                availableButton,
                slotDisplay,
                new Label("Slot ID"),
                slotIdField,
                bookButton,

                new Label("My Appointments"),
                viewButton,
                new Label("Appointment ID"),
                appointmentIdField,
                cancelButton,
                appointmentDisplay
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 500, 600);

        stage.setTitle("Patient");
        stage.setScene(scene);
        stage.show();
    }
}
