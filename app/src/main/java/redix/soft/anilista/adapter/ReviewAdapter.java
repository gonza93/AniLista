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
import redix.soft.anilista.databinding.ListReviewBinding;
import redix.soft.anilista.fragment.ReviewFragment;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener{

    private List<Review> reviews;
    private Context context;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS = 1;

    public ReviewAdapter(Context context, List<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    public void setDataSet(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListReviewBinding mBinding;
        private ItemClickListener mListener;

        public ViewHolder(ListReviewBinding binding, ItemClickListener listener) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);

            mBinding = binding;
            mListener = listener;
        }

        public void bind(Review review){
            mBinding.setReview(review);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(reviews.get(getAdapterPosition()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return reviews.get(position) != null ? TYPE_ITEM : TYPE_PROGRESS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_ITEM) {
            ListReviewBinding reviewBinding = DataBindingUtil.inflate(inflater, R.layout.list_review, parent, false);
            return new ViewHolder(reviewBinding, this);
        }
        else { //TYPE_PROGRESS
            View view = inflater.inflate(R.layout.list_progress, parent, false);
            return new ProgressHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bind(reviews.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return reviews != null? reviews.size() : 0;
    }

    public void addReviews(List<Review> reviews){
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public void startLoad(){
        this.reviews.add(null);
        notifyItemInserted(this.reviews.size());
    }

    public void endLoad(){
        this.reviews.remove(this.reviews.size() - 1);
        notifyItemRemoved(this.reviews.size());
    }

    @Override
    public void onItemClick(Review review) {
        ReviewFragment fragment = new ReviewFragment();
        fragment.setReview(review);

        ((MainActivity) context).loadFragment(fragment, ReviewFragment.TAG);
    }
}
