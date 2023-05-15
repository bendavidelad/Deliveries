package dropit.exercise.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimestampUtil {

    public static boolean isSameDate(Timestamp timestamp1, Timestamp timestamp2) {
        LocalDateTime ts1 = timestamp1.toLocalDateTime();
        LocalDateTime ts2 = timestamp2.toLocalDateTime();
        return ts1.getYear() == ts2.getYear()
                && ts1.getMonth() == ts2.getMonth()
                && ts1.getDayOfMonth() == ts2.getDayOfMonth();
    }

    public static boolean isDateInListOfTimestamps(Timestamp timestamp, List<Timestamp> timestampList) {
        return timestampList.stream().anyMatch(currTs -> isSameDate(currTs, timestamp));
    }

    public static List<Timestamp> buildListOfComingWeekDates() {
        Timestamp currDate = new Timestamp(System.currentTimeMillis());
        List<Timestamp> comingWeek = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            comingWeek.add(currDate);
            // 1000 * 60 * 60 * 24 = 1 day
            currDate = new Timestamp(currDate.getTime() + (1000 * 60 * 60 * 24));
        }
        return comingWeek;
    }
}
