package gr.hua.dit.studyrooms.web.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterForm {

    @NotBlank(message = "Το username είναι υποχρεωτικό")
    @Size(max = 20, message = "Το username πρέπει να είναι μέχρι 20 χαρακτήρες")
    private String username;

    @NotBlank(message = "Ο κωδικός είναι υποχρεωτικός")
    @Size(min = 4, max = 20, message = "Ο κωδικός πρέπει να είναι 4-20 χαρακτήρες")
    private String password;

    @NotBlank(message = "Το email είναι υποχρεωτικό")
    @Email(message = "Μη έγκυρο email")
    private String email;

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό")
    private String firstName;

    @NotBlank(message = "Το επώνυμο είναι υποχρεωτικό")
    private String lastName;

    // Default Constructor
    public RegisterForm() {}

    // Getters & Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
