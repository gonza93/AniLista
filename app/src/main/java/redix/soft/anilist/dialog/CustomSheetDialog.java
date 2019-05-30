package redix.soft.anilist.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import redix.soft.anilist.R;

public class CustomSheetDialog extends BottomSheetDialogFragment {

    static CustomSheetDialog newInstance() {
        return new CustomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_filters, container, false);

        return v;
    }
}
