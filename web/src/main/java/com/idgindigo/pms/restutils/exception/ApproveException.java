package com.idgindigo.pms.restutils.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 11:19 AM
 */
@Getter
@Setter
public class ApproveException extends RuntimeException {
    public static final String MESSAGE = "error.approve.nonApprovedDependency";

    private List<String> sources;

    public ApproveException(List<String> sources) {
        super(MESSAGE);
        this.sources = sources;
    }
}
