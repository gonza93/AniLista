package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListCharacterBinding;
import redix.soft.anilist.model.Character;
import redix.soft.anilist.model.Seiyu;

public class CharacterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<Character> characters;
    private Context context;

    public CharacterAdapter(List<Character> characters, Context context){
            this.characters = characters;
            this.context = context;
            }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListCharacterBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListCharacterBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Character character) {
            mBinding.setCharacter(character);
            mBinding.executePendingBindings();

            //Get only the japanese voice actor for each character
            ImageView seiyuAvatar = mBinding.getRoot().findViewById(R.id.character_seiyu_avatar);
            for(Seiyu s : character.getSeiyus()){
                if(s.getLanguage().equals("Japanese")) {
                    Picasso.get()
                            .load(s.getImageURL())
                            .into(seiyuAvatar);
                }
            }
        }

        public void setItemClickListener(ItemClickListener listener){
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) { }
    }

    public class ViewFooterHolder extends RecyclerView.ViewHolder{

        public ViewFooterHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                //TODO CharactersFragment
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position == characters.size() - 1 ? TYPE_FOOTER : TYPE_ITEM;

    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ListCharacterBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_character, parent, false);
            return new ViewHolder(binding);
        }
        else{ //Footer
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_character_footer, parent, false);
            return new ViewFooterHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Items
        if(holder instanceof ViewHolder) {
            ViewHolder characterHolder = (ViewHolder) holder;
            Character Character = characters.get(position);
            characterHolder.bind(Character);
        }
    }

    @Override
    public int getItemCount() {
        if(characters == null)
            return 0;
        return characters.size();
    }

    public void setDataSet(List<Character> Characters) {
        this.characters = Characters;
        notifyDataSetChanged();
    }

    public void clear() {
        characters.clear();
        notifyDataSetChanged();
    }

}
