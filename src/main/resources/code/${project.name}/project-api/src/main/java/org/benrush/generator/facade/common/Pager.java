package org.benrush.generator.facade.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
public class Pager<T> {
    private Long currentPage;
    private Long totalPage;
    private Long pageSize;
    private Long totalCount;
    private List<T> data;

    public static <T,E> Pager<T> convert(Page<E> page, Function<E,T> function){
        Pager<T> pager = new Pager<>();
        pager.setCurrentPage(page.getCurrent());
        pager.setTotalPage(page.getPages());
        pager.setPageSize(page.getSize());
        pager.setTotalCount(page.getTotal());
        List<T> pagerData = new ArrayList<>();
        for(E item : page.getRecords()){
            pagerData.add(function.apply(item));
        }
        pager.setData(pagerData);
        return pager;
    }



}
