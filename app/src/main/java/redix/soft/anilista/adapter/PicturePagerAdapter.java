package redix.soft.anilista.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import redix.soft.anilista.fragment.PictureFragment;
import redix.soft.anilista.model.Picture;

public class PicturePagerAdapter extends FragmentPagerAdapter {

    private List<Picture> pictures;

    public PicturePagerAdapter(@NonNull FragmentManager fm, List<Picture> pictures) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        PictureFragment fragment = new PictureFragment();
        fragment.setPicture(pictures.get(position));

        return fragment;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

}
