package ma.ensate.myapplication.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.Demande;
import ma.ensate.myapplication.viewmodel.DemandeViewModel;
import ma.ensate.myapplication.model.UploadResponse;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.TokenManager;

public class DemandeFormFragment extends Fragment {

    public DemandeFormFragment() {
        super(R.layout.fragment_demande_form);
    }

    private Uri justificatifUri;
    private ActivityResultLauncher<String> pickJustificatifLauncher;
    private ApiService apiService;
    private String selectedTypeValue;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DemandeViewModel viewModel = new ViewModelProvider(requireActivity()).get(DemandeViewModel.class);
        apiService = RetrofitClient.api();
        NavController nav = Navigation.findNavController(view);

        MaterialAutoCompleteTextView etType = view.findViewById(R.id.etType);
        TextInputEditText etDateDebut = view.findViewById(R.id.etDateDebut);
        TextInputEditText etDateFin = view.findViewById(R.id.etDateFin);
        TextInputEditText etMotif = view.findViewById(R.id.etMotif);
        Button btnSubmit = view.findViewById(R.id.btnSubmitDemande);
        ProgressBar progress = view.findViewById(R.id.progressDemandeForm);
        ImageButton btnBack = view.findViewById(R.id.btnBackDemande);
        Button btnPickJustificatif = view.findViewById(R.id.btnPickJustificatif);
        TextView tvJustificatifName = view.findViewById(R.id.tvJustificatifName);

        String[] typeLabels = new String[] {"Conge", "Permission", "Autorisation", "Demission"};
        String[] typeValues = new String[] {"CONGE", "PERMISSION", "AUTORISATION", "DEMISSION"};
        etType.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, typeLabels));
        etType.setOnClickListener(v -> etType.showDropDown());
        etType.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) etType.showDropDown();
        });
        etType.setOnItemClickListener((parent, v, position, id) -> {
            if (position >= 0 && position < typeValues.length) {
                selectedTypeValue = typeValues[position];
            }
        });

        etDateDebut.setOnClickListener(v -> showDatePicker(etDateDebut));
        etDateFin.setOnClickListener(v -> showDatePicker(etDateFin));

        pickJustificatifLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                justificatifUri = uri;
                tvJustificatifName.setText(getFileName(uri));
            }
        });

        btnPickJustificatif.setOnClickListener(v -> pickJustificatifLauncher.launch("*/*"));

        btnBack.setOnClickListener(v -> nav.navigateUp());

        btnSubmit.setOnClickListener(v -> {
            clearErrors(etType, etDateDebut, etDateFin, etMotif);

            String type = selectedTypeValue != null ? selectedTypeValue : textOf(etType).toUpperCase(Locale.US);
            String dateDebut = textOf(etDateDebut);
            String dateFin = textOf(etDateFin);
            String motif = textOf(etMotif);

            boolean hasError = false;
            if (TextUtils.isEmpty(type)) { etType.setError("Type requis"); hasError = true; }
            if (TextUtils.isEmpty(dateDebut)) { etDateDebut.setError("Date début requise"); hasError = true; }
            if (TextUtils.isEmpty(dateFin)) { etDateFin.setError("Date fin requise"); hasError = true; }
            if (TextUtils.isEmpty(motif)) { etMotif.setError("Motif requis"); hasError = true; }

            if (hasError) return;

            TokenManager tokenManager = new TokenManager(requireContext());
            Long createdBy = tokenManager.getUserId();
            if (createdBy == null) {
                Toast.makeText(requireContext(), "Erreur: utilisateur non identifié", Toast.LENGTH_SHORT).show();
                return;
            }

            Demande demande = new Demande();
            demande.setType(type);
            demande.setDateDebut(dateDebut);
            demande.setDateFin(dateFin);
            demande.setMotif(motif);
            demande.setCreatedBy(createdBy);

            setLoading(true, btnSubmit, progress);
            uploadJustificatifThenCreate(demande, viewModel, btnSubmit, progress, nav);
        });
    }

    private void uploadJustificatifThenCreate(Demande demande, DemandeViewModel viewModel,
                                              Button btnSubmit, ProgressBar progress, NavController nav) {
        if (justificatifUri == null) {
            createDemande(demande, viewModel, btnSubmit, progress, nav);
            return;
        }
        uploadFile(justificatifUri, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                demande.setJustificatifUrl(url);
                createDemande(demande, viewModel, btnSubmit, progress, nav);
            }

            @Override
            public void onError(Throwable t) {
                setLoading(false, btnSubmit, progress);
                Toast.makeText(requireContext(), "Erreur upload justificatif: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDemande(Demande demande, DemandeViewModel viewModel,
                               Button btnSubmit, ProgressBar progress, NavController nav) {
        viewModel.createDemande(demande, new DemandeViewModel.ActionCallback() {
            @Override
            public void onSuccess(Demande created) {
                setLoading(false, btnSubmit, progress);
                Toast.makeText(requireContext(), "Demande envoyée", Toast.LENGTH_SHORT).show();
                nav.navigateUp();
            }

            @Override
            public void onError(Throwable t) {
                setLoading(false, btnSubmit, progress);
                Toast.makeText(requireContext(), "Erreur lors de l'envoi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker(TextInputEditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, day) -> {
            String value = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            target.setText(value);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String textOf(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private String textOf(MaterialAutoCompleteTextView et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void clearErrors(MaterialAutoCompleteTextView type, TextInputEditText... fields) {
        type.setError(null);
        for (TextInputEditText f : fields) f.setError(null);
    }

    private void setLoading(boolean loading, Button button, ProgressBar bar) {
        button.setEnabled(!loading);
        bar.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void uploadFile(Uri uri, UploadCallback cb) {
        try {
            String filename = getFileName(uri);
            String mime = requireContext().getContentResolver().getType(uri);
            if (mime == null) {
                mime = "application/octet-stream";
            }
            byte[] bytes = readBytes(uri);
            RequestBody body = RequestBody.create(bytes, MediaType.parse(mime));
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", filename, body);
            apiService.uploadFile(part).enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        cb.onSuccess(response.body().getUrl());
                    } else {
                        cb.onError(new Exception("Upload failed"));
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    cb.onError(t);
                }
            });
        } catch (Exception e) {
            cb.onError(e);
        }
    }

    private byte[] readBytes(Uri uri) throws Exception {
        ContentResolver resolver = requireContext().getContentResolver();
        InputStream input = resolver.openInputStream(uri);
        if (input == null) return new byte[0];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int n;
        while ((n = input.read(data)) != -1) {
            buffer.write(data, 0, n);
        }
        input.close();
        return buffer.toByteArray();
    }

    private String getFileName(Uri uri) {
        String name = "file";
        ContentResolver resolver = requireContext().getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (cursor.moveToFirst() && nameIndex >= 0) {
                name = cursor.getString(nameIndex);
            }
            cursor.close();
        }
        return name;
    }

    private interface UploadCallback {
        void onSuccess(String url);
        void onError(Throwable t);
    }
}
