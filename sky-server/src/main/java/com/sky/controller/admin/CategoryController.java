package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CatergoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{status}")
    @ApiOperation("启用或者禁用菜品分类")
    public Result StartorStop(@PathVariable Integer status,long id){
        log.info("启用或禁用菜品分类,参数:status={},id={}",status,id);
        catergoryService.updateStatus(status,id);
        return Result.success();
    }
}
