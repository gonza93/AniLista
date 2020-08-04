package redix.soft.anilist.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.databinding.ListRankingBinding;
import redix.soft.anilist.fragment.AnimeFragment;
import redix.soft.anilist.listener.ItemClickListener;
import redix.soft.anilist.model.Anime;

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Anime> animes;
    private Context context;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;

    public RankingAdapter(List<Anime> animes, Context context) {
        this.animes = animes;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListRankingBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListRankingBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Anime anime) {
            mBinding.setAnime(anime);
            mBinding.executePendingBindings();
        }

        public void setItemClickListener(ItemClickListener listener) {
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) { listener.onItemClick(animes.get(getAdapterPosition())); }
    }

    @Override
    public int getItemViewType(int position) {
        return animes.get(position) != null ? TYPE_ITEM : TYPE_PROGRESS;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_ITEM) {
            ListRankingBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_ranking, parent, false);
            return new ViewHolder(binding);
        }
        else { //TYPE_PROGRESS
            View view = inflater.inflate(R.layout.list_progress, parent, false);
            return new ProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
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
        return animes.size();
    }

    public List<Anime> addAnimes(List<Anime> animes){
        this.animes.addAll(animes);
        notifyDataSetChanged();
        return animes;
    }

    public void startLoad(){
        this.animes.add(null);
        notifyItemInserted(this.animes.size());
    }

    public void endLoad(){
        this.animes.remove(this.animes.size() - 1);
        notifyItemRemoved(this.animes.size());
    }
}
