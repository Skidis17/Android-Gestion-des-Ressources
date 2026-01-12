package ma.ensate.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ma.ensate.myapplication.R;
import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.model.CandidatureScoreRequest;
import ma.ensate.myapplication.model.CandidatureStatusHistory;
import ma.ensate.myapplication.model.Entretien;
import ma.ensate.myapplication.model.EntretienRequest;
import ma.ensate.myapplication.model.EntretienScoreRequest;
import ma.ensate.myapplication.model.StatusChangeRequest;
import ma.ensate.myapplication.network.TokenManager;
import ma.ensate.myapplication.viewmodel.CandidatureRecrutementViewModel;

import java.math.BigDecimal;
import java.util.Calendar;
import android.widget.ArrayAdapter;

public class CandidatureDetailFragment extends Fragment {
    public CandidatureDetailFragment() {
        super(R.layout.fragment_candidature_detail);
    }

    private Long candidatureId;
    private CandidatureRecrutementViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_candidature_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        candidatureId = getArguments() != null ? getArguments().getLong("candidatureId", -1) : -1;
        if (candidatureId == -1) {
            Toast.makeText(requireContext(), "Candidature manquante", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return;
        }

        viewModel = new ViewModelProvider(this).get(CandidatureRecrutementViewModel.class);

        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView tvCin = view.findViewById(R.id.tv_cin);
        TextView tvStatut = view.findViewById(R.id.tv_statut);
        TextView tvCv = view.findViewById(R.id.tv_cv_link);
        TextView tvLm = view.findViewById(R.id.tv_lm_link);
        Button btnAccept = view.findViewById(R.id.btn_accept);
        Button btnRefuse = view.findViewById(R.id.btn_refuse);
        // Removed pending button
        Button btnAddScore = view.findViewById(R.id.btn_add_score);
        Button btnAddEntretien = view.findViewById(R.id.btn_add_entretien);
        Button btnAddEntretienScore = view.findViewById(R.id.btn_add_entretien_score);
        SwitchMaterial swEmail = view.findViewById(R.id.sw_send_email);
        View btnBack = view.findViewById(R.id.btn_back);
        LinearLayout containerScores = view.findViewById(R.id.container_scores);
        LinearLayout containerEntretiens = view.findViewById(R.id.container_entretiens);
        LinearLayout containerHistory = view.findViewById(R.id.container_history);

        btnBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        viewModel.getSelected().observe(getViewLifecycleOwner(), c -> {
            if (c == null) return;
            tvName.setText(c.getPrenom() + " " + c.getNom());
            tvEmail.setText(c.getEmail());
            tvCin.setText("CIN: " + c.getCin());
            tvStatut.setText(formatStatus(c.getStatut()));

            if (c.getCvUrl() != null && !c.getCvUrl().isBlank()) {
                tvCv.setVisibility(View.VISIBLE);
                tvCv.setOnClickListener(v -> openLink(c.getCvUrl()));
            } else {
                tvCv.setVisibility(View.GONE);
            }
            if (c.getLettreMotivationUrl() != null && !c.getLettreMotivationUrl().isBlank()) {
                tvLm.setVisibility(View.VISIBLE);
                tvLm.setOnClickListener(v -> openLink(c.getLettreMotivationUrl()));
            } else {
                tvLm.setVisibility(View.GONE);
            }
        });

        viewModel.getScores().observe(getViewLifecycleOwner(), list -> {
            containerScores.removeAllViews();
            if (list == null || list.isEmpty()) {
                addInfoRow(containerScores, "Aucun score pour le moment");
                return;
            }
            list.forEach(s -> addInfoRow(containerScores,
                    (s.getStage() != null ? s.getStage() : "GENERAL") + " - " +
                            s.getCriterion() + " : " + s.getScore()));
        });

        viewModel.getEntretiens().observe(getViewLifecycleOwner(), list -> {
            containerEntretiens.removeAllViews();
            if (list == null || list.isEmpty()) {
                addInfoRow(containerEntretiens, "Aucun entretien planifié");
                return;
            }
            for (Entretien e : list) {
                String label = e.getType() + " | " + e.getScheduledAt() + " | " + e.getStatus()
                        + (e.getScoreTotal() != null ? " | Score: " + e.getScoreTotal() : "");
                addInfoRow(containerEntretiens, label);
            }
        });

        viewModel.getHistory().observe(getViewLifecycleOwner(), list -> {
            containerHistory.removeAllViews();
            if (list == null || list.isEmpty()) {
                addInfoRow(containerHistory, "Aucun historique");
                return;
            }
            for (CandidatureStatusHistory h : list) {
                String label = (h.getChangedAt() != null ? h.getChangedAt() : "") + " : " +
                        (h.getFromStatus() != null ? h.getFromStatus() : "-") + " -> " + h.getToStatus();
                addInfoRow(containerHistory, label);
            }
        });

        viewModel.loadById(candidatureId);
        viewModel.loadScores(candidatureId);
        viewModel.loadEntretiens(candidatureId);
        viewModel.loadHistory(candidatureId);

        btnAccept.setOnClickListener(v -> updateStatus("RETENU", swEmail.isChecked()));
        btnRefuse.setOnClickListener(v -> updateStatus("REFUSE", false));
        // Removed "Remettre en attente" action

        btnAddScore.setOnClickListener(v -> showAddScoreDialog());
        btnAddEntretien.setOnClickListener(v -> showAddEntretienDialog());
        btnAddEntretienScore.setOnClickListener(v -> showAddEntretienScoreDialog());
    }

    private void updateStatus(String statut, boolean sendEmail) {
        String changedBy = new TokenManager(requireContext()).getEmail();
        StatusChangeRequest request = new StatusChangeRequest(statut, null, sendEmail, changedBy);
        viewModel.updateStatusDetail(candidatureId, request, new CandidatureRecrutementViewModel.ActionCallback() {
            @Override
            public void onSuccess(CandidatureRecrutement c) {
                Toast.makeText(requireContext(), "Statut mis à jour", Toast.LENGTH_SHORT).show();
                viewModel.loadHistory(candidatureId);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private String formatStatus(String statut) {
        if (statut == null || statut.trim().isEmpty()) return "EN ATTENTE";
        return statut.replace('_', ' ');
    }

    private void showAddScoreDialog() {
        LinearLayout layout = buildDialogLayout();
        EditText etStage = buildField(layout, "Stage (ECRIT/ORAL/GENERAL)");
        EditText etCriterion = buildField(layout, "Critère");
        EditText etScore = buildField(layout, "Score");
        EditText etWeight = buildField(layout, "Poids (optionnel)");
        EditText etReviewer = buildField(layout, "Évaluateur (optionnel)");
        EditText etNotes = buildField(layout, "Notes (optionnel)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Ajouter un score")
                .setView(layout)
                .setPositiveButton("Ajouter", (d, w) -> {
                    String stage = valueOrDefault(etStage.getText().toString(), "GENERAL");
                    String criterion = etCriterion.getText().toString();
                    BigDecimal score = toDecimal(etScore.getText().toString());
                    BigDecimal weight = toDecimal(etWeight.getText().toString());
                    String reviewer = etReviewer.getText().toString();
                    String notes = etNotes.getText().toString();
                    if (TextUtils.isEmpty(criterion) || score == null) {
                        Toast.makeText(requireContext(), "Critère et score requis", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    CandidatureScoreRequest req = new CandidatureScoreRequest(stage, criterion, score, weight, reviewer, notes);
                    viewModel.addScore(candidatureId, req, new CandidatureRecrutementViewModel.ScoreCallback() {
                        @Override
                        public void onSuccess(ma.ensate.myapplication.model.CandidatureScore score) {
                            Toast.makeText(requireContext(), "Score ajouté", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showAddEntretienDialog() {
        LinearLayout layout = buildDialogLayout();
        Spinner spType = buildSpinner(layout, new String[]{"RH", "TECH", "MANAGER", "AUTRE"});
        TextView tvDateTime = buildInfoField(layout, "Date/Heure non définie");
        Button btnPickDate = buildButton(layout, "Choisir date/heure");
        Spinner spMode = buildSpinner(layout, new String[]{"Présentiel", "Visio"});
        EditText etLocation = buildField(layout, "Lieu/Lien");
        Spinner spStatus = buildSpinner(layout, new String[]{"PLANIFIE", "TERMINE"});
        EditText etNotes = buildField(layout, "Notes (optionnel)");
        SwitchMaterial swSendEmail = new SwitchMaterial(requireContext());
        swSendEmail.setText("Envoyer email au candidat");
        swSendEmail.setChecked(true);
        layout.addView(swSendEmail);

        Calendar cal = Calendar.getInstance();
        final int[] pickedYear = {cal.get(Calendar.YEAR)};
        final int[] pickedMonth = {cal.get(Calendar.MONTH)};
        final int[] pickedDay = {cal.get(Calendar.DAY_OF_MONTH)};
        final int[] pickedHour = {cal.get(Calendar.HOUR_OF_DAY)};
        final int[] pickedMinute = {cal.get(Calendar.MINUTE)};

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dp = new DatePickerDialog(requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        pickedYear[0] = year;
                        pickedMonth[0] = month;
                        pickedDay[0] = dayOfMonth;
                        TimePickerDialog tp = new TimePickerDialog(requireContext(),
                                (tView, hourOfDay, minute) -> {
                                    pickedHour[0] = hourOfDay;
                                    pickedMinute[0] = minute;
                                    String iso = String.format("%04d-%02d-%02dT%02d:%02d",
                                            pickedYear[0], pickedMonth[0] + 1, pickedDay[0], pickedHour[0], pickedMinute[0]);
                                    tvDateTime.setText(iso);
                                },
                                pickedHour[0], pickedMinute[0], true);
                        tp.show();
                    },
                    pickedYear[0], pickedMonth[0], pickedDay[0]);
            dp.show();
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Planifier un entretien")
                .setView(layout)
                .setPositiveButton("Créer", (d, w) -> {
                    String type = spType.getSelectedItem().toString();
                    String date = tvDateTime.getText().toString();
                    if (TextUtils.isEmpty(type) || date.contains("non définie")) {
                        Toast.makeText(requireContext(), "Type et date requis", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String createdBy = new TokenManager(requireContext()).getEmail();
                    EntretienRequest req = new EntretienRequest(
                            type,
                            date,
                            spMode.getSelectedItem().toString(),
                            etLocation.getText().toString(),
                            spStatus.getSelectedItem().toString(),
                            etNotes.getText().toString(),
                            createdBy,
                            swSendEmail.isChecked()
                    );
                    viewModel.createEntretien(candidatureId, req, new CandidatureRecrutementViewModel.EntretienCallback() {
                        @Override
                        public void onSuccess(Entretien entretien) {
                            Toast.makeText(requireContext(), "Entretien créé", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void showAddEntretienScoreDialog() {
        LinearLayout layout = buildDialogLayout();
        EditText etEntretienId = buildField(layout, "ID Entretien");
        EditText etCriterion = buildField(layout, "Critère");
        EditText etScore = buildField(layout, "Score");
        EditText etWeight = buildField(layout, "Poids (optionnel)");
        EditText etReviewer = buildField(layout, "Évaluateur (optionnel)");
        EditText etNotes = buildField(layout, "Notes (optionnel)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Ajouter score d’entretien")
                .setView(layout)
                .setPositiveButton("Ajouter", (d, w) -> {
                    Long entretienId = parseLong(etEntretienId.getText().toString());
                    BigDecimal score = toDecimal(etScore.getText().toString());
                    if (entretienId == null || TextUtils.isEmpty(etCriterion.getText().toString()) || score == null) {
                        Toast.makeText(requireContext(), "ID, critère et score requis", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EntretienScoreRequest req = new EntretienScoreRequest(
                            etCriterion.getText().toString(),
                            score,
                            toDecimal(etWeight.getText().toString()),
                            etReviewer.getText().toString(),
                            etNotes.getText().toString()
                    );
                    viewModel.addEntretienScore(entretienId, req, new CandidatureRecrutementViewModel.EntretienScoreCallback() {
                        @Override
                        public void onSuccess(ma.ensate.myapplication.model.EntretienScore score) {
                            Toast.makeText(requireContext(), "Score entretien ajouté", Toast.LENGTH_SHORT).show();
                            viewModel.loadEntretiens(candidatureId);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(requireContext(), "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private LinearLayout buildDialogLayout() {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (12 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);
        return layout;
    }

    private EditText buildField(LinearLayout parent, String hint) {
        EditText field = new EditText(requireContext());
        field.setHint(hint);
        parent.addView(field);
        return field;
    }

    private Spinner buildSpinner(LinearLayout parent, String[] items) {
        Spinner spinner = new Spinner(requireContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        parent.addView(spinner);
        return spinner;
    }

    private Button buildButton(LinearLayout parent, String text) {
        Button button = new Button(requireContext());
        button.setText(text);
        parent.addView(button);
        return button;
    }

    private TextView buildInfoField(LinearLayout parent, String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(0xFF374151);
        tv.setPadding(0, 8, 0, 8);
        parent.addView(tv);
        return tv;
    }

    private void addInfoRow(LinearLayout parent, String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setTextColor(0xFF374151);
        tv.setPadding(0, 4, 0, 4);
        parent.addView(tv);
    }

    private BigDecimal toDecimal(String value) {
        if (TextUtils.isEmpty(value)) return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Long parseLong(String value) {
        if (TextUtils.isEmpty(value)) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String valueOrDefault(String value, String fallback) {
        return TextUtils.isEmpty(value) ? fallback : value;
    }
}
