package com.portfolio.inverted.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDetails {
    String filePath;
    Integer fileLength;
}
