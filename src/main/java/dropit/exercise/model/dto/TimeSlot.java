package dropit.exercise.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
public class TimeSlot {

    @JsonProperty("start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("end_time")
    private Timestamp endTime;

    @JsonProperty("supported_postcodes")
    private List<String> supportedPostcodes;

    private String timeSlotId = UUID.randomUUID().toString();

    @JsonIgnore
    private AtomicInteger usages = new AtomicInteger(0);

    public void reduceUsage(){
        synchronized (this) {
            if (usages.get() == 1){
                usages.set(0);
            } else {
                usages.set(1);
            }
        }
    }
}
