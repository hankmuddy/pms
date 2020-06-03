package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.CodeDto;
import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.idgindigo.pms.domain.extranet.Document.DocType;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:07 PM
 */
public interface DocumentRepository extends BaseRepository<Document> {

    String DOCS_BY_TYPE = "SELECT new com.idgindigo.pms.domain.extranet.CodeDto(d.accessKey) FROM Document d WHERE d.type = ?1";

    @Query("SELECT CASE WHEN (COUNT(e) > 0) THEN TRUE ELSE FALSE END FROM #{#entityName} e WHERE e.accessKey = ?1")
    boolean existsByKey(String key);

    Document findByAccessKey(String key);

    @Query(DOCS_BY_TYPE)
    List<CodeDto> findAllByType(DocType type);

    @Query("SELECT (d.accessKey) FROM Document d WHERE d.type = ?1")
    List<String> findAccessKeyByType(DocType type);

    @Query(DOCS_BY_TYPE)
    List<CodeDto> findPageByType(DocType type, Pageable pageable);
}
