package com.lab1.common.paging;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class SmartPage<T> extends PageImpl<T> {

    public SmartPage(List<T> content, Pageable pageable) {
        super(getPageContent(content, pageable), pageable, content.size());
    }

    private static <T> List<T> getPageContent(List<T> content, Pageable pageable) {
        int start = Math.min((int) pageable.getOffset(), content.size());
        int end = Math.min(start + pageable.getPageSize(), content.size());
        return content.subList(start, end);
    }
}
