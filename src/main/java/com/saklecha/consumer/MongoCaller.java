package com.saklecha.consumer;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.saklecha.exception.ValidationException;
import com.saklecha.util.Constants;
import com.saklecha.util.ErrorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MongoCaller<T> {

    private static final Logger logger = LogManager.getLogger(MongoCaller.class);

    @Value("${mongodb.connection.string}")
    private String dbUri;

    @Value("${mongodb.database}")
    private String dbName;

    private MongoDatabase database = null;

    @PostConstruct
    public void setUp() {
        MongoClientURI uri = new MongoClientURI(dbUri);
        MongoClient client = new MongoClient(uri);

        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        CodecRegistry fromProvider = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(defaultCodecRegistry, fromProvider);

        database = client.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
    }


    public List<T> read(String collectionName, Map<String, String> queryParam, Class<T> clazz) throws ValidationException {
        MongoCollection<T> collection = this.database.getCollection(collectionName, clazz);
        if(collection == null){
            logger.error("Unable to resolve container {} in Cosmos", collectionName);
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unable to resolve container in Cosmos : "+ collectionName);
        }

        logger.info("reading from {} container, storing data of type:  {}, with following query param: {}", collectionName, clazz, queryParam);

        Document query = getQueryDocument(queryParam);
        FindIterable<T> iterDoc = collection.find(query);
        List<T> result = new ArrayList<T>();
        if(iterDoc != null){
            MongoCursor<T> iter = iterDoc.iterator();
            while(iter.hasNext()){
                result.add(iter.next());
            }
        }
        return result;
    }

    public Map<Integer, String> insert(String collectionName, List<T> input, Class<T> clazz) {
        MongoCollection<T> collection = this.database.getCollection(collectionName, clazz);
        if(collection == null){
            logger.error("Unable to resolve container {} in Cosmos", collectionName);
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unable to resolve container in Cosmos : "+ collectionName);
        }
        Map<Integer, String> result = new ConcurrentHashMap<>();
        int count = 0;
        int successCount = 0;
        for(T i : input){
            try{
                collection.insertOne(i);
                result.put(++count, Constants.SUCCESS);
                ++successCount;
            }catch(Exception ex){
                result.put(++count, "Failed. Reason : " + ex.getMessage());
            }
        }
        logger.info("{} new records added to {}", successCount , collectionName);
        return result;
    }

    public void update(String collectionName, Map<String, String> queryParam, T input, Class<T> clazz) throws ValidationException {
        MongoCollection<T> collection = this.database.getCollection(collectionName, clazz);
        if(collection == null){
            logger.error("Unable to resolve container {} in Cosmos", collectionName);
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unable to resolve container in Cosmos : "+ collectionName);
        }
        Document query = getQueryDocument(queryParam);
        collection.updateMany(query, new Document("$set", input));
    }

    public boolean deleteEntries(String collectionName, Map<String, String> queryParam) throws ValidationException {
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        if(collection == null){
            logger.error("Unable to resolve container {} in Cosmos", collectionName);
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unable to resolve container in Cosmos : "+ collectionName);
        }
        if(!CollectionUtils.isEmpty(queryParam)) {
            Document query = getQueryDocument(queryParam);
            queryParam.entrySet().stream().forEach(e -> query.append(e.getKey(), e.getValue()));
            logger.info("Attempting to delete from {} using query : {}", collectionName, query.toString());
            logger.info("Documents before deletion: {}", collection.countDocuments());
            DeleteResult result = collection.deleteOne(query);
            logger.info("{} Documents deleted, {} documents remaining", result.getDeletedCount(), collection.countDocuments());
            return result.getDeletedCount() > 0;
        }else{
            throw new ValidationException("Unable to resolve unique object to delete");
        }
    }

    private Document getQueryDocument(Map<String, String> queryParam) {
        Document query = new Document();
        if (!CollectionUtils.isEmpty(queryParam)) {
            queryParam.entrySet().stream().forEach(e -> {
                String key = e.getKey();
                Object value = e.getValue();
                if("id".equals(key)){
                    key = "_id";
                    value = new ObjectId(value.toString());
                }
                query.append(key, value);
            });
        }
        return query;
    }

}
