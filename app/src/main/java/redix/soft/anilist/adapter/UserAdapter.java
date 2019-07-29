package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListUserHeaderBinding;
import redix.soft.anilist.model.User;

public class UserAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private User user;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public UserAdapter(Context context){
        this.context = context;
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListUserHeaderBinding mBinding;
        private ItemClickListener listener;

        public ViewHeaderHolder(ListUserHeaderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(User user){
            mBinding.setUser(user);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListUserHeaderBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListUserHeaderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(User role){
            mBinding.setUser(role);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //if(viewType == TYPE_HEADER){
            ListUserHeaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_user_header, parent, false);
            return new ViewHeaderHolder(binding);
        //}
        //else {
          //  ListUserBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_role, parent, false);
            //return new ViewHolder(binding);
        //}
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        //if(holder instanceof ViewHolder)
            //((ViewHolder) holder).bind(roles.get(position - 1));
        //else
        if(holder instanceof ViewHeaderHolder)
            ((ViewHeaderHolder) holder).bind(user);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setUser(User user){
        this.user = user;
        notifyDataSetChanged();
    }

}
