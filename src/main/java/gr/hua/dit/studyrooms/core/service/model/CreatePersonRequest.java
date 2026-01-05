package gr.hua.dit.studyrooms.core.service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePersonRequest(
    @NotBlank @Size(max = 20) String username,
    @NotBlank @Size(max = 50) @Email String email,
    @NotBlank @Size(max = 120) String password,
    String firstName,
    String lastName
) {}
