package redix.soft.anilista.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListUserHeaderBinding;
import redix.soft.anilista.fragment.UserListFragment;
import redix.soft.anilista.model.User;

public class UserAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private User user;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public UserAdapter(Context context){
        this.context = context;
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder {

        private ListUserHeaderBinding mBinding;

        public ViewHeaderHolder(ListUserHeaderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(User user){
            mBinding.setUser(user);
            mBinding.executePendingBindings();

            mBinding.getRoot().findViewById(R.id.user_btn_anime_list).setOnClickListener(view -> {
                UserListFragment fragment = new UserListFragment();
                fragment.setUsername(user.getUsername());

                ((MainActivity) context).loadFragment(fragment, UserListFragment.TAG);
            });

            mBinding.getRoot().findViewById(R.id.user_btn_manga_list).setOnClickListener(view -> {
                Toast.makeText(context, R.string.manga_module, Toast.LENGTH_SHORT).show();
            });

            mBinding.getRoot().findViewById(R.id.user_btn_anime_stats).setOnClickListener(view -> {
                Toast.makeText(context, R.string.stats_module, Toast.LENGTH_SHORT).show();
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view){
            super(view);
        }

        public RecyclerView findRecyclerView(int id){
            return itemView.findViewById(id);
        }

        public View findView(int id) { return itemView.findViewById(id); }

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_HEADER){
            ListUserHeaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_user_header, parent, false);
            return new ViewHeaderHolder(binding);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_favorites, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHeaderHolder)
            ((ViewHeaderHolder) holder).bind(user);
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;

            if (user != null && user.getFavorites() != null) {
                RecyclerView listAnime = viewHolder.findRecyclerView(R.id.user_list_favorite_anime);
                listAnime.setAdapter(new AnimeAdapter(user.getFavorites().getAnime(), context, R.layout.list_favorites));

                RecyclerView listManga = viewHolder.findRecyclerView(R.id.user_list_favorite_manga);
                listManga.setAdapter(new AnimeAdapter(user.getFavorites().getManga(), context, R.layout.list_favorites));

                RecyclerView listCharacters = viewHolder.findRecyclerView(R.id.user_list_favorite_characters);
                listCharacters.setAdapter(new CharacterAdapter(user.getFavorites().getCharacters(), context));

                RecyclerView listPeople = viewHolder.findRecyclerView(R.id.user_list_favorite_people);
                listPeople.setAdapter(new SeiyuAdapter(user.getFavorites().getPeople(), context, R.layout.list_seiyu));
            }
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void setUser(User user){
        this.user = user;
        notifyDataSetChanged();
    }

}
