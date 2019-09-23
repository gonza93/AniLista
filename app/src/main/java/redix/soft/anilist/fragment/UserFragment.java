package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.UserAdapter;
import redix.soft.anilist.api.JikanService;
import redix.soft.anilist.util.AnimationUtil;
import redix.soft.anilist.util.DataUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserFragment extends Fragment {

    @BindView(R.id.user_list) RecyclerView listUser;
    @BindView(R.id.user_label) View introLabel;
    @BindView(R.id.user_label_text) TextView labelText;
    @BindView(R.id.user_loader) View userLoader;
    @BindView(R.id.user_intro_icon) ImageView userLabelIcon;

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
        userLoader.setVisibility(View.GONE);

        view.post(() -> fetchUser(DataUtil.getInstance(getContext()).getSavedUsername()));

        return view;
    }

    public void fetchUser(String username) {
        if (username == null) return;
        DataUtil.getInstance(getContext()).saveString(DataUtil.DATA.USERNAME.toString(), username);

        AnimationUtil.expand(userLoader, 300);
        listUser.setVisibility(View.GONE);
        listUser.setAdapter(new UserAdapter(getContext()));

        new JikanService()
                .getUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            AnimationUtil.fadeIn(listUser);
                            introLabel.setVisibility(View.GONE);
                            ((UserAdapter) listUser.getAdapter()).setUser(user);

                            AnimationUtil.collapse(userLoader, 400, 500);
                        },
                        throwable -> {
                            Log.d("ERROR USER", throwable.getMessage());

                            introLabel.setVisibility(View.VISIBLE);
                            labelText.setText(R.string.user_not_found);
                            userLabelIcon.setImageResource(R.mipmap.ic_user_not_found);
                            AnimationUtil.collapse(userLoader, 400, 500);
                        }
                );
    }

}
