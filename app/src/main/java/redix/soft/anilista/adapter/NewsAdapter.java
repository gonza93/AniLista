package redix.soft.anilista.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import redix.soft.anilista.R;
import redix.soft.anilista.databinding.ListNewsBinding;
import redix.soft.anilista.listener.ItemClickListener;
import redix.soft.anilista.model.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    public List<News> news;
    public Context context;

    public NewsAdapter(List<News> news, Context context){
        this.news = news;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListNewsBinding mBinding;
        private ItemClickListener listener;

        public ViewHolder(ListNewsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(News news) {
            mBinding.setNews(news);
            mBinding.executePendingBindings();
        }

        public void setItemClickListener(ItemClickListener listener){
            mBinding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(news.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListNewsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_news, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(News news) {
                Uri uriNews = Uri.parse(news.getURL());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriNews);
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news != null ? news.size() : 0;
    }

    public void setDataSet(List<News> news){
        this.news = news;
        notifyDataSetChanged();
    }

}
