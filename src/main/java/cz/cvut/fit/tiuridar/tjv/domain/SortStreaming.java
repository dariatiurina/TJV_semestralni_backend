package cz.cvut.fit.tiuridar.tjv.domain;

import java.util.Comparator;

public class SortStreaming implements Comparator<StreamingService> {
    public int compare(StreamingService a, StreamingService b) {
        Double sum = (double) 0;
        Double aRating, bRating;
        int count = 0;
        for (Film i : a.getHasFilms()) {
            if (i.getRating() != null) {
                sum += i.getRating();
                count++;
            }
        }
        if (count > 0)
            aRating = sum / count;
        else
            aRating = (double) 0;

        count = 0;
        sum = (double) 0;
        for (Film i : b.getHasFilms()) {
            if (i.getRating() != null) {
                sum += i.getRating();
                count++;
            }
        }
        if (count > 0)
            bRating = sum / count;
        else
            bRating = (double) 0;
        return Double.compare(bRating, aRating);
    }
}
