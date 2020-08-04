package redix.soft.anilist.fragment;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.FragmentPictureBinding;
import redix.soft.anilist.model.Picture;

public class PictureFragment extends Fragment {

    public static final String TAG = "PictureFragment";

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

        return pictureBinding.getRoot();
    }
}
