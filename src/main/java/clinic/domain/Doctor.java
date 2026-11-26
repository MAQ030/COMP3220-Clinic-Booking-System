package clinic.domain;

public class Doctor {
    
    private int doctorId;
    private String name;
    private String specialty;

    //constructor
    public Doctor(int doctorId, String name, String specialty) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialty = specialty;
    }

    //getter methods
    public int getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

}
