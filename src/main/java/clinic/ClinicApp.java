// We used ChatGPT to help with ClinicApp.java.
// The code was reviewed, tested, and we understood it before submission.

package clinic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClinicApp extends Application {

    private int nextPatientId = 1001;

    @Override
    public void start(Stage stage) {

        TextField nameField = new TextField();
        TextField phoneField = new TextField();

        Button registerButton = new Button("Register Patient");

        Label patientDisplay = new Label("No patient registered yet.");

        registerButton.setOnAction(event -> {

            String name = nameField.getText.trim();
            String phone = phoneField.getText.trim();

            // make sure both fields are filled in
            if (name.isEmpty() || phone.isEmpty()) {
                patientDisplay.setText("Please enter both a name and phone number.");
                return;
            }

            Patient patient = new Patient(nextPatientId, name, phone);

            nextPatientId++;

            patientDisplay.setText(
                    "Patient ID: " + patient.getPatientId()
                    + "\nName: " + patient.getName()
                    + "\nPhone: " + patient.getPhone()
            );

            // clear fields after successful registration
            nameField.clear();
            phoneField.clear();
        });

        VBox layout = new VBox(
                10,
                new Label("Register Patient"),
                nameField,
                phoneField,
                registerButton,
                patientDisplay
        );

        Scene scene = new Scene(layout, 400, 300);

        stage.setTitle("Clinic Appointment Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
