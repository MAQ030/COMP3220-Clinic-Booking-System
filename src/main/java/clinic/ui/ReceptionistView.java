
package clinic.ui;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import clinic.domain.Patient;
import clinic.domain.Doctor;
import clinic.domain.TimeSlot;
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

public class ReceptionistView {

    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;

    public ReceptionistView(PatientService patientService,
                            DoctorService doctorService,
                            AppointmentService appointmentService) {

        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    public void show() {

        Stage stage = new Stage();

        // Patient registration

        TextField nameField = new TextField();
        TextField phoneField = new TextField();

        Button registerButton = new Button("Register Patient");
        Label patientDisplay = new Label();

        registerButton.setOnAction(event -> {

            try {
                Patient patient = patientService.registerPatient(
                        nameField.getText(), phoneField.getText());

                patientDisplay.setText(
                        "Patient ID: " + patient.getPatientId()
                        + "\nName: " + patient.getName()
                        + "\nPhone: " + patient.getPhone());

                nameField.clear();
                phoneField.clear();

            } catch (IllegalArgumentException e) {
                patientDisplay.setText(e.getMessage());
            }
        });

        // Doctor registration

        TextField doctorNameField = new TextField();
        TextField specialtyField = new TextField();

        Button registerDoctorButton = new Button("Register Doctor");
        Label doctorDisplay = new Label();

        registerDoctorButton.setOnAction(event -> {

            String name = doctorNameField.getText().trim();
            String specialty = specialtyField.getText().trim();

            if (name.isEmpty() || specialty.isEmpty()) {
                doctorDisplay.setText("Enter name and specialty.");
                return;
            }

            try {
                Doctor doctor = doctorService.registerDoctor(name, specialty);

                doctorDisplay.setText(
                        "Doctor ID: " + doctor.getDoctorId()
                        + "\nName: " + doctor.getName()
                        + "\nSpecialty: " + doctor.getSpecialty());

                doctorNameField.clear();
                specialtyField.clear();

            } catch (IllegalArgumentException e) {
                doctorDisplay.setText(e.getMessage());
            }
        });

        // Doctor availability

        TextField doctorIdField = new TextField();
        TextField dayField = new TextField();
        TextField timeField = new TextField();
        TextField slotIdField = new TextField();

        Button addSlotButton = new Button("Add Time Slot");
        Button viewSlotsButton = new Button("View Time Slots");
        Button removeSlotButton = new Button("Remove Time Slot");

        Label slotDisplay = new Label();

        addSlotButton.setOnAction(event -> {

            try {
                int doctorId = Integer.parseInt(doctorIdField.getText());

                DayOfWeek day = DayOfWeek.valueOf(
                        dayField.getText().trim().toUpperCase());

                LocalTime time = LocalTime.parse(timeField.getText());

                TimeSlot slot = doctorService.addTimeSlot(
                        doctorId, day, time);

                slotDisplay.setText(
                        "Time slot added. ID: " + slot.getSlotId());

            } catch (RuntimeException e) {
                slotDisplay.setText("Could not add time slot: "
                        + e.getMessage());
            }
        });

        viewSlotsButton.setOnAction(event -> {

            try {
                int doctorId = Integer.parseInt(doctorIdField.getText());

                List<TimeSlot> slots =
                        doctorService.getDoctorTimeSlots(doctorId);

                String result = "";

                for (TimeSlot slot : slots) {
                    result = result
                            + "Slot ID: " + slot.getSlotId()
                            + " | " + slot.getDayOfWeek()
                            + " | " + slot.getStartTime()
                            + " - " + slot.getEndTime()
                            + "\n";
                }

                if (result.isEmpty()) {
                    result = "No time slots found.";
                }

                slotDisplay.setText(result);

            } catch (RuntimeException e) {
                slotDisplay.setText("Could not retrieve time slots.");
            }
        });

        removeSlotButton.setOnAction(event -> {

            try {
                int slotId = Integer.parseInt(slotIdField.getText());

                doctorService.removeTimeSlot(slotId);

                slotDisplay.setText("Time slot removed.");

            } catch (RuntimeException e) {
                slotDisplay.setText("Could not remove time slot.");
            }
        });

        VBox layout = new VBox(
                10,
                new Label("Patient Registration"),
                new Label("Name"),
                nameField,
                new Label("Phone"),
                phoneField,
                registerButton,
                patientDisplay,

                new Label("Doctor Registration"),
                new Label("Doctor Name"),
                doctorNameField,
                new Label("Specialty"),
                specialtyField,
                registerDoctorButton,
                doctorDisplay,

                new Label("Doctor Availability"),
                new Label("Doctor ID"),
                doctorIdField,
                new Label("Day (example: MONDAY)"),
                dayField,
                new Label("Start Time (example: 09:00)"),
                timeField,
                addSlotButton,
                viewSlotsButton,
                new Label("Slot ID"),
                slotIdField,
                removeSlotButton,
                slotDisplay
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 500, 600);

        stage.setTitle("Receptionist");
        stage.setScene(scene);
        stage.show();
    }
}
