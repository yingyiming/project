package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CatergoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/category")
@Api(tags="菜品分类管理")
public class CategoryController {
    @Autowired
    private CatergoryService catergoryService;

    //分页查询分类表
    @GetMapping("/page")
    @ApiOperation("分页查询菜品分类")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询分类表,参数:{}",categoryPageQueryDTO);
        PageResult pageResult= catergoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    //现在菜品分类
    @PostMapping()
    @ApiOperation("新增菜品分类")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增菜品分类,参数:{}",categoryDTO);
        catergoryService.save(categoryDTO);
        return Result.success();
    }

    //启用或禁用菜品分类
    @PostMapping("/status/{status}")
    @ApiOperation("启用或者禁用菜品分类")
    public Result StartorStop(@PathVariable Integer status,long id){
        log.info("启用或禁用菜品分类,参数:status={},id={}",status,id);
        catergoryService.updateStatus(status,id);
        return Result.success();
    }


    //根据ID查询菜品分类
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品分类(也用于编辑的回显操作)")
    public Result<Category> getCategoryById(@PathVariable long id){
        log.info("根据ID查询菜品分类,参数:{}",id);
        Category category=catergoryService.getById(id);
        return Result.success(category);
    }


    //修改菜品分类
    @PutMapping
    @ApiOperation("修改菜品分类")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("修改菜品分类,参数:{}",categoryDTO);
        catergoryService.update(categoryDTO);
        return Result.success();
    }

    //删除菜品分类
    @DeleteMapping()
    @ApiOperation("删除菜品分类")
    public Result deleteCategory(@RequestParam long id){
        log.info("删除菜品分类,参数:{}",id);
        catergoryService.delete(id);
        return Result.success();
    }

    //根据type查询分类表
    @GetMapping("/list")
    @ApiOperation("根据type查询分类表")
    public Result<List> getByType(@RequestParam Integer type){
        log.info("根据type查询分类表,参数:{}",type);
        List<Category> categoryList=catergoryService.getByType(type);
        return Result.success(categoryList);
    }
}
