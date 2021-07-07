package com.saklecha.consumer;

import com.saklecha.entity.*;

public enum EntityType {
    USERS("users", UserEntity.class),
    TEST_ENTITY("testCollection", TestEntity.class);

    private String collection;
    private Class entityType;

    private EntityType(String collection, Class entityType){
        this.collection = collection;
        this.entityType = entityType;
    }

    public String getCollection(){
        return this.collection;
    }

    public Class getEntityType(){
        return this.entityType;
    }
}
