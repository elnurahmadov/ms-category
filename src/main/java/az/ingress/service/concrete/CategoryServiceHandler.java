package az.ingress.service.concrete;

import az.ingress.aspect.ExecutionTracker;
import az.ingress.dao.entity.CategoryEntity;
import az.ingress.dao.repository.CategoryRepository;
import az.ingress.exception.ConflictException;
import az.ingress.exception.NotFoundException;
import az.ingress.model.enums.CategoryStatus;
import az.ingress.model.request.CategoryRequest;
import az.ingress.model.response.CategoryResponse;
import az.ingress.service.abstraction.CategoryService;
import az.ingress.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static az.ingress.exception.ErrorMessage.CATEGORY_HAS_CHILDREN;
import static az.ingress.exception.ErrorMessage.CATEGORY_NOT_FOUND;
import static az.ingress.exception.ErrorMessage.CATEGORY_SLUG_ALREADY_EXISTS;
import static az.ingress.mapper.CategoryMapper.CATEGORY_MAPPER;
import static az.ingress.model.constants.Cache.CACHE_EXPIRATION_HOURS;
import static az.ingress.model.constants.Cache.CATEGORY_CACHE_KEY;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
@ExecutionTracker
public class CategoryServiceHandler implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CacheUtil cacheUtil;

    @Override
    @Transactional
    public void createCategory(CategoryRequest categoryRequest) {

        if (categoryRepository.existsBySlug(categoryRequest.getSlug())) {
            throw new ConflictException(CATEGORY_SLUG_ALREADY_EXISTS, categoryRequest.getSlug());
        }

        var category = CATEGORY_MAPPER.buildCategoryEntity(categoryRequest);

        if (categoryRequest.getParentId() != null) {
            var parent = fetchCategoryIfExist(categoryRequest.getParentId());
            category.setParent(parent);
        }

        categoryRepository.save(category);
        clearAllCaches();
    }

    @Override
    public List<CategoryResponse> getCategories() {

        List<CategoryResponse> response;

        response = cacheUtil.getBucket(CATEGORY_CACHE_KEY);

        if (response != null) {
            return response;
        }

        List<CategoryEntity> categories = categoryRepository.findAll();
        response = CATEGORY_MAPPER.toResponseList(categories);

        cacheUtil.saveToCache(CATEGORY_CACHE_KEY, response, CACHE_EXPIRATION_HOURS, HOURS);

        return response;
    }

    @Override
    public CategoryResponse getCategory(Long id) {
        return CATEGORY_MAPPER.toResponse(fetchCategoryIfExist(id));
    }

    @Override
    @Transactional
    public void updateStatus(Long id, CategoryStatus status) {
        var category = fetchCategoryIfExist(id);
        category.setStatus(status);
    }

    @Override
    public void deleteCategory(Long id) {
        var category = fetchCategoryIfExist(id);

        if (categoryRepository.existsByParentId(id)) {
            throw new ConflictException(CATEGORY_HAS_CHILDREN, id);
        }

        categoryRepository.delete(category);
        clearAllCaches();
    }

    private CategoryEntity fetchCategoryIfExist(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND, id));
    }

    private void clearAllCaches() {
        cacheUtil.deleteKey(CATEGORY_CACHE_KEY);
    }
}
