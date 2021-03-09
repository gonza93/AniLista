package redix.soft.anilista.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import redix.soft.anilista.R;
import redix.soft.anilista.adapter.UserAdapter;
import redix.soft.anilista.api.JikanService;
import redix.soft.anilista.api.MyAnimeListAuthService;
import redix.soft.anilista.api.MyAnimeListService;
import redix.soft.anilista.util.AnimationUtil;
import redix.soft.anilista.util.CodeChallenge;
import redix.soft.anilista.util.DataUtil;
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

        //view.post(() -> fetchUser(DataUtil.getInstance(getContext()).getSavedUsername()));
        view.post(this::fetchUserV2);

        return view;
    }

    @OnClick(R.id.user_btn_login)
    public void onClickLogin() {
        try {
            String codeVerifier = CodeChallenge.generateCodeVerifier();
            //String codeChallenge = CodeChallenge.generateCodeChallange(codeVerifier);

            DataUtil.getInstance(getContext()).saveString(DataUtil.DATA.VERIFIER.toString(), codeVerifier);

            String authURL = "https://myanimelist.net/v1/oauth2/authorize" +
                    "?response_type=code" +
                    "&client_id=9384b6b211e93a675362739dd10d1ff4" +
                    "&state=xyz" +
                    "&code_challenge=" + codeVerifier +
                    "&code_challenge_method=plain";

            showLoader();

            Uri uriAuthURL = Uri.parse(authURL);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriAuthURL);
            startActivity(browserIntent);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "The device cant log in due to security issues.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void fetchUser(String username) {
        if (username == null) return;
        DataUtil.getInstance(getContext()).saveString(DataUtil.DATA.USERNAME.toString(), username);

        new JikanService()
                .getUser(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> {
                            ((UserAdapter) listUser.getAdapter()).setUser(user);
                            hideLoader(false);
                        },
                        throwable -> {
                            Log.d("ERROR USER", throwable.getMessage());
                            hideLoader(true);
                        }
                );
    }

    public void authenticateUser(String authorizationCode) {
        String verifier = DataUtil.getInstance(getContext()).getString(DataUtil.DATA.VERIFIER.toString());

        new MyAnimeListAuthService()
                .getToken(authorizationCode, verifier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( token -> {
                            token.save(getContext());

                            if (token.getAccessToken() == null){
                                Toast.makeText(getContext(), "Log In error. Try again.", Toast.LENGTH_SHORT).show();
                                hideLoader(true);
                                return;
                            }

                            fetchUserV2();
                        },
                        throwable   -> {
                            Toast.makeText(getContext(), "Log In error. Try again.", Toast.LENGTH_SHORT).show();
                            hideLoader(true);
                        });
    }

    public void fetchUserV2() {
        String token = DataUtil.getInstance(getContext()).getString(DataUtil.DATA.TOKEN.toString());
        if (token == null) return;

        showLoader();

        new MyAnimeListService(getContext())
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( user -> {
                            ((UserAdapter) listUser.getAdapter()).setUser(user);
                            hideLoader(false);
                        },
                        throwable   -> {
                            Toast.makeText(getContext(), "Log In error. Try again.", Toast.LENGTH_SHORT).show();
                            hideLoader(true);
                        });
    }

    public void showLoader() {
        AnimationUtil.expand(userLoader, 300);
        listUser.setVisibility(View.GONE);
        listUser.setAdapter(new UserAdapter(getContext()));
        introLabel.setVisibility(View.GONE);
    }

    public void hideLoader(boolean error) {
        if (!error) {
            AnimationUtil.fadeIn(listUser);
            introLabel.setVisibility(View.GONE);
        }
        else {
            introLabel.setVisibility(View.VISIBLE);
            labelText.setText(R.string.user_not_found);
            userLabelIcon.setImageResource(R.mipmap.ic_user_not_found);
        }

        AnimationUtil.collapse(userLoader, 400, 500);
    }

}
