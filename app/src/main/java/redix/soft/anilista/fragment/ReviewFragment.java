package redix.soft.anilista.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.FragmentReviewBinding;
import redix.soft.anilista.model.Review;

public class ReviewFragment extends Fragment {

    public static final String TAG = "Review Details";

    @BindView(R.id.review_scroll) NestedScrollView scrollView;

    private FragmentReviewBinding reviewBinding;
    private Review review;

    public void setReview(Review review) {
        this.review = review;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        reviewBinding = DataBindingUtil.inflate( inflater,
                                                 R.layout.fragment_review,
                                                 container,
                                   false);

        View view = reviewBinding.getRoot();
        ButterKnife.bind(this, view);

        reviewBinding.setReview(review);

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    ((MainActivity) getContext()).getToolbar().setElevation(scrollY == 0? 0 : 20);
                }
        );

        ((MainActivity) getContext()).getToolbar().setElevation(0);

        return view;

    }
}
