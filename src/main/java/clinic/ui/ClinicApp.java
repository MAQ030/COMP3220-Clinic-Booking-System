// This code was developed with assistance from AI.
// Review, test, and understand the code before submission.

package clinic.ui;

import clinic.database.DatabaseManager;
import clinic.repository.*;
import clinic.service.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClinicApp extends Application {

    @Override
    public void start(Stage stage) {

        // Initialize the database
        DatabaseManager databaseManager =
                new DatabaseManager("jdbc:sqlite:clinic.db");

        databaseManager.initializeDatabase();

        // Create repositories
        PatientRepository patientRepository =
                new SQLitePatientRepository(databaseManager);

        DoctorRepository doctorRepository =
                new SQLiteDoctorRepository(databaseManager);

        TimeSlotRepository timeSlotRepository =
                new SQLiteTimeSlotRepository(databaseManager);

        AppointmentRepository appointmentRepository =
                new SQLiteAppointmentRepository(databaseManager);

        // Create services
        PatientService patientService =
                new PatientService(patientRepository);

        DoctorService doctorService =
                new DoctorService(doctorRepository, timeSlotRepository);

        AppointmentService appointmentService =
                new AppointmentService(appointmentRepository,
                        patientRepository, timeSlotRepository);

        // Create views
        PatientView patientView =
                new PatientView(patientService, doctorService,
                        appointmentService);

        DoctorView doctorView =
                new DoctorView(doctorService, appointmentService);

        ReceptionistView receptionistView =
                new ReceptionistView(patientService, doctorService,
                        appointmentService);

        // Create main menu
        Button patientButton = new Button("Patient");
        Button doctorButton = new Button("Doctor");
        Button receptionistButton = new Button("Receptionist");

        patientButton.setOnAction(event -> patientView.show());
        doctorButton.setOnAction(event -> doctorView.show());
        receptionistButton.setOnAction(event -> receptionistView.show());

        VBox layout = new VBox(
                10,
                new Label("Select your role"),
                patientButton,
                doctorButton,
                receptionistButton
        );

        Scene scene = new Scene(layout, 400, 250);

        stage.setTitle("Clinic Appointment Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}