package az.ingress.controller;

import az.ingress.model.criteria.CategoryCriteria;
import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.enums.CategoryStatus;
import az.ingress.model.request.CategoryRequest;
import az.ingress.model.response.CategoryResponse;
import az.ingress.model.response.PageableResponse;
import az.ingress.service.abstraction.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@PathVariable Long id, @RequestParam CategoryStatus status) {
        categoryService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/filter")
    public PageableResponse filterCategories(PageCriteria pageCriteria, CategoryCriteria categoryCriteria) {
        return categoryService.filterCategories(pageCriteria, categoryCriteria);
    }
}