package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.databinding.ListGenre3Binding;
import redix.soft.anilist.databinding.ListGenreBinding;
import redix.soft.anilist.databinding.ListGenre2Binding;
import redix.soft.anilist.fragment.ListFragment;
import redix.soft.anilist.listener.ItemClickListener;
import redix.soft.anilist.model.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder>{
    
    private List<Genre> genres;
    private Context context;
    private int layout;
    private Genre lastSelectedGenre;
    private int lastSelectedPosition;

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
            if (layout == R.layout.list_genre_3)
                ((ListGenre3Binding) mBinding).setGenre(genre);
            mBinding.executePendingBindings();
        }
    
        public void setItemClickListener(ItemClickListener listener){
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }
    
        @Override
        public void onClick(View view) {
            listener.onItemClick(genres.get(getAdapterPosition()), getAdapterPosition());
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
        holder.bind(genres.get(position));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(Genre genre, int position) {
                if(layout == R.layout.list_genre_3){
                    Fragment currentPage = ((MainActivity) context).navigationUtil.getCurrentPage();
                    if(currentPage instanceof ListFragment){
                        if (((ListFragment) currentPage).getType().equals(ListFragment.TYPES.GENRE)){
                            if(lastSelectedGenre != null && !lastSelectedGenre.equals(genre)){
                                lastSelectedGenre.select(false);
                                notifyItemChanged(lastSelectedPosition);
                            }
                            lastSelectedGenre = genre;
                            lastSelectedPosition = position;
                        }
                    }
                    genre.select(!genre.isSelected());
                    notifyItemChanged(position);
                }
                else if(layout == R.layout.list_genre){
                    ListFragment fragment = new ListFragment();
                    fragment.setType(ListFragment.TYPES.GENRE);
                    fragment.setGenre(genre);

                    ((MainActivity) context).loadFragment(fragment, ListFragment.TAG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(genres == null)
            return 0;
        return genres.size();
    }

    public void setItems(List<Genre> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }

    public List<Genre> getItems(){
        return this.genres;
    }

    public void clear() {
        for(Genre g : genres)
            g.select(false);
        notifyDataSetChanged();
    }

}
