package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    //新增菜品数据
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);


    List<Dish> selectByIds(List<Long> ids);

    List<Long> getSetmealDishIds(List<Long> ids);

    //根据id删除菜品数据

    void deleteByIds(List<Long> ids);

    //根据id查询菜品数据
    @Select("select * from dish where id = #{id}")
    Dish selectById(Long id);

    //根据id查询菜品分类名称
    @Select("select name from category where id = #{categoryId}")
    String getCategoryNameById(Long categoryId);

    //根据id更新菜品数据
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from dish where category_id = #{categoryId}")
    List<DishVO> getByCategoryId(Long categoryId);
}
