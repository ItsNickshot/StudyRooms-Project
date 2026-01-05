package gr.hua.dit.studyrooms.core.service;

import gr.hua.dit.studyrooms.core.service.model.CreatePersonRequest;

public interface PersonBusinessLogicService {
    Long createPerson(CreatePersonRequest request);
}
