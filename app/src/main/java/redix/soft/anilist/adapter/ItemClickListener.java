package redix.soft.anilist.adapter;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Genre;
import redix.soft.anilist.model.News;

public abstract class ItemClickListener {
    public void onItemClick(Anime anime){}
    public void onItemClick(Genre genre){}
    public void onItemClick(News news){}
}