package ma.ensate.backend.dto;

public class PersonnelOptionDto {
    private Long id;
    private String fullName;
    private String email;

    public PersonnelOptionDto(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
}
