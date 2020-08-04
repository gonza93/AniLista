package redix.soft.anilist.listener;

import redix.soft.anilist.model.Anime;
import redix.soft.anilist.model.Genre;
import redix.soft.anilist.model.News;
import redix.soft.anilist.model.Picture;
import redix.soft.anilist.model.Review;
import redix.soft.anilist.model.Seiyu;

public abstract interface ItemClickListener {
    default void onItemClick(Anime anime){}
    default void onItemClick(Genre genre, int position){}
    default void onItemClick(News news){}
    default void onItemClick(Seiyu seiyu){}
    default void onItemClick(Review review){}
    default void onItemClick(Picture picture){}
}