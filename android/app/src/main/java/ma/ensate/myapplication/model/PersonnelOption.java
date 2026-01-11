package ma.ensate.myapplication.model;

public class PersonnelOption {
    private Long id;
    private String label;
    private String email;

    public Long getId() { return id; }
    public String getLabel() { return label; }
    public String getEmail() { return email; }

    public void setId(Long id) { this.id = id; }
    public void setLabel(String label) { this.label = label; }
    public void setEmail(String email) { this.email = email; }
}
