
package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.databinding.ListAnimeBinding;
import redix.soft.anilist.databinding.ListTrendingBinding;
import redix.soft.anilist.fragment.AnimeFragment;
import redix.soft.anilist.model.Anime;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder>{

    private List<Anime> animes;
    private Context context;

    private int layout;

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
            if(layout == R.layout.list_anime)
                ((ListAnimeBinding) mBinding).setAnime(anime);
            if(layout == R.layout.list_trending)
                ((ListTrendingBinding) mBinding).setAnime(anime);
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
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Anime anime = animes.get(position);
        holder.bind(anime);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(Anime anime) {
                AnimeFragment animeFragment = new AnimeFragment();
                animeFragment.setAnimeId(anime.getId());

                ((MainActivity) context).loadFragment(animeFragment, AnimeFragment.TAG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public void setDataSet(List<Anime> animes) {
        this.animes = animes;
        notifyDataSetChanged();
    }

    public void clear() {
        animes.clear();
        notifyDataSetChanged();
    }

}