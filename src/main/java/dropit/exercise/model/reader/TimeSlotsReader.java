package dropit.exercise.model.reader;

import com.fasterxml.jackson.annotation.JsonProperty;
import dropit.exercise.model.dto.TimeSlot;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TimeSlotsReader {

    @JsonProperty("courier_available_timeslots")
    List<TimeSlot> courierAvailableTimeSlots;
}
