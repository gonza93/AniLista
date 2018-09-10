package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListThemeBinding;
import redix.soft.anilist.model.Theme;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    private List<Theme> themes;
    private List<Theme> themesToDisplay = new ArrayList<>();
    private Context context;

    public ThemeAdapter(List<Theme> themes, Context context) {
        this.themes = themes;
        this.themesToDisplay.addAll(themes);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListThemeBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListThemeBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Theme theme) {
            mBinding.setTheme(theme);
            mBinding.executePendingBindings();
        }

        public void setItemClickListener(ItemClickListener listener) {
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) { }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListThemeBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_theme, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theme theme = themesToDisplay.get(position);
        holder.bind(theme);
    }

    @Override
    public int getItemCount() {
        return themesToDisplay.size();
    }

    public void toggleThemes(boolean isOpening){
        themesToDisplay.clear();
        for(Theme theme : themes){
            if(theme.isOpening() == isOpening)
                themesToDisplay.add(theme);
        }
        notifyDataSetChanged();
    }

}