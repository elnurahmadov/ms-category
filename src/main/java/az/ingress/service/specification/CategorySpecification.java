package az.ingress.service.specification;

import az.ingress.dao.entity.CategoryEntity;
import az.ingress.dao.entity.CategoryEntity.Fields;
import az.ingress.model.criteria.CategoryCriteria;
import az.ingress.model.enums.CategoryStatus;
import az.ingress.util.PredicateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor(staticName = "of")
@Data
public class CategorySpecification implements Specification<CategoryEntity> {

    private CategoryCriteria categoryCriteria;

    @Override
    public Predicate toPredicate(@NonNull Root<CategoryEntity> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder cb) {
        var predicates = PredicateUtil.builder()
                .addNullSafety(
                        categoryCriteria.getName(),
                        name -> cb.like(
                                root.get(Fields.name),
                                applyLikePattern(name)
                        )
                )
                .addNullSafety(
                        categoryCriteria.getCreatedFrom(),
                        createdFrom -> cb.greaterThanOrEqualTo(
                                root.get(Fields.createdAt),
                                createdFrom
                        )
                )
                .addNullSafety(
                        categoryCriteria.getCreatedTo(),
                        createdTo -> cb.lessThanOrEqualTo(
                                root.get(Fields.createdAt),
                                createdTo
                        )
                )
                .add(
                        CategoryStatus.INACTIVE,
                        status -> cb.notEqual(
                                root.get(Fields.status),
                                status
                        )
                )
                .build();

        return cb.and(predicates);
    }

    private String applyLikePattern(String data) {
        return "%" + data + "%";
    }
}
