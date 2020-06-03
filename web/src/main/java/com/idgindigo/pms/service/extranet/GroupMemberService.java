package com.idgindigo.pms.service.extranet;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.repository.extranet.person.PersonRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/18/14 10:19 AM
 */
@Service
public class GroupMemberService {

    @Inject
    private PersonRepository personRepository;
    @Inject
    private GroupMemberRepository repository;

    public GroupMember create(GroupMember groupMember) {
        if (groupMember.getPerson().getId() == null) {
            groupMember.setPerson(personRepository.save(groupMember.getPerson()));
        }
        return repository.save(groupMember);
    }
}
