package com.sample.database.service;

import com.sample.model.SampleRequestModel;
import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.model.vault.EncryptOptions;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SampleDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(SampleDatabaseService.class);

    private static String DATABASE_NAME = "lc-db";
    private static String COLLECTION_NAME = "sample-coll";
    public static final String MONGODB_CONNECTION_URI = "mongodb://localhost:27017";

    private ClientEncryption clientEncryption;
    private MongoClient mongoClient;

    private static final int SIZE_MASTER_KEY = 96;
    private static String KEY_VAULT_NAMESPACE = "admin.datakeys";
    private static String KEY_ALT_NAME = "LcKey";
    private static final String DETERMINISTIC = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    private static final String RANDOM = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";

    private byte[] generateMasterKey() {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        new SecureRandom().nextBytes(masterKey);
        logger.info("generatedMasterKey - " + masterKey);
        return masterKey;
    }

    private Map<String, Map<String, Object>> createKMSProvidersMap() {
        Map<String, Map<String, Object>> kmsProviders =
                new HashMap<String, Map<String, Object>>() {
                    {
                        put(
                                "local",
                                new HashMap<String, Object>() {
                                    {
                                        put("key", generateMasterKey());
                                    }
                                });
                    }
                };
        logger.info("createKMSProvidersMap - " + kmsProviders);
        return kmsProviders;
    }

    private MongoClient getClientEncryptionMongoClient() {
        try {
            ConnectionString connectionString = new ConnectionString(MONGODB_CONNECTION_URI);

            MongoClientSettings clientSettings =
                    MongoClientSettings.builder()
                            .applyConnectionString(connectionString)
                            .writeConcern(WriteConcern.ACKNOWLEDGED)
                            .readConcern(ReadConcern.MAJORITY)
                            .build();

            ClientEncryptionSettings clientEncryptionSettings =
                    ClientEncryptionSettings.builder()
                            .keyVaultMongoClientSettings(clientSettings)
                            .keyVaultNamespace(KEY_VAULT_NAMESPACE)
                            .kmsProviders(createKMSProvidersMap())
                            .build();

            this.clientEncryption = ClientEncryptions.create(clientEncryptionSettings);

            createDataEncryptionKeys();
            mongoClient = MongoClients.create(clientSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongoClient;
    }

    private MongoClient getAutoEncryptionMongoClient() {
        MongoClient mongoClient = null;

        try {
            ConnectionString connectionString = new ConnectionString(MONGODB_CONNECTION_URI);

            AutoEncryptionSettings autoEncryptionSettings =
                    AutoEncryptionSettings.builder()
                            .keyVaultNamespace(KEY_VAULT_NAMESPACE)
                            .kmsProviders(createKMSProvidersMap())
                            .build();

            MongoClientSettings clientSettings =
                    MongoClientSettings.builder()
                            // .applyConnectionString(connectionString)
                            .autoEncryptionSettings(autoEncryptionSettings)
                            .build();

            mongoClient = MongoClients.create(clientSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongoClient;
    }

    public void saveEncryptedField() {
        MongoCollection<Document> mongoCollection =
                getAutoEncryptionMongoClient()
                        .getDatabase(DATABASE_NAME)
                        .getCollection(COLLECTION_NAME);
        // mongoCollection.drop();

        mongoCollection.insertOne(new Document("encryptedField", "123456789"));

        logger.info(mongoCollection.find().first().toJson());
    }

    public void saveSampleRequest(SampleRequestModel sampleRequestModel) {
        MongoClient mongoClient = getClientEncryptionMongoClient();
        Document sampleDocument = new Document();
        sampleDocument.append("name", sampleRequestModel.getName());
        sampleDocument.append("age", sampleRequestModel.getAge());

        BsonBinary companyBsonBinary =
                clientEncryption.encrypt(
                        new BsonString(sampleRequestModel.getCompany()),
                        new EncryptOptions(DETERMINISTIC).keyAltName(KEY_ALT_NAME));
        BsonBinary mobileBsonBinary =
                clientEncryption.encrypt(
                        new BsonString(sampleRequestModel.getMobile()),
                        new EncryptOptions(RANDOM).keyAltName(KEY_ALT_NAME));
        //		sampleDocument.put("company", sampleRequestModel.getCompany());
        //		sampleDocument.put("mobile", sampleRequestModel.getMobile());

        sampleDocument.append("company", companyBsonBinary);
        sampleDocument.append("mobile", mobileBsonBinary);
        mongoClient
                .getDatabase(DATABASE_NAME)
                .getCollection(COLLECTION_NAME)
                .insertOne(sampleDocument);
    }

    public Document getSavedSampleRequest() {
        Document document = null;
        // MongoClient mongoClient = getClientEncryptionMongoClient();
        MongoCursor<Document> mongoCursor =
                mongoClient
                        .getDatabase(DATABASE_NAME)
                        .getCollection(COLLECTION_NAME)
                        .find()
                        .iterator();
        if (mongoCursor.hasNext()) {
            document = mongoCursor.next();
        }

        return document;
    }

    private BsonBinary createDataEncryptionKeys() {
        List<String> keyAltNameList = new ArrayList<String>();
        keyAltNameList.add(KEY_ALT_NAME);
        BsonBinary randomKeyId =
                clientEncryption.createDataKey(
                        "local", new DataKeyOptions().keyAltNames(keyAltNameList));
        logger.info("createDataEncryptionKeys - " + randomKeyId);
        return randomKeyId;
    }
}
