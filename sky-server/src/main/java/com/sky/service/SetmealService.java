package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void insert(SetmealVO setmealVO);

    void startOrStop(Integer status, Long id);

    void update(SetmealVO setmealVO);

    void deleteByIds(List<Long> ids);

    SetmealVO getById(Long id);
}
