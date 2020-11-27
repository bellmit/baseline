package ${project.groupId}.${project.name}.facade.api;

import io.swagger.annotations.ApiOperation;
import ${project.groupId}.${project.name}.facade.common.Pager;
import ${project.groupId}.${project.name}.facade.common.ResultDto;
import ${project.groupId}.${project.name}.facade.dto.req.${reqName};
import ${project.groupId}.${project.name}.facade.dto.res.${resName};
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 暴露给外部的api接口调用
 * @author: 刘希晨
 * @date: 2020/11/4 14:43
 */

public interface ${apiName} {
    @ApiOperation(value = "根据id查询单个对象")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<${resName}> getOneById(@PathVariable String id);

    @ApiOperation(value = "分页查询")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<Pager<${resName}>> getPage(@RequestParam(value = "currentPage",defaultValue = "1") Long currentPage,
                                              @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize);

    @ApiOperation(value = "新增单个对象")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<${resName}> addOne(@RequestBody @Valid ${reqName} ${baseName}ReqDto);

    @ApiOperation(value = "新增多个对象")
    @PostMapping(value = "/batch",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<List<${resName}>> addBatch(@RequestBody @Valid List<${reqName}> ${baseName}ReqDtoList);

    @ApiOperation(value = "删除单个对象")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<Void> deleteOne(@PathVariable(value = "id") String id);

    @ApiOperation(value = "删除多个对象")
    @DeleteMapping(value = "/batch/{ids}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<Void> deleteBatch(@PathVariable(value = "ids") String ids);

    @ApiOperation(value = "修改单个对象")
    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    ResultDto<Void> updateOne(@PathVariable(value = "id") String id, @RequestBody @Valid ${reqName} ${baseName}ReqDto);
}
