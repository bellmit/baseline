package org.benrush.generator.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benrush.generator.facade.api.GeApi;
import org.benrush.generator.facade.dto.req.GeReqDto;
import org.benrush.generator.facade.dto.res.GeResDto;
import org.benrush.generator.facade.enums.ProjectErrorType;
import org.benrush.generator.facade.common.Pager;
import org.benrush.generator.facade.common.ResultDto;
import org.benrush.generator.server.persistence.po.GePo;
import org.benrush.generator.server.service.defination.GeService;
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
public class GeController implements GeApi {

    private final GeService geService;

    @Autowired
    GeController(GeService geService) {
        this.geService = geService;
    }

    @ApiOperation(value = "根据id查询单个对象")
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<GeResDto> getOneById(@PathVariable String id) {
        GePo gePo = geService.getById(id);
        if(gePo == null) {
            return ResultDto.ok();
        }
        GeResDto geResDto = new GeResDto();
        BeanUtils.copyProperties(gePo, geResDto);
        return ResultDto.ok(geResDto);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Pager<GeResDto>> getPage(@RequestParam(value = "currentPage",defaultValue = "1") Long currentPage,
                                       @RequestParam(value = "pageSize",defaultValue = "10") Long pageSize) {
        Page<GePo> page = geService.page(new Page<>(currentPage,pageSize),new QueryWrapper<>());
        Pager<GeResDto> pager = Pager.convert(page,gePo -> {
            GeResDto geResDto = new GeResDto();
            BeanUtils.copyProperties(gePo,geResDto);
            return geResDto;
        });
        return ResultDto.ok(pager);
    }

    @ApiOperation(value = "新增单个对象")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<GeResDto> addOne(@RequestBody @Valid GeReqDto geReqDto){
        GePo gePo = new GePo();
        BeanUtils.copyProperties(geReqDto,gePo);
        boolean success = geService.save(gePo);
        GeResDto geResDto = new GeResDto();
        BeanUtils.copyProperties(gePo,geResDto);
        return success ? ResultDto.ok(geResDto) : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

    @ApiOperation(value = "新增多个对象")
    @PostMapping(value = "/batch",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<GeResDto>> addBatch(@RequestBody @Valid List<GeReqDto> geReqDtoList){
        List<GePo> gePoList = new ArrayList<>();
        geReqDtoList.forEach(geReqDto -> {
            GePo gePo = new GePo();
            BeanUtils.copyProperties(geReqDto,gePo);
            gePoList.add(gePo);
        });
        boolean success = geService.saveBatch(gePoList);
        List<GeResDto> geResDtoList = new ArrayList<>();
        gePoList.forEach(gePo -> {
            GeResDto geResDto = new GeResDto();
            BeanUtils.copyProperties(gePo,geResDto);
            geResDtoList.add(geResDto);
        });
        return success ? ResultDto.ok(geResDtoList) : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }


    @ApiOperation(value = "删除单个对象")
    @DeleteMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Void> deleteOne(@PathVariable(value = "id") String id){
        return geService.removeById(id) ? ResultDto.ok() : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

    @ApiOperation(value = "删除多个对象")
    @DeleteMapping(value = "/batch/{ids}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Void> deleteBatch(@PathVariable(value = "ids") String ids){
        List<String> idList = new ArrayList<>();
        Arrays.stream(ids.split(",")).forEach(id -> idList.add(id));
        return geService.removeByIds(idList) ? ResultDto.ok() : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

    @ApiOperation(value = "修改单个对象")
    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<Void> updateOne(@PathVariable(value = "id") String id, @RequestBody @Valid GeReqDto geReqDto){
        GePo gePo = new GePo();
        gePo.setId(id);
        BeanUtils.copyProperties(geReqDto,gePo);
        return geService.updateById(gePo) ? ResultDto.ok() : ResultDto.error(ProjectErrorType.DATABASE_ERROR);
    }

}
