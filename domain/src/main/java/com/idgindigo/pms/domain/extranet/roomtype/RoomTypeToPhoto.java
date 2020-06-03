package com.idgindigo.pms.domain.extranet.roomtype;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.domain.extranet.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author vomel
 * @since 28.02.14 15:01
 */

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"roomType_id", "document_id"}))
public class RoomTypeToPhoto extends BaseIdentifiable {
    @NotNull
    @ManyToOne(optional = false)
    private RoomType roomType;

    @NotNull
    @ManyToOne(optional = false)
    private Document document;
}
