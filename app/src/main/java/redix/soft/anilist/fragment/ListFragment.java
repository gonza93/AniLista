package redix.soft.anilist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.adapter.ThemeAdapter;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Theme;

public class ListFragment extends Fragment{

    public static final String TAG = "ListFragment";
    private String TYPE;

    @BindView(R.id.list) RecyclerView list;

    private ThemeAdapter themeAdapter;

    private Anime anime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);

        ButterKnife.bind(this, view);

        if (TYPE.equals("Themes"))
            populateThemes();

        return view;
    }

    private void populateThemes(){
        List<Theme> themes = new ArrayList<>();

        themes.addAll(parseThemes(anime.getOpeningThemes(), true));
        themes.addAll(parseThemes(anime.getEndingThemes(), false));

        themeAdapter = new ThemeAdapter(themes, getContext());
        themeAdapter.toggleThemes(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(themeAdapter);
    }

    private List<Theme> parseThemes(List<String> themes, boolean isOpening){
        List<Theme> parsedThemes = new ArrayList<>();
        int order = 1;
        for(String line : themes) {

            Theme theme = new Theme();
            theme.setOpening(isOpening);

            theme.setOrder(String.valueOf(order++));
            int titleIndex = line.lastIndexOf("\"") + 1;

            String title = line.substring(0, titleIndex);
            theme.setTitle(title.replace("\"", ""));
            line = line.replace(title, "").substring(1);

            String author;
            if (line.lastIndexOf("(") == -1) {
                theme.setAuthor(line);
            }
            else {
                author = line.substring(0, line.lastIndexOf("(") - 1);
                theme.setAuthor(author);

                line = line.replace(theme.getAuthor(), "").substring(1);
                theme.setEpisodes(line);
            }

            parsedThemes.add(theme);
        }

        return parsedThemes;
    }

    public void toggleThemes(boolean isOpening){
        themeAdapter.toggleThemes(isOpening);
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
}
