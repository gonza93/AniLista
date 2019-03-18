package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListGenreBinding;
import redix.soft.anilist.databinding.ListGenre2Binding;
import redix.soft.anilist.model.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder>{
    
    private List<Genre> genres;
    private Context context;
    private int layout;

    public GenreAdapter(List<Genre> genres, Context context, int layout){
        this.genres = genres;
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
    
        public void bind(Genre genre) {
            if (layout == R.layout.list_genre)
                ((ListGenreBinding) mBinding).setGenre(genre);
            if (layout == R.layout.list_genre_2)
                ((ListGenre2Binding) mBinding).setGenre(genre);
            mBinding.executePendingBindings();
        }
    
        public void setItemClickListener(ItemClickListener listener){
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }
    
        @Override
        public void onClick(View view) {
            listener.onItemClick(genres.get(getAdapterPosition()));
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
        Genre genre = genres.get(position);
        holder.bind(genre);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(Genre genre) {
                super.onItemClick(genre);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(genres == null)
            return 0;
        return genres.size();
    }

    public void setDataSet(List<Genre> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }

    public void clear() {
        genres.clear();
        notifyDataSetChanged();
    }

}
