package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CatergoryService {
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void save(CategoryDTO categoryDTO);

    void updateStatus(Integer status, long id);

    Category getById(long id);

    void update(CategoryDTO categoryDTO);

    void delete(long id);

    List<Category> getByType(Integer type);
}
