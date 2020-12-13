package redix.soft.anilista.listener;

import redix.soft.anilista.model.Anime;
import redix.soft.anilista.model.Genre;
import redix.soft.anilista.model.News;
import redix.soft.anilista.model.Review;
import redix.soft.anilista.model.Seiyu;

public abstract interface ItemClickListener {
    default void onItemClick(Anime anime){}
    default void onItemClick(Genre genre, int position){}
    default void onItemClick(News news){}
    default void onItemClick(Seiyu seiyu){}
    default void onItemClick(Review review){}
    default void onItemClick(int position){}
}