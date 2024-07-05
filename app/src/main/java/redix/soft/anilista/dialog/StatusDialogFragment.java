package redix.soft.anilista.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shawnlin.numberpicker.NumberPicker;

import redix.soft.anilista.R;
import redix.soft.anilista.api.MyAnimeListService;
import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.AnimeStatus;
import redix.soft.anilista.util.DataUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StatusDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ActionStatusBottomSDialog";

    private Anime anime;
    private Spinner spinnerStatus;
    private NumberPicker numberPickerView;
    private EditText editTextEpisodes;
    private View progress;
    private View saveButton, deleteButton;
    private int episodesWatched;
    private int episodesTotal;

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_status, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerStatus = view.findViewById(R.id.dialog_status_status);
        numberPickerView = view.findViewById(R.id.dialog_status_score);
        editTextEpisodes = view.findViewById(R.id.dialog_status_episodes);
        progress = view.findViewById(R.id.dialog_status_progress);
        saveButton = view.findViewById(R.id.dialog_status_save);
        deleteButton = view.findViewById(R.id.dialog_status_delete);

        AnimeStatus animeStatus = anime.getAnimeStatus();

        episodesWatched = animeStatus.getEpisodesWatchedInt();
        episodesTotal = animeStatus.getEpisodes();

        view.findViewById(R.id.dialog_status_save).setOnClickListener(this);
        view.findViewById(R.id.dialog_status_delete).setOnClickListener(this);

        view.findViewById(R.id.dialog_status_up).setOnClickListener(v -> {
            episodesWatched++;
            if (episodesWatched > episodesTotal)
                episodesWatched = episodesTotal;

            editTextEpisodes.setText(String.valueOf(episodesWatched));
        });
        view.findViewById(R.id.dialog_status_down).setOnClickListener(v -> {
            episodesWatched--;
            if (episodesWatched < 0)
                episodesWatched = 0;

            editTextEpisodes.setText(String.valueOf(episodesWatched));
        });

        //Status
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.dialog_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1)
                    episodesWatched = episodesTotal;

                editTextEpisodes.setText(String.valueOf(episodesWatched));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Score
        numberPickerView.setDisplayedValues(getResources().getStringArray(R.array.dialog_status_score_array));
        numberPickerView.setMinValue(0);
        numberPickerView.setMaxValue(10);

        spinnerStatus.setSelection(determineStatusPosition());
        ((TextView) view.findViewById(R.id.dialog_status_episodes_total)).setText("/ " + episodesTotal);
        editTextEpisodes.setText(String.valueOf(episodesWatched));
        numberPickerView.setValue(10 - animeStatus.getScoreInt());
    }

    private int determineStatusPosition() {
        int position;

        switch (anime.getAnimeStatus().getStatus()){
            case "Completed":
                position = 1;
                break;
            case "On hold":
                position = 2;
                break;
            case "Dropped":
                position = 3;
                break;
            case "Plan to watch":
                position = 4;
                break;
            default:
                position = 0;
                break;
        }

        return position;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        showLoader();

        if (v == saveButton) {
            if (DataUtil.getInstance(getContext()).getString(DataUtil.DATA.TOKEN.toString()) == null){
                Toast.makeText(getContext(), "You're not logged in.", Toast.LENGTH_SHORT).show();
                dismiss();
                return;
            }

            if (editTextEpisodes.getText().toString().equals(""))
                editTextEpisodes.setText("0");

            String status = spinnerStatus.getSelectedItem().toString();
            episodesWatched = Integer.parseInt(editTextEpisodes.getText().toString());
            int score = 10 - 1; //numberPickerView.getValue();

            status = status.replace(" ", "_").toLowerCase();

            new MyAnimeListService(getContext())
                    .updateAnimeStatus(anime.getId(), status, score, episodesWatched)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            animeStatus -> {
                                anime.setAnimeStatus(animeStatus);

                                progress.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Anime updated succesfully", Toast.LENGTH_SHORT).show();

                                hideLoader();
                                dismiss();
                            },
                            throwable -> {
                                Log.d("ERROR", throwable.getMessage());
                                Toast.makeText(getContext(), "Error while updating anime status", Toast.LENGTH_SHORT).show();
                                hideLoader();
                            }
                    );
        }

        if (v == deleteButton) {
            new MyAnimeListService(getContext())
                    .deleteAnimeStatus(anime.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            deleted -> {
                                anime.setAnimeStatus(new AnimeStatus());
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Anime deleted succesfully", Toast.LENGTH_SHORT).show();

                                hideLoader();
                                dismiss();
                            },
                            throwable -> {
                                Log.d("ERROR", throwable.getMessage());
                                Toast.makeText(getContext(), "Error while updating anime status", Toast.LENGTH_SHORT).show();
                                hideLoader();
                            }
                    );
        }
    }

    private void showLoader() {
        new Handler().postDelayed(() -> {
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }, 200);
    }

    private void hideLoader() {
        saveButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }
}
