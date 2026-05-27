package az.ingress.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse {
    private List<CategoryResponse> categories;
    private int lastPageNumber;
    private long totalElements;
    private boolean hasNextPage;
}
