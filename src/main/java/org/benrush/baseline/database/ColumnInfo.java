package org.benrush.baseline.database;

import lombok.Data;

@Data
public class ColumnInfo {
    private String udtName;
    private String maxLength;
    private String comment;
}
