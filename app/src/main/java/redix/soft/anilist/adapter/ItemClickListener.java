package redix.soft.anilist.adapter;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Genre;

public abstract class ItemClickListener {
    public void onItemClick(Anime anime){}
    public void onItemClick(Genre genre){}
}