// We used ChatGPT to help with Patient.java.
// The code was reviewed, tested, and we understood it before submission.

package clinic.domain;

public class Patient {

    private int patientId;
    private String name;
    private String phone;

    // constructor 
    public Patient(int patientId, String name, String phone) {
        this.patientId = patientId;
        this.name = name;
        this.phone = phone;
    }

    // getter methods 
    public int getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    // setter methods 
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}