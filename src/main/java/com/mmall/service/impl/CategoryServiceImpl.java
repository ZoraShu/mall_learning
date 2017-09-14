package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by yueshu on 8/29/17.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired

    private CategoryMapper categoryMapper;
    public ServerResponse addCategory(String categoryName, Integer patentId) {
        if (patentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("Wrong Category Parameter");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(patentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("Add category success");
        }
        return ServerResponse.createByErrorMessage("Add category failure");
    }

    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("Wrong Category Parameter");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("Update Category Name Success");
        }
        return ServerResponse.createByErrorMessage("Update Category Name Failure");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("Haven't find any children under this category");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }
    //find all product under this category recursively
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }

}
