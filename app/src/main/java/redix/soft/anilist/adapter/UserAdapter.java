package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;

import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.databinding.ListUserHeaderBinding;
import redix.soft.anilist.fragment.UserListFragment;
import redix.soft.anilist.model.User;
import redix.soft.anilist.util.DataUtil;

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
        }

        public void saveButtonProps(TextView textSave, SparkButton saveButton, String username){
            textSave.setTextColor(username != null? ContextCompat.getColor(context, R.color.colorRedTextUser) :
                    ContextCompat.getColor(context, android.R.color.white));
            textSave.setText(username != null? R.string.user_save_2 : R.string.user_save_1);

            DataUtil.getInstance(context).saveString(DataUtil.DATA.SAVED_USER.toString(), username);
            saveButton.setChecked(username != null);
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

            if (user != null) {
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
