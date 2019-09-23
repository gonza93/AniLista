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
import redix.soft.anilist.databinding.ListPictureBinding;
import redix.soft.anilist.listener.ItemClickListener;
import redix.soft.anilist.model.Picture;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private List<Picture> pictures;
    private Context context;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;

    public PictureAdapter(List<Picture> pictures, Context context) {
        this.pictures = pictures;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListPictureBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListPictureBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Picture picture) {
            mBinding.setPicture(picture);
            mBinding.executePendingBindings();
        }

        public void setItemClickListener(ItemClickListener listener) {
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) { }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListPictureBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_picture, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(pictures.get(position));
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public List<Picture> addPictures(List<Picture> pictures){
        this.pictures.addAll(pictures);
        notifyDataSetChanged();
        return pictures;
    }

}
