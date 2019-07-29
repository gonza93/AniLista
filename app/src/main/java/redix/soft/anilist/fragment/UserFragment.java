package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.UserAdapter;
import redix.soft.anilist.api.JikanService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserFragment extends Fragment {

    @BindView(R.id.user_list) RecyclerView listUser;
    @BindView(R.id.user_progress) ProgressBar progress;

    private static UserFragment instance;
    public static final String TAG = "User Details";

    public static UserFragment getInstance() {
        return instance == null ? instance = new UserFragment() : instance;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ButterKnife.bind(this, view);

        listUser.setAdapter(new UserAdapter(getContext()));
        fetchUser("gonzaakito");

        return view;
    }

    private void fetchUser(String username) {
        new JikanService()
                .getUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            progress.setVisibility(View.GONE);
                            ((UserAdapter) listUser.getAdapter()).setUser(user);
                        },
                        throwable -> Log.d("ERROR", throwable.getMessage())
                );
    }

}
