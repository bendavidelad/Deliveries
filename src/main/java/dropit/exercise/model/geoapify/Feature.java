package dropit.exercise.model.geoapify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
public class Feature {

    private String type;

    private HashMap<String, Object> properties = new HashMap<>();
}
