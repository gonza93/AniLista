package redix.soft.anilista.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import butterknife.BindView;
import redix.soft.anilista.R;
import redix.soft.anilista.util.DataUtil;

public class InfoDialog extends DialogFragment {

    public static final String TAG = "InfoDialog";
    public int layout;

    public void setLayout(int layout) {
        this.layout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == 0)
            layout = R.layout.dialog_info;

        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String info = "No info available.";
        boolean isSeiyuInfo = false;
        Bundle args = getArguments();

        if (args != null) {
            info = args.getString("text");
            isSeiyuInfo = args.getBoolean("seiyu");
        }

        CheckBox infoCheck = view.findViewById(R.id.dialog_info_check);
        TextView infoText = view.findViewById(R.id.dialog_info_text);
        View infoYes = view.findViewById(R.id.dialog_info_yes);
        View infoClose = view.findViewById(R.id.dialog_info_close);

        infoText.setText(info);
        if (!isSeiyuInfo) {
            infoCheck.setOnClickListener(v -> {
                if (infoCheck.isChecked())
                    DataUtil.getInstance(getContext()).saveBoolean(
                            DataUtil.DATA.DONTSHOW.toString(),
                            true
                    );
            });

            infoYes.setOnClickListener(v -> {
                String presentationUrl = getString(R.string.support_link);
                Uri uriAuthURL = Uri.parse(presentationUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriAuthURL);
                startActivity(browserIntent);
            });

            infoClose.setOnClickListener(v -> this.dismiss());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }
}