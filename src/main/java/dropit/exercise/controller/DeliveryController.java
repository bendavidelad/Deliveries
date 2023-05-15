package dropit.exercise.controller;

import dropit.exercise.model.dto.Delivery;
import dropit.exercise.model.request.DeliveryRequest;
import dropit.exercise.service.DeliveriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveriesService deliveriesService;

    @PostMapping("")
    public String bookDelivery(@RequestBody DeliveryRequest deliveryRequest) {
        return deliveriesService.bookDelivery(deliveryRequest);
    }

    @DeleteMapping("/{deliveryId}")
    public void cancelDelivery(@PathVariable String deliveryId) {
        deliveriesService.cancelDelivery(deliveryId);
    }

    @GetMapping("/daily")
    public List<Delivery> getDayDeliveries() {
        return deliveriesService.getDayDeliveries();
    }

    @GetMapping("/weekly")
    public List<Delivery> getWeekDeliveries() {
        return deliveriesService.getWeekDeliveries();
    }


}
