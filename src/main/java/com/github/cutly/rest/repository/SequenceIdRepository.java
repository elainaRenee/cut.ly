package com.github.cutly.rest.repository;

import com.github.cutly.rest.exception.SequenceException;

public interface SequenceIdRepository {

    long getNextSequenceId(String key) throws SequenceException;

}
