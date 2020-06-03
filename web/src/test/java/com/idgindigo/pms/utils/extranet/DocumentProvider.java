package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.DocumentRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


/**
 * @author vomel
 * @since 28.02.14 16:09
 */
@Component
public class DocumentProvider extends EntityProvider<Document> {
    @Inject
    private DocumentRepository repository;

    @Override
    public Document createAndFill() {
        Document document = new Document();
        document.setAccessKey("key:" + randomString());
        document.setContentType("contentType:" + randomString());
        document.setFileName("fileName:" + randomString());
        document.setFileContent("FileContent".getBytes());
        return document;
    }

    @Override
    public BaseRepository<Document> getRepository() {
        return repository;
    }
}
