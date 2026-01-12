package ma.ensate.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class PersonnelOption {

    private Long id;

    @SerializedName("fullName")
    private String fullName;

    private String email;

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }

    // ✅ Pour réutiliser ton code existant (p.getLabel())
    public String getLabel() {
        return fullName;
    }
}
