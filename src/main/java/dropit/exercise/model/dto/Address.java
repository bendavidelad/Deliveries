package dropit.exercise.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Address {

    private String street;

    @JsonProperty("address_line1")
    private String line1;

    @JsonProperty("address_line2")
    private String line2;

    private String country;

    private String postcode;
}
