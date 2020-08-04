package redix.soft.anilist.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.databinding.FragmentReviewBinding;
import redix.soft.anilist.model.Review;

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

        //review.setContent(review.getContent().replace("\\n", "\n"));
        reviewBinding.setReview(review);

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    ((MainActivity) getContext()).getToolbar().setElevation(scrollY == 0? 0 : 20);
                }
        );

        ((MainActivity) getContext()).getToolbar().setElevation(0);

        return view;

    }
}
