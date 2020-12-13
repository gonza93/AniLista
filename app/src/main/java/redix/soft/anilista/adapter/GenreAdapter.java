package redix.soft.anilista.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListGenre3Binding;
import redix.soft.anilista.databinding.ListGenreBinding;
import redix.soft.anilista.databinding.ListGenre2Binding;
import redix.soft.anilista.fragment.ListFragment;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.Genre;

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
