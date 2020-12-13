package redix.soft.anilista.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import redix.soft.anilista.R;
import redix.soft.anilista.util.DataUtil;

public class BottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = "ActionBottomDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_config, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.config_night).setOnClickListener(this);
        view.findViewById(R.id.config_day).setOnClickListener(this);
        view.findViewById(R.id.config_system).setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
       int mode = AppCompatDelegate.MODE_NIGHT_NO;
       switch (view.getId()){
           case R.id.config_night:
               mode = AppCompatDelegate.MODE_NIGHT_YES;
               break;
           case R.id.config_system:
               mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
               break;
       }
       AppCompatDelegate.setDefaultNightMode(mode);
       DataUtil.getInstance(getContext()).saveInt(DataUtil.DATA.MODE.toString(), mode);

       dismiss();
    }

}