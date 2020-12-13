package redix.soft.anilista.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilista.R;
import redix.soft.anilista.adapter.ThemePagerAdapter;
import redix.soft.anilista.model.Anime;

public class ThemeFragment extends Fragment {

    public static final String TAG = "Themes";

    @BindView(R.id.user_viewpager) ViewPager viewPager;
    @BindView(R.id.user_list_tabs) SmartTabLayout tabLayout;

    public void setAnime(Anime anime) {
        this.anime = anime;
    }
    private Anime anime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);

        ButterKnife.bind(this, view);

        ThemePagerAdapter pagerAdapter = new ThemePagerAdapter(getContext(), getChildFragmentManager(), anime);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        return view;
    }

}
