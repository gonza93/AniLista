package redix.soft.anilista.util;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import redix.soft.anilista.activity.MainActivity;

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
