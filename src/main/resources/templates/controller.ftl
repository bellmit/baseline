package org.benrush.generator.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ${project.groupId}.${project.name}.facade.api.${apiName};
import ${project.groupId}.${project.name}.facade.dto.req.${reqName};
import ${project.groupId}.${project.name}.facade.dto.res.${resName};
import ${project.groupId}.${project.name}.facade.enums.ProjectErrorType;
import ${project.groupId}.${project.name}.facade.common.Pager;
import ${project.groupId}.${project.name}.facade.common.ResultDto;
import ${project.groupId}.${project.name}.server.persistence.po.${poName};
import ${project.groupId}.${project.name}.server.service.defination.${serviceName};
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(tags = "ge表接口")
@RestController
@RequestMapping("/ge")
@Slf4j
public class ${controllerName} implements ${apiName} {

    private final ${serviceName} ${baseName}Service;

    @Autowired
    GeController(${serviceName} ${baseName}Service) {
        this.${baseName}Service = ${baseName}Service;
    }

    @ApiOperation(value = "根据id查询单个对象")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<${resName}> getOneById(@PathVariable String id) {
        ${poName} ${baseName}Po = ${baseName}Service.getById(id);
        if(${baseName}Po == null) {
            return ResultDto.ok();
        }
        ${resName} ${baseName}ResDto = new ${resName}();
        BeanUtils.copyProperties(${baseName}Po, ${baseName}ResDto);
        return ResultDto.ok(${baseName}ResDto);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Pager<${resName}>> getPage(@RequestParam(value = "currentPage",defaultValue = "1") Long currentPage,
                                       @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize) {
        Page<${poName}> page = ${baseName}Service.page(new Page<>(currentPage,pageSize),new QueryWrapper<>());
        Pager<${resName}> pager = Pager.convert(page,${baseName}Po -> {
            ${resName} ${baseName}ResDto = new ${resName}();
            BeanUtils.copyProperties(${baseName}Po,${baseName}ResDto);
            return ${baseName}ResDto;
        });
        return ResultDto.ok(pager);
    }

    @ApiOperation(value = "新增单个对象")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<${resName}> addOne(@RequestBody @Valid ${reqName} geReqDto){
        ${poName} ${baseName}Po = new ${poName}();
        BeanUtils.copyProperties(geReqDto,${baseName}Po);
        boolean success = ${baseName}Service.save(${baseName}Po);
        ${resName} ${baseName}ResDto = new ${resName}();
        BeanUtils.copyProperties(${baseName}Po,${baseName}ResDto);
        return success ? ResultDto.ok(${baseName}ResDto) : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

    @ApiOperation(value = "新增多个对象")
    @PostMapping(value = "/batch",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<${resName}>> addBatch(@RequestBody @Valid List<${reqName}> geReqDtoList){
        List<${poName}> gePoList = new ArrayList<>();
        geReqDtoList.forEach(geReqDto -> {
            ${poName} ${baseName}Po = new ${poName}();
            BeanUtils.copyProperties(geReqDto,${baseName}Po);
            gePoList.add(${baseName}Po);
        });
        boolean success = ${baseName}Service.saveBatch(gePoList);
        List<${resName}> ${baseName}ResDtoList = new ArrayList<>();
        gePoList.forEach(${baseName}Po -> {
            ${resName} ${baseName}ResDto = new ${resName}();
            BeanUtils.copyProperties(${baseName}Po,${baseName}ResDto);
            ${baseName}ResDtoList.add(${baseName}ResDto);
        });
        return success ? ResultDto.ok(${baseName}ResDtoList) : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }


    @ApiOperation(value = "删除单个对象")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Void> deleteOne(@PathVariable(value = "id") String id){
        return ${baseName}Service.removeById(id) ? ResultDto.ok() : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

    @ApiOperation(value = "删除多个对象")
    @DeleteMapping(value = "/batch/{ids}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Void> deleteBatch(@PathVariable(value = "ids") String ids){
        List<String> idList = new ArrayList<>();
        Arrays.stream(ids.split(",")).forEach(id -> idList.add(id));
        return ${baseName}Service.removeByIds(idList) ? ResultDto.ok() : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

    @ApiOperation(value = "修改单个对象")
    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Void> updateOne(@PathVariable(value = "id") String id, @RequestBody @Valid ${reqName} geReqDto){
        ${poName} ${baseName}Po = new ${poName}();
        ${baseName}Po.setId(id);
        BeanUtils.copyProperties(geReqDto,${baseName}Po);
        return ${baseName}Service.updateById(${baseName}Po) ? ResultDto.ok() : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

}
