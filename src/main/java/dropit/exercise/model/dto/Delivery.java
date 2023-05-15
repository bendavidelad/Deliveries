package dropit.exercise.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dropit.exercise.enums.DeliveryStatus;
import dropit.exercise.model.request.DeliveryRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Delivery {

    public Delivery(DeliveryRequest deliveryRequest, TimeSlot requestTimeSlot) {
        userId = deliveryRequest.getUserId();
        timeSlot = requestTimeSlot;
        deliveryId = UUID.randomUUID().toString();
        status = DeliveryStatus.VALID;
    }

    @JsonIgnore
    private String deliveryId;

    @JsonIgnore
    private String userId;

    private DeliveryStatus status;

    private TimeSlot timeSlot;

    public void cancelDelivery(){
        status = DeliveryStatus.CANCELLED;
        timeSlot.reduceUsage();
    }
}
