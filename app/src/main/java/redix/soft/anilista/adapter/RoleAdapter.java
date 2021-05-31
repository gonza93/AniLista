package redix.soft.anilista.adapter;

import android.content.Context;

import androidx.annotation.MainThread;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.activity.MainActivity;
import redix.soft.anilista.databinding.ListRoleStaffBinding;
import redix.soft.anilista.databinding.ListRoleStaffLabelBinding;
import redix.soft.anilista.databinding.ListSeiyuHeaderBinding;
import redix.soft.anilista.dialog.InfoDialog;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.Role;
import redix.soft.anilista.databinding.ListRoleBinding;
import redix.soft.anilista.model.Seiyu;


public class RoleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Seiyu seiyu;
    private List<Role> roles;
    private List<Role> rolesBuffer;
    private int qVoiceRoles;
    private int qVoiceRolesBuffer;
    private Context context;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_VOICE = 1;
    private static final int TYPE_STAFF_LABEL = 2;
    private static final int TYPE_STAFF = 3;

    public RoleAdapter(Context context){
        this.context = context;
        this.roles = new ArrayList<>();
        this.rolesBuffer = new ArrayList<>();
        this.roles.add(null);
    }

    public class ViewHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListSeiyuHeaderBinding mBinding;

        public ViewHeaderHolder(ListSeiyuHeaderBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().findViewById(R.id.person_info_layout).setOnClickListener(this);
        }

        public void bind(Seiyu seiyu){
            mBinding.setSeiyu(seiyu);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            InfoDialog infoDialog = new InfoDialog();

            Bundle args = new Bundle();
            args.putString("text", seiyu.getAbout());
            infoDialog.setArguments(args);

            infoDialog.show(((MainActivity) context).getSupportFragmentManager(), InfoDialog.TAG);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ViewDataBinding mBinding;
        private ItemClickListener listener;
        private int mLayout;

        public ViewHolder(ViewDataBinding binding, int layout) {
            super(binding.getRoot());
            mBinding = binding;
            mLayout = layout;
        }

        public void bind(Role role){
            if (mLayout == R.layout.list_role)
                ((ListRoleBinding) mBinding).setRole(role);
            if (mLayout == R.layout.list_role_staff)
                ((ListRoleStaffBinding) mBinding).setRole(role);
            if (mLayout == R.layout.list_role_staff_label)
                ((ListRoleStaffLabelBinding) mBinding).setRole(role);

            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else if (position <= qVoiceRoles)
            return TYPE_VOICE;
        else if (position == qVoiceRoles + 1)
            return TYPE_STAFF_LABEL;
        else
            return TYPE_STAFF;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_HEADER){
            ListSeiyuHeaderBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_seiyu_header, parent, false);
            return new ViewHeaderHolder(binding);
        }
        else if(viewType == TYPE_VOICE) {
            ListRoleBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_role, parent, false);
            return new ViewHolder(binding, R.layout.list_role);
        }
        else if(viewType == TYPE_STAFF_LABEL) {
            ListRoleStaffLabelBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_role_staff_label, parent, false);
            return new ViewHolder(binding, R.layout.list_role_staff_label);
        }
        else {
            ListRoleStaffBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_role_staff, parent, false);
            return new ViewHolder(binding, R.layout.list_role_staff);
        }
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
        this.roles.remove(0);
        this.roles.addAll(seiyu.getVoiceRoles());
        this.roles.addAll(seiyu.getStaffRoles());

        this.rolesBuffer.addAll(seiyu.getVoiceRoles());
        this.rolesBuffer.addAll(seiyu.getStaffRoles());

        this.qVoiceRoles = seiyu.getVoiceRoles().size();
        this.qVoiceRolesBuffer = seiyu.getVoiceRoles().size();

        notifyDataSetChanged();
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

    public void filterRoles(String query) {
        roles.clear();
        qVoiceRoles = 0;
        query = query.toUpperCase();

        if (query.equals("")) {
            roles.addAll(rolesBuffer);
            qVoiceRoles = qVoiceRolesBuffer;
        }
        else {
            for (Role role : rolesBuffer) {
                if (role.getAnime().getTitle().toUpperCase().contains(query)) {
                    roles.add(role);
                    if (role.getPosition() == null)
                        qVoiceRoles++;
                }
            }
        }

        notifyDataSetChanged();
    }

}
