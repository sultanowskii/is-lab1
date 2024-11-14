package com.lab1.common.paging;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Data;

@Data
public class Paginator implements Pageable {
    private final int pageNumber;
    private final int pageSize;
    private final Sort sortParameters;

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return (long) pageNumber * pageSize;
    }

    @Override
    public Sort getSort() {
        return sortParameters;
    }

    @Override
    public Pageable next() {
        return new Paginator(pageNumber + 1, pageSize, sortParameters);
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new Paginator(pageNumber - 1, pageSize, sortParameters) : this;
    }

    @Override
    public Pageable first() {
        return new Paginator(0, pageSize, sortParameters);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new Paginator(pageNumber, this.pageSize, sortParameters);
    }

    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }
}
