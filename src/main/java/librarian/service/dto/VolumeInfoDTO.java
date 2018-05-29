package librarian.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfoDTO {
// ------------------------------ FIELDS ------------------------------

    private List<String> authors = new ArrayList<>();
    private ImageLinksDTO imageLinks;
    private String infoLink;
    private String language;
    private String maturityRating;
    private Integer pageCount;
    private String previewLink;
    private String publishedDate;
    private String publisher;
    private String title;
}
