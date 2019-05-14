package redix.soft.anilist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import redix.soft.anilist.R;
import redix.soft.anilist.databinding.ListSeiyuHeaderBinding;
import redix.soft.anilist.model.Role;
import redix.soft.anilist.databinding.ListRoleBinding;
import redix.soft.anilist.model.Seiyu;


public class RoleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Seiyu seiyu;
    private List<Role> roles;
    private List<Role> allRoles;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public RoleAdapter(Context context){
        this.context = context;
        this.roles = new ArrayList<>();
        this.roles.add(null);
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListSeiyuHeaderBinding mBinding;
        private ItemClickListener listener;

        public ViewHeaderHolder(ListSeiyuHeaderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Seiyu seiyu){
            mBinding.setSeiyu(seiyu);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListRoleBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListRoleBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Role role){
            mBinding.setRole(role);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ViewFooterHolder extends RecyclerView.ViewHolder{

        public ViewFooterHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                hideMoreButton();

                boolean hideMoreButton;
                int rolesToFetch = 50;
                List<Role> rolesToAdd;
                if(hideMoreButton = (roles.size() - 1) + 50 > allRoles.size())
                    rolesToFetch = allRoles.size() - (roles.size() - 1);

                rolesToAdd = allRoles.subList(roles.size(), roles.size() + rolesToFetch);

                addRoles(rolesToAdd);

                if(!hideMoreButton)
                    showMoreButton();
            });
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
        if(viewType == TYPE_HEADER){
            ListSeiyuHeaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_seiyu_header, parent, false);
            return new ViewHeaderHolder(binding);
        }
        else {
            ListRoleBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_role, parent, false);
            return new ViewHolder(binding);
        }
        /*else {
            View view = inflater.inflate(R.layout.list_role_footer, parent, false);
            return new ViewFooterHolder(view);
        }*/
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder)
            ((ViewHolder) holder).bind(roles.get(position - 1));
        else if(holder instanceof ViewHeaderHolder)
            ((ViewHeaderHolder) holder).bind(seiyu);
    }

    @Override
    public int getItemCount() {
        return roles.size() + 1;
    }

    public void setSeiyu(Seiyu seiyu){
        this.seiyu = seiyu;
        this.allRoles = seiyu.getVoiceRoles();
        this.roles = seiyu.getVoiceRoles();

        /*List<Role> roles = seiyu.getVoiceRoles();
        boolean showMore;
        if(showMore = roles.size() > 50)
            roles = roles.subList(0, 50);

        this.roles.add(null);
        this.roles.addAll(roles);*/

        notifyDataSetChanged();

        //if(showMore)
        //    showMoreButton();
    }

    public void addRoles(List<Role> roles){
        int previousSize = this.roles.size();
        this.roles.addAll(roles);
        notifyItemRangeInserted(previousSize, roles.size());
    }

    public void showMoreButton(){
        this.roles.add(null);
        notifyItemInserted(this.roles.size());
    }

    public void hideMoreButton(){
        this.roles.remove(this.roles.size() - 1);
        notifyItemRemoved(this.roles.size());
    }

}
