package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListEpisodeBinding;
import redix.soft.anilist.model.Episode;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private List<Episode> episodes;
    private Context context;

    public EpisodeAdapter(List<Episode> episodes, Context context) {
        this.episodes = episodes;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListEpisodeBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListEpisodeBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Episode episode) {
            mBinding.setEpisode(episode);
            mBinding.executePendingBindings();
        }

        public void setItemClickListener(ItemClickListener listener) {
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) { }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListEpisodeBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_episode, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }
    
}
