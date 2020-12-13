package redix.soft.anilista.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilista.R;
import redix.soft.anilista.activity.FullscreenActivity;
import redix.soft.anilista.databinding.FragmentPictureBinding;
import redix.soft.anilista.model.Picture;

public class PictureFragment extends Fragment {

    public static final String TAG = "PictureFragment";

    @BindView(R.id.picture_layout) View pictureLayout;

    private FragmentPictureBinding pictureBinding;
    private Picture picture;

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pictureBinding = DataBindingUtil.inflate(   inflater,
                                                    R.layout.fragment_picture,
                                                    container,
                                      false);

        pictureBinding.setPicture(picture);
        View view = pictureBinding.getRoot();

        ButterKnife.bind(this, view);

        pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null)
                    ((FullscreenActivity) getContext()).toggle();
            }
        });

        return view;
    }
}
