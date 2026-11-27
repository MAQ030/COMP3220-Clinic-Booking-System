
package clinic.ui;

import java.time.LocalDate;
import java.util.List;

import clinic.domain.Doctor;
import clinic.domain.Appointment;
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

public class DoctorView {

    private DoctorService doctorService;
    private AppointmentService appointmentService;

    public DoctorView(DoctorService doctorService,
                      AppointmentService appointmentService) {

        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    public void show() {

        Stage stage = new Stage();

        TextField doctorIdField = new TextField();
        TextField startDateField = new TextField();
        TextField endDateField = new TextField();

        Button scheduleButton = new Button("View Schedule");
        Label scheduleDisplay = new Label();

        scheduleButton.setOnAction(event -> {

            try {
                int doctorId = Integer.parseInt(doctorIdField.getText());

                LocalDate startDate =
                        LocalDate.parse(startDateField.getText());

                LocalDate endDate =
                        LocalDate.parse(endDateField.getText());

                Doctor doctor = doctorService.getDoctor(doctorId);

                if (doctor == null) {
                    scheduleDisplay.setText("Doctor not found.");
                    return;
                }

                if (endDate.isBefore(startDate)) {
                    scheduleDisplay.setText("Enter a valid date range.");
                    return;
                }

                List<Appointment> appointments =
                        appointmentService.getDoctorAppointment(
                                doctorId, startDate, endDate);

                String result = "Doctor: " + doctor.getName() + "\n\n";

                if (appointments.isEmpty()) {
                    result = result + "No appointments found.";
                }

                for (Appointment appointment : appointments) {

                    result = result
                            + "Appointment ID: "
                            + appointment.getAppointmentId()
                            + "\nPatient: "
                            + appointment.getPatient().getName()
                            + "\nDate: " + appointment.getDate()
                            + "\nTime: "
                            + appointment.getTimeSlot().getStartTime()
                            + "\nStatus: " + appointment.getStatus()
                            + "\n\n";
                }

                scheduleDisplay.setText(result);

            } catch (RuntimeException e) {
                scheduleDisplay.setText("Could not retrieve schedule.");
            }
        });

        VBox layout = new VBox(
                10,
                new Label("Doctor Schedule"),
                new Label("Doctor ID"),
                doctorIdField,
                new Label("Start Date (YYYY-MM-DD)"),
                startDateField,
                new Label("End Date (YYYY-MM-DD)"),
                endDateField,
                scheduleButton,
                scheduleDisplay
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 500, 600);

        stage.setTitle("Doctor");
        stage.setScene(scene);
        stage.show();
    }
}
