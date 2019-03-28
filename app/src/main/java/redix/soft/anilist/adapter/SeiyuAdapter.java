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
import redix.soft.anilist.databinding.ListCharacterSeiyuBinding;
import redix.soft.anilist.databinding.ListSeiyuBinding;
import redix.soft.anilist.model.Seiyu;

public class SeiyuAdapter extends RecyclerView.Adapter<SeiyuAdapter.ViewHolder> {

    private List<Seiyu> seiyus;
    private Context context;
    private int layout;

    public SeiyuAdapter(List<Seiyu> seiyus, Context context, int layout){
        this.seiyus = seiyus;
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

        public void bind(Seiyu seiyu) {
            if(layout == R.layout.list_character_seiyu)
                ((ListCharacterSeiyuBinding) mBinding).setSeiyu(seiyu);
            if(layout == R.layout.list_seiyu)
                ((ListSeiyuBinding) mBinding).setSeiyu(seiyu);

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
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layout, parent, false);
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
