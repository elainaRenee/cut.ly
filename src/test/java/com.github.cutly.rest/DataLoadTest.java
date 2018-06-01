package com.github.cutly.rest;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.hashids.Hashids;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * loads database with 3,000,000 documents of dummy data
 */
public class DataLoadTest {

    private static MongoClient mongo;

    private static MongoDatabase db;

    @Before
    public void setUp() throws Exception {
        mongo = new MongoClient("localhost", 27017);
        db = mongo.getDatabase("cutlydb");
        db.drop();
    }

    @Test
    public void insertDocuments() {
        // insert sequencing
        MongoCollection sequences = db.getCollection("sequences");
        Document doc = new Document();
        doc.put("_id", "urls");
        doc.put("sequence", 3000000);
        sequences.insertOne(doc);

        Hashids hashids = new Hashids();

        // create 3,000,000 registered urls
        MongoCollection urls = db.getCollection("urls");

        // create indexes
        IndexOptions indexOptions = new IndexOptions().unique(true);
        urls.createIndex(Indexes.ascending("sequenceId"), indexOptions);
        urls.createIndex(Indexes.ascending("hash"), indexOptions);

        List<Document> registeredUrls = new ArrayList<Document>();
        for(long i = 1; i <= 3000000; i++) {
            Document doc2 = new Document()
                    .append("sequenceId", i)
                    .append("accessDates", new ArrayList<Date>())
                    .append("hash", hashids.encode(i))
                    .append("longUrl","https://www.google.com");
            registeredUrls.add(doc2);
        }

        // bulk insert
        urls.insertMany(registeredUrls);
    }

    @After
    public void takeDown() {
        mongo.close();
    }
}
