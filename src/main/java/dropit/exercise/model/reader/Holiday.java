package dropit.exercise.model.reader;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Holiday {

    private String name;
    private Timestamp date;
    private String country;
}
