package az.ingress.model.criteria;

import az.ingress.model.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCriteria {
    private String name;
    private ZonedDateTime createdFrom;
    private ZonedDateTime createdTo;
    private CategoryStatus status;
}
