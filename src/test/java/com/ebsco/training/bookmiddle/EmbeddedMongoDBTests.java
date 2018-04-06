package com.ebsco.training.bookmiddle;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.util.UUID;

import de.flapdoodle.embed.mongo.distribution.Version;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmbeddedMongoDBTests {

    private static final String BOOK_COLLECTION_NAME = "books";

    private static final String BOOK1_AUTHOR = "George R.R. Martin";
    private static final String BOOK2_AUTHOR = "Janna Yeshanova";
    private static final String BOOK3_AUTHOR = "Stieg Larsson";

    private static final String BOOK1_TITLE = "A Game Of Thrones";
    private static final String BOOK2_TITLE = "Love Is Never Past Tense";
    private static final String BOOK3_TITLE = "The Girl with the Dragon Tattoo";
    private static final String BOOK4_TITLE = "The Winds Of Winter";

    private static final String BOOK1_GENRE = "Fantasy";
    private static final String BOOK2_GENRE = "Romance";
    private static final String BOOK3_GENRE = "Mistery";

    protected MongodForTestsFactory factory;
    protected MongoClient mongo;

    @Before
    public void setup() throws Exception {
        factory = MongodForTestsFactory.with(Version.Main.PRODUCTION);
        mongo = factory.newMongo();
    }

    @After
    public void teardown() throws Exception {
        if (factory != null)
            factory.shutdown();
    }

    @Test
    public void shouldPersistAndFindBooks() {

        MongoDatabase db = mongo.getDatabase("EmbeddedMongoDB-" + UUID.randomUUID());
        db.createCollection(BOOK_COLLECTION_NAME);

        MongoIterable<String> collectionNames = db.listCollectionNames();
        assertThat(collectionNames, hasItem(BOOK_COLLECTION_NAME));

        Document book1 = new Document();
        book1.put("title", BOOK1_TITLE);
        book1.put("author", BOOK1_AUTHOR);
        book1.put("genre", BOOK1_GENRE);

        Document book2 = new Document();
        book2.put("title", BOOK2_TITLE);
        book2.put("author", BOOK2_AUTHOR);
        book2.put("genre", BOOK2_GENRE);

        Document book3 = new Document();
        book3.put("title", BOOK3_TITLE);
        book3.put("author", BOOK3_AUTHOR);
        book3.put("genre", BOOK3_GENRE);

        Document book4 = new Document();
        book4.put("title", BOOK4_TITLE);
        book4.put("author", BOOK1_AUTHOR);
        book4.put("genre", BOOK1_GENRE);

        db.getCollection(BOOK_COLLECTION_NAME).insertOne(book1);
        db.getCollection(BOOK_COLLECTION_NAME).insertOne(book2);
        db.getCollection(BOOK_COLLECTION_NAME).insertOne(book3);
        db.getCollection(BOOK_COLLECTION_NAME).insertOne(book4);

        assertThat(db.getCollection(BOOK_COLLECTION_NAME).count(), equalTo(4l));
    }
}