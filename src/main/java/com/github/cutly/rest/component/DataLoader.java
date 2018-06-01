package com.github.cutly.rest.component;

import com.github.cutly.rest.domain.SequenceId;
import com.github.cutly.rest.repository.SequenceIdRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static com.github.cutly.rest.constants.Constants.URLS_SEQUENCE_ID;

@Component
public class DataLoader {

    private static final Logger logger = Logger.getLogger(DataLoader.class);

    private final SequenceIdRepository sequenceIdRepository;

    private final MongoOperations mongoOperations;

    @Autowired
    public DataLoader(SequenceIdRepository sequenceIdRepository, MongoOperations mongoOperations) {
        this.sequenceIdRepository = sequenceIdRepository;
        this.mongoOperations = mongoOperations;

        loadData();
    }

    private void loadData() {
        // find document for key
        Query query = new Query(Criteria.where("_id").is(URLS_SEQUENCE_ID));

        if (mongoOperations.find(query, SequenceId.class).size() != 0)
            return;
        logger.debug("Loading initial sequence");
        SequenceId sequenceId = new SequenceId();
        sequenceId.setSequence(0);
        sequenceId.setId(URLS_SEQUENCE_ID);
        mongoOperations.save(sequenceId);
    }
}
