package Pojo;

public class GoRestUser {

    /** these fields belong to object and this are instance method */
    private String id;
    private String name; // name = "Toffee
    private String email; // email = "tof@yahoo.com"
    private String gender; // gender = "male"
    private String status; // status = "active"

    /** Encapsulation */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
