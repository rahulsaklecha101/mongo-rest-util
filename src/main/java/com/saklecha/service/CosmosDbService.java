package com.saklecha.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saklecha.consumer.EntityType;
import com.saklecha.consumer.MongoCaller;
import com.saklecha.exception.ValidationException;
import com.saklecha.response.BOGenericResponse;
import com.saklecha.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CosmosDbService {

    private static final Logger logger = LogManager.getLogger(CosmosDbService.class);

    @Autowired
    private MongoCaller mongoCaller;

    private ObjectMapper mapper;

    @PostConstruct
    public void init(){
        mapper = new ObjectMapper();
    }

    public BOGenericResponse getEntries(String collection, Map<String, String> queryParam) {
        EntityType type = null;
        if (collection == null || (type = EntityType.valueOf(collection)) == null) {
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unsupported or incorrect Collection name");
        }

        logger.info("reading from {} container, storing data of type:  {}, with following query param: {}",
                type.getCollection(), type.getEntityType(), queryParam);
        List<Object> data = mongoCaller.read(type.getCollection(), queryParam, type.getEntityType());
        if (CollectionUtils.isEmpty(data)) {
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_NO_CONTENT, "No Records available");
        }

        BOGenericResponse response = new BOGenericResponse();
        response.setData(data);

        return response;
    }

    public BOGenericResponse updateEntries(String collection, Map<String, String> queryParam, List<Object> entities){
        EntityType type = null;
        if(collection == null || (type = EntityType.valueOf(collection)) == null){
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unsupported or incorrect Collection name");
        }
        if(CollectionUtils.isEmpty(entities)){
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_NO_UPDATE,
                    "Data cannot be null");
        }

        Object input = map(entities.get(0), type.getEntityType());
        mongoCaller.update(type.getCollection(), queryParam,  input , type.getEntityType());

        BOGenericResponse response = new BOGenericResponse();
        response.setMessage("Updated successfully");

        return response;
    }

    public BOGenericResponse insertEntries(String collection, List<Object> entities){
        EntityType type = null;
        if(collection == null || (type = EntityType.valueOf(collection)) == null){
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unsupported or incorrect Collection name");
        }
        if(CollectionUtils.isEmpty(entities)){
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_NO_INSERT,
                    "Data cannot be null");
        }

        List<Object> convertedList = new ArrayList<>();
        for(Object e : entities){
            Object converted = map(e, type.getEntityType());
            if(converted != null){
                convertedList.add(converted);
            }
        }

        Map<Integer, String> result = mongoCaller.insert(type.getCollection(), convertedList, type.getEntityType());
        BOGenericResponse response = new BOGenericResponse();

        if(!result.containsValue(Constants.SUCCESS))
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_NO_INSERT, "Unable to add any entry");

        response.setMessage("Following records inserted successfully");
        response.setData(result);

        return response;
    }

    public BOGenericResponse deleteEntries(String collection, Map<String, String> queryParam){
        EntityType type = null;
        if(collection == null || (type = EntityType.valueOf(collection)) == null){
            throw new ValidationException(ErrorUtil.VALIDATION_GENERIC_BO_INVALID_COLLECTION,
                    "Unsupported or incorrect Collection name");
        }
        BOGenericResponse response = new BOGenericResponse();
        boolean result = mongoCaller.deleteEntries(type.getCollection(), queryParam);
        if(result)
            response.setMessage("Delete Call Successful");
        else{
            response.setMessage("Unable to delete");
        }
        return response;
    }

    private Object map(Object input, Class clazz){
        Object o = null;
        try {
            o = this.mapper.readValue(JSONObject.valueToString(input), clazz);
        } catch (JsonProcessingException ex) {
            logger.error("Unable to convert [{}] due to an error. Details : {} ", input, ex.getMessage());
        }
        return o;
    }
}
