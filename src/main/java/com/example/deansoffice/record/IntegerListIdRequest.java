package com.example.deansoffice.record;

import java.util.List;
import java.util.Objects;

public record IntegerListIdRequest(List<Integer> listId) {
    public IntegerListIdRequest {
        Objects.requireNonNull(listId, "List cannot be null.");
        if (listId.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty.");
        }
    }
}