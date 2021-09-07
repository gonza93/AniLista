package redix.soft.anilista.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListUserListBinding;
import redix.soft.anilista.fragment.AnimeFragment;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.DataAnime;

public class UserAnimeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<DataAnime> animes;
    private Context context;

    private int layout;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;

    public UserAnimeListAdapter(List<DataAnime> animes, Context context, int layout){
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

        public void bind(DataAnime anime){
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
                public void onItemClick(DataAnime anime) {
                    AnimeFragment animeFragment = new AnimeFragment();
                    animeFragment.setAnimeId(anime.getNode().getId());

                    ((MainActivity) context).loadFragment(animeFragment, AnimeFragment.TAG);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return animes == null? 0 : animes.size();
    }

    public void setDataSet(List<DataAnime> anime) {
        this.animes = anime;
        notifyDataSetChanged();
    }

    public void addAnime(List<DataAnime> anime){
        this.animes.addAll(anime);
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
