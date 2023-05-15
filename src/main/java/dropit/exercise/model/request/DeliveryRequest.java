package dropit.exercise.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryRequest {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("timeslotId")
    private String timeSlotId;
}
