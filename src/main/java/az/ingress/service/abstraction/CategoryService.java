package az.ingress.service.abstraction;

import az.ingress.model.criteria.CategoryCriteria;
import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.enums.CategoryStatus;
import az.ingress.model.request.CategoryRequest;
import az.ingress.model.response.CategoryResponse;
import az.ingress.model.response.PageableResponse;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequest categoryRequest);

    List<CategoryResponse> getCategories();

    CategoryResponse getCategory(Long id);

    void updateStatus(Long id, CategoryStatus status);

    void deleteCategory(Long id);

    PageableResponse filterCategories(PageCriteria pageCriteria, CategoryCriteria categoryCriteria);
}
