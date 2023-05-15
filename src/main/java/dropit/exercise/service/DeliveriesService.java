package dropit.exercise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dropit.exercise.enums.DeliveryStatus;
import dropit.exercise.model.dto.Address;
import dropit.exercise.model.dto.Delivery;
import dropit.exercise.model.reader.Holiday;
import dropit.exercise.model.dto.TimeSlot;
import dropit.exercise.model.reader.HolidaysReader;
import dropit.exercise.model.reader.TimeSlotsReader;
import dropit.exercise.model.request.DeliveryRequest;
import dropit.exercise.util.TimestampUtil;
import dropit.exercise.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class DeliveriesService {

    @Value("classpath:initialization/available_timeslots.json")
    private Resource timeslotsFile;
    @Value("classpath:initialization/holidays.json")
    private Resource holidaysFile;
    private final ObjectMapper objectMapper;
    private List<TimeSlot> timeSlots;
    private List<Holiday> holidays;
    private List<Delivery> deliveries;


    @EventListener(ApplicationReadyEvent.class)
    public void loadResources() {
        try {
            TimeSlotsReader timeSlotsReader = objectMapper.readValue(timeslotsFile.getInputStream(), TimeSlotsReader.class);
            HolidaysReader holidaysReader = objectMapper.readValue(holidaysFile.getInputStream(), HolidaysReader.class);
            holidays = holidaysReader.getHolidays();
            timeSlots = timeSlotsReader.getCourierAvailableTimeSlots().stream().filter(this::isTimeSlotNotInHolidays).collect(Collectors.toList());
            deliveries = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error while loading resources files: " + e.getMessage());
        }
    }

    private boolean isTimeSlotNotInHolidays(TimeSlot timeSlot) {
        List<Timestamp> holidaysDates = holidays.stream().map(Holiday::getDate).toList();
        return !TimestampUtil.isDateInListOfTimestamps(timeSlot.getStartTime(), holidaysDates);
    }

    public List<TimeSlot> getTimeslotsForAddress(Address address) {
        return timeSlots
                .stream()
                .filter(timeslot -> timeslot.getSupportedPostcodes()
                        .contains(address.getPostcode()))
                .collect(Collectors.toList());
    }

    public String bookDelivery(DeliveryRequest deliveryRequest) {
        Optional<TimeSlot> optionalTimeSLot = timeSlots.stream().filter(timeSlot -> Objects.equals(timeSlot.getTimeSlotId(), deliveryRequest.getTimeSlotId())).findAny();
        if (optionalTimeSLot.isPresent()) {
            return bookDelivery(deliveryRequest, optionalTimeSLot.get());
        } else {
            throw new RuntimeException(ErrorMessages.TIMESLOT_ID_INCORRECT);
        }
    }

    private String bookDelivery(DeliveryRequest deliveryRequest, TimeSlot timeSlot) {
        // Using get and increment to handle Concurrent requests, only the first two will be able to book timeslot
        int usages = timeSlot.getUsages().getAndIncrement();
        if (usages < 2) {
            Delivery delivery = new Delivery(deliveryRequest, timeSlot);
            deliveries.add(delivery);
            return delivery.getDeliveryId();
        } else {
            throw new RuntimeException(ErrorMessages.TIMESLOT_NOT_AVAILABLE);
        }
    }

    public void cancelDelivery(String deliveryId) {
        Optional<Delivery> optionalDelivery = deliveries.stream().filter(delivery -> Objects.equals(delivery.getDeliveryId(), deliveryId)).findAny();
        if (optionalDelivery.isEmpty()) {
            throw new RuntimeException(ErrorMessages.DELIVERY_ID_INCORRECT);
        } else {
            Delivery delivery = optionalDelivery.get();
            delivery.cancelDelivery();
            delivery.getTimeSlot().getUsages().decrementAndGet();
        }
    }

    public List<Delivery> getDayDeliveries() {
        Timestamp todayDate = new Timestamp(System.currentTimeMillis());
        return deliveries
                .stream()
                .filter(delivery -> TimestampUtil.isSameDate(delivery.getTimeSlot().getStartTime(), todayDate))
                .filter(delivery -> delivery.getStatus() == DeliveryStatus.VALID)
                .collect(Collectors.toList());
    }

    public List<Delivery> getWeekDeliveries() {
        List<Timestamp> comingWeek = TimestampUtil.buildListOfComingWeekDates();
        return deliveries
                .stream()
                .filter(delivery -> TimestampUtil.isDateInListOfTimestamps(delivery.getTimeSlot().getStartTime(), comingWeek))
                .filter(delivery -> delivery.getStatus() == DeliveryStatus.VALID)
                .collect(Collectors.toList());
    }

}
