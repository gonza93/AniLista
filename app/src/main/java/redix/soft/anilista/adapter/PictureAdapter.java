package redix.soft.anilista.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListPictureBinding;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.Picture;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> implements ItemClickListener {

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
        private ItemClickListener mListener;

        public ViewHolder(ListPictureBinding binding, ItemClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;

            mBinding.getRoot().setOnClickListener(this);
            mListener = listener;
        }

        public void bind(Picture picture) {
            mBinding.setPicture(picture);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(getAdapterPosition());
        }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListPictureBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_picture, parent, false);
        return new ViewHolder(binding, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(pictures.get(position));
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public void addPictures(List<Picture> pictures){
        this.pictures.addAll(pictures);
        notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        ((MainActivity) context).loadGallery((ArrayList<Picture>) this.pictures, position);
    }
}
