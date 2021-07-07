package com.saklecha.controller;

import com.saklecha.response.BOGenericResponse;
import com.saklecha.service.CosmosDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController(value = "/util/mongo/v1.0")
public class DbController {

    @Autowired
    private CosmosDbService dbService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<Object> get(@RequestParam Map<String, String> map){
        Map<String, String> queryParam = new ConcurrentHashMap<>(map);
        String collectionName = queryParam.get("collection");
        queryParam.remove("collection");
        BOGenericResponse response = dbService.getEntries(collectionName, queryParam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Object> add(@RequestParam Map<String, String> map, @RequestBody List entities){
        Map<String, String> queryParam = new ConcurrentHashMap<>(map);
        String collectionName = queryParam.get("collection");
        queryParam.remove("collection");
        if(!CollectionUtils.isEmpty(entities)){
            System.out.println("POST body: "+ entities);
        }
        BOGenericResponse response = dbService.insertEntries(collectionName, entities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<Object> update(@RequestParam Map<String, String> map, @RequestBody List entities) {
        Map<String, String> queryParam = new ConcurrentHashMap<>(map);
        String collectionName = queryParam.get("collection");
        queryParam.remove("collection");
        if (!CollectionUtils.isEmpty(entities)) {
            System.out.println("PUT body: " + entities);
        }
        BOGenericResponse response = dbService.updateEntries(collectionName, queryParam, entities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Object> delete(@RequestParam Map<String, String> map){
        Map<String, String> queryParam = new ConcurrentHashMap<>(map);
        String collectionName = queryParam.get("collection");
        queryParam.remove("collection");
        BOGenericResponse response = dbService.deleteEntries(collectionName, queryParam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
