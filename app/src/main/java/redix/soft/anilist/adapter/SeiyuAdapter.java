package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListCharacterSeiyuBinding;
import redix.soft.anilist.model.Seiyu;

public class SeiyuAdapter extends RecyclerView.Adapter<SeiyuAdapter.ViewHolder> {

    private List<Seiyu> seiyus;
    private Context context;

    public SeiyuAdapter(List<Seiyu> seiyus, Context context){
        this.seiyus = seiyus;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListCharacterSeiyuBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListCharacterSeiyuBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Seiyu seiyu) {
            mBinding.setSeiyu(seiyu);
            mBinding.executePendingBindings();
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
        ListCharacterSeiyuBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_character_seiyu, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Seiyu seiyu = seiyus.get(position);
        holder.bind(seiyu);
    }

    @Override
    public int getItemCount() {
        return seiyus == null ? 0 : seiyus.size();
    }

    public void setDataSet(List<Seiyu> seiyus){
        this.seiyus.addAll(seiyus);
        notifyDataSetChanged();
    }

}
