package kr.njw.odeseoul.common.dto;

import lombok.Data;

@Data
public class PageResponse {
    int page;
    int size;
    int limit;
    int totalPage;
    long totalSize;
}
