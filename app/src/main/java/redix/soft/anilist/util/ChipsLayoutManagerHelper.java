package redix.soft.anilist.util;

import android.content.Context;
import android.view.Gravity;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

public class ChipsLayoutManagerHelper {

    public static ChipsLayoutManager build(Context context){
       return ChipsLayoutManager.newBuilder(context)
                .setChildGravity(Gravity.CENTER)
                .setScrollingEnabled(true)
                .setMaxViewsInRow(3)
                .setGravityResolver(position -> Gravity.CENTER)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
    }

}
