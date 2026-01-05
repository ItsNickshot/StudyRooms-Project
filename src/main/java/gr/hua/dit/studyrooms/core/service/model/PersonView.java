package gr.hua.dit.studyrooms.core.service.model;

public record PersonView(
    Long id,
    String username,
    String firstName,
    String lastName,
    String email,
    String type
) {}
