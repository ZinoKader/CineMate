package main.helpers;

import javafx.scene.control.TreeItem;
import main.model.Episode;
import main.model.Season;
import main.model.SeriesDetail;
import main.model.TvType;

import java.util.Comparator;

/**
 * Custom comparator for determining how seasons and episodes should be sorted (ascending)
 */
public class SeriesDetailComparator implements Comparator<TreeItem<SeriesDetail>> {

    @Override
    public int compare(TreeItem<SeriesDetail> o1, TreeItem<SeriesDetail> o2) {

        if(o1.getValue().getType().equals(TvType.SEASON) && o2.getValue().getType().equals(TvType.SEASON)) {
            Season season = (Season) o1.getValue();
            Season otherSeason = (Season) o2.getValue();
            return Integer.compare(season.getSeasonNumber(), otherSeason.getSeasonNumber());
        }
        else if(o1.getValue().getType().equals(TvType.EPISODE) && o2.getValue().getType().equals(TvType.EPISODE)) {
            Episode episode = (Episode) o1.getValue();
            Episode otherEpisode = (Episode) o2.getValue();
            return Integer.compare(episode.getEpisodeNumber(), otherEpisode.getEpisodeNumber());
        }
        else {
            return -1;
        }

    }
}
