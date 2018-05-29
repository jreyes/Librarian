package librarian.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {
// ------------------------------ FIELDS ------------------------------

    private String id;
    private String kind;
    private String selfLink;
    private VolumeInfoDTO volumeInfo;
}
