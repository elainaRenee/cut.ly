package com.github.cutly.rest.repository;

import com.github.cutly.rest.component.DataLoader;
import com.github.cutly.rest.domain.SequenceId;
import com.github.cutly.rest.exception.SequenceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceIdRepositoryImpl implements SequenceIdRepository {

    private static final Logger logger = Logger.getLogger(SequenceIdRepositoryImpl.class);

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public long getNextSequenceId(String key) throws SequenceException {

        // find document with key
        Query query = new Query(Criteria.where("_id").is(key));

        Update update = new Update();
        update.inc("sequence", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        SequenceId sequenceId = mongoOperations.findAndModify(query, update, options, SequenceId.class);

        if (sequenceId == null) {
            logger.error("Unable to get sequence id for key: " + key);
            throw new SequenceException("Unable to get sequence id for key: " + key);
        }

        return sequenceId.getSequence();
    }
}
