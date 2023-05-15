package dropit.exercise.controller;

import dropit.exercise.model.dto.Address;
import dropit.exercise.model.dto.TimeSlot;
import dropit.exercise.service.DeliveriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeslots")
@RequiredArgsConstructor
public class TimeSlotController {

    private final DeliveriesService deliveriesService;


    @PostMapping("")
    public List<TimeSlot> getDailyTimeslots(@RequestBody Address address) {
        return deliveriesService.getTimeslotsForAddress(address);
    }


}
