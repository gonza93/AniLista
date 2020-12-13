package redix.soft.anilista.util;

import android.content.Context;
import android.view.Gravity;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

public class ChipsLayoutManagerHelper {

    public static ChipsLayoutManager build(Context context){
       return ChipsLayoutManager.newBuilder(context)
               .setChildGravity(Gravity.CENTER)
               .setScrollingEnabled(true)
               .setGravityResolver(position -> Gravity.CENTER)
               .setOrientation(ChipsLayoutManager.HORIZONTAL)
               .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
               .build();
    }

}
