package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 12:55 PM
 */
@Component
public class BankDetailsProvider extends EntityProvider<BankDetails> {
    public static final String ACCOUNT_NUMBER = "12345678900987654321";
    @Inject
    private BankDetailsRepository repository;

    @Override
    public BankDetails createAndFill() {
        BankDetails details = new BankDetails();
        details.setName(randomAlphabeticString());
        details.setPaymentType(BankDetails.PaymentType.CARD);
        details.setAccount(ACCOUNT_NUMBER);
        return details;
    }

    @Override
    public BaseRepository<BankDetails> getRepository() {
        return repository;
    }
}
