package com.idgindigo.pms.web.controller;

/**
 * @author vomel
 * @since 29.10.13 15:42
 */

import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import org.springframework.data.domain.Pageable;

public class ResponseEntity<T> {

    private T content;
    private Pageable page = new PageWithTotalCount(1, 1, 1L);
    private Boolean success = true;

    public ResponseEntity() {
    }

    public ResponseEntity(T content) {
        this.content = content;
    }

    public ResponseEntity(T content, Pageable page) {

        this.content = content;
        this.page = page;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Pageable getPage() {
        return page;
    }

    public void setPage(Pageable page) {
        this.page = page;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("content", content)
                .toString();
    }


}
