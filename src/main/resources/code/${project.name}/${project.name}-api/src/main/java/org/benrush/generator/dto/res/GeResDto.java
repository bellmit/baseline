package org.benrush.dto.res;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GeResDto {
    private String id;

    private String v;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
