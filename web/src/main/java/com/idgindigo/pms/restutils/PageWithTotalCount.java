package com.idgindigo.pms.restutils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author vomel
 * @since 29.10.13 15:43
 */
public class PageWithTotalCount extends PageRequest {

    private long totalCount;
    private int offset;

    public PageWithTotalCount(int page, int size) {
        super(page, size);
    }

    public PageWithTotalCount(int page, int size, int offset) {
        super(page, size);
        this.offset = offset;
    }

    public PageWithTotalCount(int page, int size, long totalCount) {
        super(page, size);
        this.totalCount = totalCount;
    }

    public PageWithTotalCount(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public PageWithTotalCount(int page, int size, int offset, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
        this.offset = offset;
    }

    public PageWithTotalCount(int page, int size, Sort.Direction direction, long totalCount, String... properties) {
        super(page, size, direction, properties);
        this.totalCount = totalCount;
    }

    public PageWithTotalCount(int page, int size, Sort sort) {
        super(page, size, sort);
    }

    public PageWithTotalCount(int page, int size, int offset, Sort sort) {
        super(page, size, sort);
        this.offset = offset;
    }

    public PageWithTotalCount(int page, int size, Sort sort, long totalCount) {
        super(page, size, sort);
        this.totalCount = totalCount;
    }

    public PageWithTotalCount(int page, int size, int offset, Sort sort, long totalCount) {
        super(page, size, sort);
        this.totalCount = totalCount;
        this.offset = offset;
    }

    @Override
    public int getOffset() {
        return offset > 0 ? offset : super.getOffset();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
