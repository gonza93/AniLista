
package redix.soft.anilista.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListAiringBinding;
import redix.soft.anilista.databinding.ListAnimeBinding;
import redix.soft.anilista.databinding.ListAnimeDayBinding;
import redix.soft.anilista.databinding.ListAnimeGenreBinding;
import redix.soft.anilista.databinding.ListFavoritesBinding;
import redix.soft.anilista.databinding.ListRelatedBinding;
import redix.soft.anilista.databinding.ListRelatedOrderBinding;
import redix.soft.anilista.databinding.ListUserListBinding;
import redix.soft.anilista.fragment.AnimeFragment;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Genre;

public class AnimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Anime> animes;
    private Context context;

    private int layout;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;

    public AnimeAdapter(List<Anime> animes, Context context, int layout){
        this.animes = animes;
        this.context = context;
        this.layout = layout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewDataBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Anime anime) {
            if (layout == R.layout.list_anime)
                ((ListAnimeBinding) mBinding).setAnime(anime);
            if (layout == R.layout.list_airing)
                ((ListAiringBinding) mBinding).setAnime(anime);
            if (layout == R.layout.list_anime_day){
                ((ListAnimeDayBinding) mBinding).setAnime(anime);

                List<Genre> genres = new ArrayList<>();
                if(anime.getGenres().size() >= 3)
                    genres = anime.getGenres().subList(0, 3);

                RecyclerView listGenre = mBinding.getRoot().findViewById(R.id.anime_genre_list);
                GenreAdapter adapter = new GenreAdapter(genres, context, R.layout.list_genre_2);
                listGenre.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                listGenre.setAdapter(adapter);
            }
            if (layout == R.layout.list_related)
                ((ListRelatedBinding) mBinding).setAnime(anime);
            if (layout == R.layout.list_related_order)
                ((ListRelatedOrderBinding) mBinding).setAnime(anime);
            if (layout == R.layout.list_anime_genre)
                ((ListAnimeGenreBinding) mBinding).setAnime(anime);
            if (layout == R.layout.list_favorites)
                ((ListFavoritesBinding) mBinding).setAnime(anime);
            if (layout == R.layout.list_user_list)
                ((ListUserListBinding) mBinding).setAnime(anime);

            mBinding.executePendingBindings();
        }

        public void setItemClickListener(ItemClickListener listener){
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(animes.get(getAdapterPosition()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return animes.get(position) != null? TYPE_ITEM : TYPE_PROGRESS;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, layout, parent, false);
            return new ViewHolder(binding);
        }
        else{
            View view = inflater.inflate(R.layout.list_progress, parent, false);
            return new ProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bind(animes.get(position));
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(Anime anime) {
                    AnimeFragment animeFragment = new AnimeFragment();
                    animeFragment.setAnimeId(anime.getId());

                    ((MainActivity) context).loadFragment(animeFragment, AnimeFragment.TAG);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return animes == null? 0 : animes.size();
    }

    public void setDataSet(List<Anime> anime) {
        this.animes = anime;
        notifyDataSetChanged();
    }

    public void addAnime(Anime anime){
        this.animes.add(anime);
        notifyDataSetChanged();
    }

    public void addAnime(List<Anime> anime){
        this.animes.addAll(anime);
        notifyDataSetChanged();
    }

    public void putAnime(Anime anime){
        this.animes.add(0, anime);
        notifyDataSetChanged();
    }

    public void clear() {
        animes.clear();
        notifyDataSetChanged();
    }

    public void startLoad() {
        this.animes.add(null);
        notifyItemInserted(this.animes.size());
    }

    public void startLoadInverse() {
        this.animes.add(0, null);
        notifyItemInserted(0);
    }

    public void endLoad() {
        this.animes.remove(this.animes.size() - 1);
        notifyItemRemoved(this.animes.size());
    }

    public void endLoadInverse() {
        this.animes.remove(0);
        notifyItemRemoved(0);
    }

}