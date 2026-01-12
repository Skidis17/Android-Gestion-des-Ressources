package ma.ensate.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ma.ensate.myapplication.model.CandidatureRecrutement;
import ma.ensate.myapplication.model.CandidatureScore;
import ma.ensate.myapplication.model.CandidatureScoreRequest;
import ma.ensate.myapplication.model.CandidatureStatusHistory;
import ma.ensate.myapplication.model.Entretien;
import ma.ensate.myapplication.model.EntretienRequest;
import ma.ensate.myapplication.model.EntretienScore;
import ma.ensate.myapplication.model.EntretienScoreRequest;
import ma.ensate.myapplication.model.StatusChangeRequest;
import ma.ensate.myapplication.repository.CandidatureRecrutementRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatureRecrutementViewModel extends ViewModel {
    private final CandidatureRecrutementRepository repository = new CandidatureRecrutementRepository();
    private final MutableLiveData<List<CandidatureRecrutement>> candidatures = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<CandidatureRecrutement> selected = new MutableLiveData<>();
    private final MutableLiveData<List<CandidatureStatusHistory>> history = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<CandidatureScore>> scores = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Entretien>> entretiens = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<EntretienScore>> entretienScores = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<CandidatureRecrutement>> getCandidatures() { return candidatures; }
    public LiveData<CandidatureRecrutement> getSelected() { return selected; }
    public LiveData<List<CandidatureStatusHistory>> getHistory() { return history; }
    public LiveData<List<CandidatureScore>> getScores() { return scores; }
    public LiveData<List<Entretien>> getEntretiens() { return entretiens; }
    public LiveData<List<EntretienScore>> getEntretienScores() { return entretienScores; }

    public void loadByRecrutement(Long recrutementId) {
        repository.getByRecrutement(recrutementId).enqueue(new Callback<List<CandidatureRecrutement>>() {
            @Override
            public void onResponse(Call<List<CandidatureRecrutement>> call, Response<List<CandidatureRecrutement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    candidatures.postValue(response.body());
                } else {
                    candidatures.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CandidatureRecrutement>> call, Throwable t) {
                candidatures.postValue(new ArrayList<>());
            }
        });
    }

    public interface ActionCallback { void onSuccess(CandidatureRecrutement c); void onError(Throwable t); }

    public void create(CandidatureRecrutement c, ActionCallback cb) {
        repository.create(c).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                loadByRecrutement(c.getRecrutementId());
                if (response.isSuccessful() && response.body() != null) cb.onSuccess(response.body()); else cb.onError(new Exception("Create failed"));
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void loadById(Long id) {
        repository.getById(id).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                if (response.isSuccessful()) selected.postValue(response.body());
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                selected.postValue(null);
            }
        });
    }

    public void updateStatus(Long id, String statut, boolean sendEmail, ActionCallback cb) {
        repository.updateStatus(id, statut, sendEmail).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selected.postValue(response.body());
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Update failed"));
                }
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void updateStatusDetail(Long id, StatusChangeRequest request, ActionCallback cb) {
        repository.updateStatusDetail(id, request).enqueue(new Callback<CandidatureRecrutement>() {
            @Override
            public void onResponse(Call<CandidatureRecrutement> call, Response<CandidatureRecrutement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selected.postValue(response.body());
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Update failed"));
                }
            }

            @Override
            public void onFailure(Call<CandidatureRecrutement> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void loadHistory(Long id) {
        repository.getHistory(id).enqueue(new Callback<List<CandidatureStatusHistory>>() {
            @Override
            public void onResponse(Call<List<CandidatureStatusHistory>> call, Response<List<CandidatureStatusHistory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    history.postValue(response.body());
                } else {
                    history.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CandidatureStatusHistory>> call, Throwable t) {
                history.postValue(new ArrayList<>());
            }
        });
    }

    public void loadScores(Long id) {
        repository.getScores(id).enqueue(new Callback<List<CandidatureScore>>() {
            @Override
            public void onResponse(Call<List<CandidatureScore>> call, Response<List<CandidatureScore>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    scores.postValue(response.body());
                } else {
                    scores.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CandidatureScore>> call, Throwable t) {
                scores.postValue(new ArrayList<>());
            }
        });
    }

    public void addScore(Long id, CandidatureScoreRequest request, ScoreCallback cb) {
        repository.addScore(id, request).enqueue(new Callback<CandidatureScore>() {
            @Override
            public void onResponse(Call<CandidatureScore> call, Response<CandidatureScore> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cb.onSuccess(response.body());
                    loadScores(id);
                    loadById(id);
                } else {
                    cb.onError(new Exception("Add score failed"));
                }
            }

            @Override
            public void onFailure(Call<CandidatureScore> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void loadEntretiens(Long id) {
        repository.getEntretiens(id).enqueue(new Callback<List<Entretien>>() {
            @Override
            public void onResponse(Call<List<Entretien>> call, Response<List<Entretien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    entretiens.postValue(response.body());
                } else {
                    entretiens.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Entretien>> call, Throwable t) {
                entretiens.postValue(new ArrayList<>());
            }
        });
    }

    public void createEntretien(Long id, EntretienRequest request, EntretienCallback cb) {
        repository.createEntretien(id, request).enqueue(new Callback<Entretien>() {
            @Override
            public void onResponse(Call<Entretien> call, Response<Entretien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cb.onSuccess(response.body());
                    loadEntretiens(id);
                } else {
                    cb.onError(new Exception("Create interview failed"));
                }
            }

            @Override
            public void onFailure(Call<Entretien> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void loadEntretienScores(Long entretienId) {
        repository.getEntretienScores(entretienId).enqueue(new Callback<List<EntretienScore>>() {
            @Override
            public void onResponse(Call<List<EntretienScore>> call, Response<List<EntretienScore>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    entretienScores.postValue(response.body());
                } else {
                    entretienScores.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<EntretienScore>> call, Throwable t) {
                entretienScores.postValue(new ArrayList<>());
            }
        });
    }

    public void addEntretienScore(Long entretienId, EntretienScoreRequest request, EntretienScoreCallback cb) {
        repository.addEntretienScore(entretienId, request).enqueue(new Callback<EntretienScore>() {
            @Override
            public void onResponse(Call<EntretienScore> call, Response<EntretienScore> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cb.onSuccess(response.body());
                    loadEntretienScores(entretienId);
                } else {
                    cb.onError(new Exception("Add interview score failed"));
                }
            }

            @Override
            public void onFailure(Call<EntretienScore> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public void selectAccepted(Long recrutementId, boolean sendEmail, ListCallback cb) {
        repository.selectAccepted(recrutementId, sendEmail).enqueue(new Callback<List<CandidatureRecrutement>>() {
            @Override
            public void onResponse(Call<List<CandidatureRecrutement>> call, Response<List<CandidatureRecrutement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    candidatures.postValue(response.body());
                    cb.onSuccess(response.body());
                } else {
                    cb.onError(new Exception("Selection failed"));
                }
            }

            @Override
            public void onFailure(Call<List<CandidatureRecrutement>> call, Throwable t) {
                cb.onError(t);
            }
        });
    }

    public interface ListCallback { void onSuccess(List<CandidatureRecrutement> list); void onError(Throwable t); }
    public interface ScoreCallback { void onSuccess(CandidatureScore score); void onError(Throwable t); }
    public interface EntretienCallback { void onSuccess(Entretien entretien); void onError(Throwable t); }
    public interface EntretienScoreCallback { void onSuccess(EntretienScore score); void onError(Throwable t); }
}
