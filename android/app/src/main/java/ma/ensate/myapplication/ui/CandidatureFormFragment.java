package ma.ensate.myapplication.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ma.ensate.myapplication.model.UploadResponse;
import ma.ensate.myapplication.network.RetrofitClient;
import ma.ensate.myapplication.network.ApiService;
import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.viewmodel.CandidatureRecrutementViewModel;

public class CandidatureFormFragment extends Fragment {

    public CandidatureFormFragment() {
        super(R.layout.fragment_candidature_form);
    }

    private Long recrutementId;
    private CandidatureRecrutementViewModel viewModel;
    private Uri cvUri;
    private Uri lmUri;
    private ActivityResultLauncher<String> pickCvLauncher;
    private ActivityResultLauncher<String> pickLmLauncher;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidature_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recrutementId = getArguments() != null ? getArguments().getLong("recrutementId", -1) : -1;
        if (recrutementId == -1) {
            Toast.makeText(requireContext(), "Recrutement manquant", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }

        viewModel = new ViewModelProvider(this).get(CandidatureRecrutementViewModel.class);
        apiService = RetrofitClient.api();

        TextView tvRecrutementRef = view.findViewById(R.id.tv_recrutement_ref);
        EditText etNom = view.findViewById(R.id.et_nom);
        EditText etPrenom = view.findViewById(R.id.et_prenom);
        EditText etCin = view.findViewById(R.id.et_cin);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etTelephone = view.findViewById(R.id.et_telephone);
        EditText etCommentaires = view.findViewById(R.id.et_commentaires);
        Button btnSave = view.findViewById(R.id.btn_save_candidature);
        View btnBack = view.findViewById(R.id.btn_back);
        View btnPickCv = view.findViewById(R.id.btn_pick_cv);
        View btnPickLm = view.findViewById(R.id.btn_pick_lm);
        TextView tvCvName = view.findViewById(R.id.tv_cv_name);
        TextView tvLmName = view.findViewById(R.id.tv_lm_name);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        }

        if (tvRecrutementRef != null) {
            tvRecrutementRef.setText("Offre #" + recrutementId);
        }

        pickCvLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                cvUri = uri;
                tvCvName.setText(uri.getLastPathSegment());
            }
        });

        pickLmLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                lmUri = uri;
                tvLmName.setText(uri.getLastPathSegment());
            }
        });

        btnPickCv.setOnClickListener(v -> pickCvLauncher.launch("*/*"));
        btnPickLm.setOnClickListener(v -> pickLmLauncher.launch("*/*"));

        btnSave.setOnClickListener(v -> {
            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String cin = etCin.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenom) || TextUtils.isEmpty(cin) || TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Champs requis manquants", Toast.LENGTH_SHORT).show();
                return;
            }

            CandidatureRecrutement c = new CandidatureRecrutement();
            c.setRecrutementId(recrutementId);
            c.setNom(nom);
            c.setPrenom(prenom);
            c.setCin(cin);
            c.setEmail(email);
            c.setTelephone(etTelephone.getText().toString().trim());
            c.setCommentaires(etCommentaires.getText().toString().trim());
            c.setStatut("EN_ATTENTE");

            btnSave.setEnabled(false);
            uploadCvThenCreate(c, btnSave);
        });
    }

    private void uploadCvThenCreate(CandidatureRecrutement c, Button btnSave) {
        if (cvUri == null) {
            uploadLmThenCreate(c, btnSave);
            return;
        }
        uploadFile(cvUri, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                c.setCvUrl(url);
                uploadLmThenCreate(c, btnSave);
            }

            @Override
            public void onError(Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(requireContext(), "Erreur upload CV: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadLmThenCreate(CandidatureRecrutement c, Button btnSave) {
        if (lmUri == null) {
            createCandidature(c, btnSave);
            return;
        }
        uploadFile(lmUri, new UploadCallback() {
            @Override
            public void onSuccess(String url) {
                c.setLettreMotivationUrl(url);
                createCandidature(c, btnSave);
            }

            @Override
            public void onError(Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(requireContext(), "Erreur upload LM: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createCandidature(CandidatureRecrutement c, Button btnSave) {
        viewModel.create(c, new CandidatureRecrutementViewModel.ActionCallback() {
            @Override
            public void onSuccess(CandidatureRecrutement cr) {
                btnSave.setEnabled(true);
                Toast.makeText(requireContext(), "Candidature ajoutée", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).popBackStack();
            }

            @Override
            public void onError(Throwable t) {
                btnSave.setEnabled(true);
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
