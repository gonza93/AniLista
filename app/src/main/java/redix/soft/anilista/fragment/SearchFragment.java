package redix.soft.anilista.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilista.R;
import redix.soft.anilista.adapter.AnimeAdapter;
import redix.soft.anilista.api.JikanService;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    private static SearchFragment instance;
    public static final String TAG = "searchFragment";

    @BindView(R.id.search_list) RecyclerView listAnime;
    @BindView(R.id.search_progress) View progress;
    @BindView(R.id.search_not_found) View notFoundView;

    private AnimeAdapter animeAdapter;
    private HashMap<String, String> searchParams;

    public void setQuery(String query) {
        if (!query.isEmpty())
            searchParams.put("q", query);
        else
            searchParams.remove("q");
    }

    public HashMap<String, String> getSearchParams() {
        return searchParams;
    }
    public void clearSearchParams(){
        searchParams.clear();
        searchParams.put("page", "1");
        //searchParams.put("genre", "12");
        //searchParams.put("genre_exclude", "0");
    }
    public void setListPaddingBottom(int padding){
        this.listAnime.setPadding(0, 0, 0, padding);
    }

    public static SearchFragment getInstance(){
        if(instance == null)
            instance = new SearchFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        animeAdapter = new AnimeAdapter(new ArrayList<>(), getContext(), R.layout.list_anime);

        listAnime.setLayoutManager(new LinearLayoutManager(getContext()));
        listAnime.setAdapter(animeAdapter);

        searchParams = new HashMap<>();
        searchParams.put("q", "Air");
        searchParams.put("page", "1");
        //searchParams.put("genre", "12");
        //searchParams.put("genre_exclude", "1");

        searchAnime();

        return view;
    }

    public void searchAnime(){
        animeAdapter.clear();
        progress.setVisibility(View.VISIBLE);
        notFoundView.setVisibility(View.GONE);

        new JikanService()
                .search(searchParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            animeAdapter.setDataSet(response.getAnimes());
                            progress.setVisibility(View.GONE);

                            if (response.getAnimes().size() == 0)
                                notFoundView.setVisibility(View.VISIBLE);
                            },
                        throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                            notFoundView.setVisibility(View.VISIBLE);
                            Log.d("SearchFragment", throwable.getMessage());
                        }
                );
    }

    public void searchManga(){
        searchParams.put("limit", "15");
        progress.setVisibility(View.VISIBLE);

        //TODO searchManga
    }
}
