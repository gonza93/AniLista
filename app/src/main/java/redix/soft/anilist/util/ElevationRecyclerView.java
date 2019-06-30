package redix.soft.anilist.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import redix.soft.anilist.activity.MainActivity;

public class ElevationRecyclerView extends RecyclerView {

    public ElevationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                ((MainActivity) getContext())
                        .getToolbar()
                        .setElevation(!recyclerView.canScrollVertically(-1) ? 0 : 20);
            }
        });
    }

}
