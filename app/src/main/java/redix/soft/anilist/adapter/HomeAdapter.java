package redix.soft.anilist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import redix.soft.anilist.R;
import redix.soft.anilist.activity.MainActivity;
import redix.soft.anilist.fragment.ListFragment;
import redix.soft.anilist.fragment.SeasonFragment;
import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Genre;
import redix.soft.anilist.model.Seiyu;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private AnimeAdapter scheduleAdapter, airingAdapter, popularAdapter;
    private SeiyuAdapter peopleAdapter;
    private View loaderSchedule, loaderAiring, loaderPeople, loaderPopular;
    private static final int TYPE_SCHEDULE = 0;
    private static final int TYPE_MENU = 1;
    private static final int TYPE_AIRING = 2;
    private static final int TYPE_PEOPLE = 3;
    private static final int TYPE_POPULAR = 4;

    private List<Anime> scheduleAnime;
    private List<Anime> airingAnime;
    private List<Seiyu> topPeople;
    private List<Anime> popularAnime;

    public HomeAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return TYPE_SCHEDULE;
            case 1:
                return TYPE_MENU;
            case 2:
                return TYPE_AIRING;
            case 3:
                return TYPE_PEOPLE;
            case 4:
                return TYPE_POPULAR;

            default:
                return TYPE_POPULAR;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);
        }

        public View findView(int id){
            return itemView.findViewById(id);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resourceId;
        if (viewType == TYPE_SCHEDULE)
            resourceId = R.layout.fragment_home_schedule;
        else if (viewType == TYPE_MENU)
            resourceId = R.layout.fragment_home_menu;
        else if (viewType == TYPE_AIRING)
            resourceId = R.layout.fragment_home_airing;
        else if (viewType == TYPE_PEOPLE)
            resourceId = R.layout.fragment_home_people;
        else
            resourceId = R.layout.fragment_home_popular;

        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_SCHEDULE){
            loaderSchedule = holder.findView(R.id.home_loader_anime_schedule);
            RecyclerView scheduleList = (RecyclerView) holder.findView(R.id.home_anime_schedule_list);

            scheduleAdapter = new AnimeAdapter(scheduleAnime, context, R.layout.list_anime_day);
            scheduleList.setAdapter(scheduleAdapter);

            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(scheduleList);
        }
        if (holder.getItemViewType() == TYPE_MENU){
            holder.findView(R.id.home_ranking).setOnClickListener(view -> {
                ListFragment fragment = new ListFragment();
                fragment.setType(ListFragment.TYPES.RANKING);

                ((MainActivity) context).loadFragment(fragment, ListFragment.TAG);
            });
            holder.findView(R.id.home_upcoming).setOnClickListener(view -> {
                ListFragment fragment = new ListFragment();
                fragment.setType(ListFragment.TYPES.UPCOMING);

                ((MainActivity) context).loadFragment(fragment, ListFragment.TAG);
            });
            holder.findView(R.id.home_season).setOnClickListener(view -> {
                SeasonFragment fragment = new SeasonFragment();

                ((MainActivity) context).loadFragment(fragment, SeasonFragment.TAG);
            });
            holder.findView(R.id.home_genres).setOnClickListener(view -> {
                ListFragment fragment = new ListFragment();
                fragment.setType(ListFragment.TYPES.GENRE);
                fragment.setGenre(new Genre(1, "Action"));

                ((MainActivity) context).loadFragment(fragment, ListFragment.TAG);
            });
        }
        if (holder.getItemViewType() == TYPE_AIRING){
            loaderAiring = holder.findView(R.id.home_loader_airing);
            RecyclerView airingList = (RecyclerView) holder.findView(R.id.home_airing_list);

            airingAdapter = new AnimeAdapter(airingAnime, context, R.layout.list_airing);
            airingList.setAdapter(airingAdapter);
        }
        if (holder.getItemViewType() == TYPE_PEOPLE){
            loaderPeople = holder.findView(R.id.home_loader_seiyu);
            RecyclerView peopleList = (RecyclerView) holder.findView(R.id.home_top_seiyu_list);

            peopleAdapter = new SeiyuAdapter(topPeople, context, R.layout.list_seiyu);
            peopleList.setAdapter(peopleAdapter);
        }
        if (holder.getItemViewType() == TYPE_POPULAR){
            loaderPopular = holder.findView(R.id.home_loader_popular);
            RecyclerView popularList = (RecyclerView) holder.findView(R.id.home_popular_list);

            popularAdapter = new AnimeAdapter(popularAnime, context, R.layout.list_airing);
            popularList.setAdapter(popularAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void setScheduleItems(List<Anime> items){
        scheduleAnime = items;
        if (scheduleAdapter != null) {
            scheduleAdapter.setDataSet(items);
            loaderSchedule.setVisibility(View.GONE);
        }
    }

    public void setAiringItems(List<Anime> items){
        airingAnime = items;
        if (airingAdapter != null) {
            airingAdapter.setDataSet(items);
            loaderAiring.setVisibility(View.GONE);
        }
    }

    public void setPeopleItems(List<Seiyu> items){
        topPeople = items;
        if (peopleAdapter != null) {
            peopleAdapter.setDataSet(items);
            loaderPeople.setVisibility(View.GONE);
        }
    }

    public void setPopularItems(List<Anime> items){
        popularAnime = items;
        if (popularAdapter != null) {
            popularAdapter.setDataSet(items);
            loaderPopular.setVisibility(View.GONE);
        }
    }
}
