package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @Insert("insert into category(type,name,sort,status,create_time,update_time,create_user,update_user)"+
            "values"+
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    //更新数据库数据
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    @Select("select * from category where id=#{id}")
    Category getById(long id);

    //删除菜品分类
    @Delete("delete from category where id=#{id}")
    void delete(long id);

    @Select("select * from category where type=#{type}")
    List<Category> getByType(Integer type);
}
