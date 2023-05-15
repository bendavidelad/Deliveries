package dropit.exercise.model.geoapify;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AddressResult {

    private String type;
    private List<Feature> features;
}
