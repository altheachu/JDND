package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

//@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class OperationAbsentItemsException extends RuntimeException{

    private final List<Long> itemIds;

    private final String api;

    public OperationAbsentItemsException(List<Long> itemIds, String api) {
        super("Following ids are non-existent: " + itemIds);
        this.itemIds = itemIds;
        this.api = api;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public String getApi() {
        return api;
    }
}
