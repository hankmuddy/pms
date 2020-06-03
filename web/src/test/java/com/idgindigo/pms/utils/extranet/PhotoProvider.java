package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.DocumentRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.idgindigo.pms.domain.extranet.Document.DocType.PHOTO;

/**
 * @author vomel
 * @since 04.03.14 16:28
 */
@Component
public class PhotoProvider extends EntityProvider<Document> {
    @Inject
    private DocumentRepository repository;

    @Override
    public Document createAndFill() {
        Document photo = new Document(PHOTO);
        photo.setAccessKey("key:" + randomString());
        photo.setContentType("contentType:" + randomString());
        return photo;
    }

    @Override
    public BaseRepository<Document> getRepository() {
        return repository;
    }
}
