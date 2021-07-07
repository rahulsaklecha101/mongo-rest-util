package com.saklecha.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saklecha.model.ErrorType;
import lombok.Data;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "testCollection")
@Data
@Table
@Entity
public class TestEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    //API URL
    private String service;

    @NotNull
    @Column(unique=true, nullable = false)
    private String code;

    @NotNull
    @Column(nullable = false)
    private ErrorType type;

    @NotNull
    @Column(nullable = false)
    private String errorMessage;
    private String note;

    @BsonRepresentation(BsonType.BOOLEAN)
    @BsonProperty("isActive")
    @JsonProperty("isActive")
    private boolean isActive;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM DD, YYYY HH:mm:ss a", timezone = "CST")
    private LocalDateTime createdAt;
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM DD, YYYY HH:mm:ss a", timezone = "CST")
    private LocalDateTime updatedAt;
    @LastModifiedBy
    private String updatedBy;
}
