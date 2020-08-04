package redix.soft.anilist.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.RoleAdapter;
import redix.soft.anilist.api.JikanService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeiyuFragment extends Fragment {

    public static final String TAG = "Person Details";

    @BindView(R.id.person_list) RecyclerView listPerson;

    private int personId;

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seiyu, container, false);

        ButterKnife.bind(this, view);

        listPerson.setAdapter(new RoleAdapter(getContext()));

        getPerson();

        return view;
    }

    private void getPerson(){
        new JikanService()
                .getPerson(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> ((RoleAdapter) listPerson.getAdapter()).setSeiyu(response),
                        throwable -> {
                            Log.d("ERROR", throwable.getMessage());
                            getPerson();
                        }
                );
    }

}
