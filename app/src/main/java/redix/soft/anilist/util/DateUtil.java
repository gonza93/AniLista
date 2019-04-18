package redix.soft.anilist.util;

import java.util.Calendar;

public class DateUtil {

    public static String getCurrentSeason(){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String season = null;
        switch (month){
            case 1:
            case 2:
                season = "winter";
                break;
            case 3:
                season = day < 20 ? "winter" : "spring";
                break;
            case 4:
            case 5:
                season = "spring";
                break;
            case 6:
                season = day < 21 ? "spring" : "summer";
                break;
            case 7:
            case 8:
                season = "summer";
                break;
            case 9:
                season = day < 22 ? "summer" : "fall";
                break;
            case 10:
            case 11:
                season = "fall";
                break;
            case 12:
                season = day < 21 ? "fall" : "winter";
                break;
        }
        return season;
    }

}
