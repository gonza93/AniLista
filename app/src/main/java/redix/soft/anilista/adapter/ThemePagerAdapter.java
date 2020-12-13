package redix.soft.anilista.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.fragment.ListFragment;
import redix.soft.anilista.model.Anime;

public class ThemePagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Anime anime;

    public ThemePagerAdapter(Context context, FragmentManager fm, Anime anime) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.context = context;
        this.anime = anime;
    }

    @Override
    public Fragment getItem(int position) {
        ListFragment fragment = new ListFragment().withType(ListFragment.TYPES.THEMES);
        fragment.setAnime(anime);

        if (position == 0)
            fragment.setThemeOp(true);
        else
            fragment.setThemeOp(false);

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.openings);
            default:
                return context.getString(R.string.endings);
        }
    }

}
