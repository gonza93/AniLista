package redix.soft.anilista.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListCharacterBinding;
import redix.soft.anilista.fragment.ListFragment;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.Character;

public class CharacterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<Character> characters, allCharacters;
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
                ListFragment fragment = new ListFragment();
                fragment.setType(ListFragment.TYPES.CHARACTERS);
                allCharacters.remove(null);
                fragment.setCharacters(allCharacters);

                ((MainActivity) context).loadFragment(fragment, ListFragment.TAG);
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return characters.get(position) == null ? TYPE_FOOTER : TYPE_ITEM;

    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ListCharacterBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_character, parent, false);
            return new ViewHolder(binding);
        }
        else { //Footer
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
            characterHolder.bind(characters.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return characters == null? 0 : characters.size();
    }

    public void setDataSet(List<Character> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    public void clear() {
        characters.clear();
        notifyDataSetChanged();
    }

    public void setAllCharacters(List<Character> allCharacters){
        this.allCharacters = allCharacters;
    }

}
