package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //新增套餐
    @Override
    public void insert(SetmealVO setmealVO) {
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO, setmeal);
        //执行插入操作
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();//
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.insertList(setmealDishes);
    }

    //启售或者停售
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();

        setmealMapper.update(setmeal);
    }

    //修改套餐
    @Override
    public void update(SetmealVO setmealVO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO, setmeal);
        setmealMapper.update(setmeal);
        List<Long> ids = new ArrayList<>();
        ids.add(setmeal.getId());
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();
        setmealDishMapper.deleteByIds(ids);
        setmealDishMapper.insertList(setmealDishes);
    }

    //删除套餐
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        List<Setmeal> setmealList = setmealMapper.getByIds(ids);
        for (Setmeal setmeal : setmealList) {
            if (setmeal.getStatus() == 1) {
                throw new DeletionNotAllowedException("套餐正在售卖中，无法删除");
            }
        }
        setmealMapper.deleteByIds(ids);
        setmealDishMapper.deleteByIds(ids);
    }

    //根据ID查询套餐
    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);

        Long categoryId = setmeal.getCategoryId();
        Category category = categoryMapper.getById(categoryId);
        String categoryName = category.getName();//获取菜品种类名称

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setCategoryName(categoryName);

        //将套餐下的菜品也查询出来
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }
}
