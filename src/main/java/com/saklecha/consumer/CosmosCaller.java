//package com.optum.ds.consumer;
//
//import com.azure.cosmos.CosmosClient;
//import com.azure.cosmos.CosmosContainer;
//import com.azure.cosmos.CosmosDatabase;
//import com.azure.cosmos.models.CosmosItemRequestOptions;
//import com.azure.cosmos.models.CosmosItemResponse;
//import com.azure.cosmos.models.PartitionKey;
//import com.azure.cosmos.util.CosmosPagedIterable;
//import com.optum.ds.exception.ValidateRequestException;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CosmosCaller<T> {
//
//    private static final Logger logger = LogManager.getLogger(CosmosCaller.class);
//
//    private CosmosClient cosmosClient;
//    private CosmosDatabase cosmosDb;
//
//    public CosmosCaller(CosmosClient cosmosClient, String dbName){
//        this.cosmosClient = cosmosClient;
//        logger.info("{} initialized. Following databases received : {}", this.getClass().getName(),
//                cosmosClient.readAllDatabases().stream().map(d -> d.getId()).collect(Collectors.toList()) );
//        this.cosmosDb = cosmosClient.getDatabase(dbName);
//        logger.info("Following collections received : {}",
//                this.cosmosDb.readAllContainers().stream().map(d -> d.getId()).collect(Collectors.toList()) );
//    }
//
//    public synchronized  List<T> read(String collectionName, String id, Class<T> clazz) throws ValidateRequestException {
//        CosmosContainer container = this.cosmosDb.getContainer(collectionName);
//        if(container == null){
//            logger.error("Unable to resolve container {} in Cosmos", collectionName);
//            throw new ValidateRequestException("Unable to resolve container in Cosmos : "+ collectionName);
//        }
//        if(id != null){
//            CosmosItemResponse response = container.readItem(id, new PartitionKey("id"), clazz);
//            return Arrays.asList((T) response.getItem());
//        }
//        CosmosPagedIterable iter = container.readAllItems(new PartitionKey("id"), clazz);
//        System.out.println("Iter: "+ iter.toString());
//        List<T> myList = new ArrayList<T>();
//        return myList;
//    }
//
//    public synchronized  void upsert(String collectionName, List<Object> entries){
//        CosmosContainer container = this.cosmosDb.getContainer(collectionName);
//        if(container == null){
//            logger.info("Unable to resolve container {} in Cosmos, creating new collection");
//            this.cosmosDb.createContainerIfNotExists(collectionName, "/_id");
//        }else{
//            logger.info("Container {} resolved in Cosmos", collectionName);
//        }
//        entries.stream().forEach( e-> {
//            container.upsertItem(e);
//        });
//    }
//
//    public synchronized void delete(String collectionName, String id) throws ValidateRequestException {
//        CosmosContainer container = this.cosmosDb.getContainer(collectionName);
//        if(container == null){
//            logger.error("Unable to resolve container {} in Cosmos", collectionName);
//            throw new ValidateRequestException("Unable to resolve container in Cosmos : "+ collectionName);
//        }
//        container.deleteItem(id, new PartitionKey("id"), new CosmosItemRequestOptions());
//    }
//}
