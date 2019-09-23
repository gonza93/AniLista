package redix.soft.anilist.listener;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Genre;
import redix.soft.anilist.model.News;
import redix.soft.anilist.model.Seiyu;

public abstract class ItemClickListener {
    public void onItemClick(Anime anime){}
    public void onItemClick(Genre genre, int position){}
    public void onItemClick(News news){}
    public void onItemClick(Seiyu seiyu){}
}