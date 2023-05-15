package dropit.exercise.controller;

import dropit.exercise.model.dto.Address;
import dropit.exercise.model.request.AddressRequest;
import dropit.exercise.service.AddressesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AddressesController {

    private final AddressesService addressesService;

    @PostMapping("resolve-address")
    public Address resolveAddress(@RequestBody AddressRequest address) {
        return addressesService.resolveAddress(address);
    }

}
