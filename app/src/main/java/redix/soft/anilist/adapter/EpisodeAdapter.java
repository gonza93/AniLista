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
import redix.soft.anilist.databinding.ListEpisodeBinding;
import redix.soft.anilist.listener.ItemClickListener;
import redix.soft.anilist.model.Episode;

public class EpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Episode> episodes;
    private Context context;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;

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
    public int getItemViewType(int position) {
        return episodes.get(position) != null ? TYPE_ITEM : TYPE_PROGRESS;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_ITEM) {
            ListEpisodeBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_episode, parent, false);
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
            viewHolder.bind(episodes.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public void addEpisodes(List<Episode> episodes){
        this.episodes.addAll(episodes);
        notifyDataSetChanged();
    }

    public void startLoad(){
        this.episodes.add(null);
        notifyItemInserted(this.episodes.size());
    }

    public void endLoad(){
        this.episodes.remove(this.episodes.size() - 1);
        notifyItemRemoved(this.episodes.size());
    }
}
