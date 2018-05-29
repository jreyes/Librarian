package librarian.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultDTO {
// ------------------------------ FIELDS ------------------------------

    private List<ItemDTO> items = new ArrayList<>();
    private String kind;
    private Integer totalItems;
}
