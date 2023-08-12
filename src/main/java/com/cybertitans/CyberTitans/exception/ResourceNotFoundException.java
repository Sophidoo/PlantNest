package com.cybertitans.CyberTitans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fileName;
    private long fieldName;

    public ResourceNotFoundException(String resourceName, String fileName, long fieldName) {
        super(String.format("%s with %s = %s not found", resourceName, fileName, fieldName)); // post not with id = 1
        this.resourceName = resourceName;
        this.fileName = fileName;
        this.fieldName = fieldName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFieldName() {
        return fieldName;
    }
}
