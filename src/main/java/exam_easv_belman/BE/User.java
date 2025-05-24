package exam_easv_belman.BE;

import java.util.Objects;

public class User {

    private int id;
    private String username;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String qrKey;
    private String signaturePath;

    public User(int id, String username, String password, Role role, String signaturePath) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.signaturePath = signaturePath;
    }

    public User(String username, String password, Role role, String firstName, String lastName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getSignaturePath() {
        return signaturePath;
    }
    public void setSignaturePath(String signaturePath) {
        this.signaturePath = signaturePath;
    }

    public String getQrKey() {
        return qrKey;
    }

    public void setQrKey(String qrKey) {
        this.qrKey = qrKey;
    }




    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName + ". Username: " + username;
    }
}
