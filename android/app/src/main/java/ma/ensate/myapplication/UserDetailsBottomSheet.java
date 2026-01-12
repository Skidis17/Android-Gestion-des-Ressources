package ma.ensate.myapplication;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ma.ensate.myapplication.model.UserInfoDto;

public class UserDetailsBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ROLE = "role";
    private static final String ARG_CIN = "cin";
    private static final String ARG_NOM = "nom";
    private static final String ARG_PRENOM = "prenom";
    private static final String ARG_TEL = "telephone";
    private static final String ARG_TYPE = "typePersonnel";
    private static final String ARG_GRADE = "grade";
//    private static final String ARG_ECH = "echelon";
    private static final String ARG_DEPT = "departement";

    public static UserDetailsBottomSheet newInstance(UserInfoDto dto) {
        UserDetailsBottomSheet f = new UserDetailsBottomSheet();
        Bundle b = new Bundle();
        b.putString(ARG_USERNAME, dto.username);
        b.putString(ARG_EMAIL, dto.email);
        b.putString(ARG_ROLE, dto.role);
        b.putString(ARG_CIN, dto.cin);
        b.putString(ARG_NOM, dto.nom);
        b.putString(ARG_PRENOM, dto.prenom);
        b.putString(ARG_TEL, dto.telephone);
        b.putString(ARG_TYPE, dto.typePersonnel);
        b.putString(ARG_GRADE, dto.grade);
//        b.putString(ARG_ECH, dto.echelon);
        b.putString(ARG_DEPT, dto.departement);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheet_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvContent = view.findViewById(R.id.tvContent);

        Bundle a = getArguments();
        if (a == null) return;

        String username = a.getString(ARG_USERNAME, "-");
        tvTitle.setText(username);

        String content =
                "Email: " + a.getString(ARG_EMAIL, "-") + "\n" +
                        "Rôle: " + a.getString(ARG_ROLE, "-") + "\n\n" +
                        "CIN: " + a.getString(ARG_CIN, "-") + "\n" +
                        "Nom: " + a.getString(ARG_NOM, "-") + "\n" +
                        "Prénom: " + a.getString(ARG_PRENOM, "-") + "\n" +
                        "Téléphone: " + a.getString(ARG_TEL, "-") + "\n" +
                        "Type: " + a.getString(ARG_TYPE, "-") + "\n" +
                        "Grade: " + a.getString(ARG_GRADE, "-") + "\n" +
//                        "Échelon: " + a.getString(ARG_ECH, "-") + "\n" +
                        "Département: " + a.getString(ARG_DEPT, "-");

        tvContent.setText(content);
    }
}
