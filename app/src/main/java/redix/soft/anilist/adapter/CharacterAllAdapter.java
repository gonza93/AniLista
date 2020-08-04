package redix.soft.anilist.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListCharacterAllBinding;
import redix.soft.anilist.listener.ItemClickListener;
import redix.soft.anilist.model.Character;

public class CharacterAllAdapter extends RecyclerView.Adapter<CharacterAllAdapter.ViewHolder> {

    private List<Character> characters;
    private Context context;

    public CharacterAllAdapter(List<Character> characters, Context context){
        this.characters = characters;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListCharacterAllBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListCharacterAllBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Character character) {
            mBinding.setCharacter(character);
            mBinding.executePendingBindings();

            //Each character seiyu's list
            RecyclerView listSeiyus = mBinding.getRoot().findViewById(R.id.character_seiyu_list);
            SeiyuAdapter adapter = new SeiyuAdapter(character.getSeiyus(), context, R.layout.list_character_seiyu);
            listSeiyus.setLayoutManager(new LinearLayoutManager(context));
            listSeiyus.setAdapter(adapter);
        }

        public void setItemClickListener(ItemClickListener listener){
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) { }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListCharacterAllBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_character_all, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(characters.get(position));
    }

    @Override
    public int getItemCount() {
        return characters == null ? 0 : characters.size();
    }

    public void setDataSet(List<Character> characters){
        this.characters.addAll(characters);
        notifyDataSetChanged();
    }

}
