package com.idgindigo.pms.domain.extranet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author vomel
 * @since 04.03.14 15:48
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Document extends BaseEntity {
    public Document(DocType type) {
        this.type = type;
    }

    @NotNull
    @Column(unique = true)
    private String accessKey;

    @NotNull
    private String contentType;

    @Column
    private String fileName;

    @Lob
    @JsonIgnore
    @Size(max = 500000)
    private byte[] fileContent;

    @NotNull
    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private DocType type;

    public enum DocType {DOC, PHOTO, LOGO, SCAN}

}
