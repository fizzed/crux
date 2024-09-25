package com.fizzed.crux.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryParamMapper {

    static public Map<String,String> toQueryParams(ObjectMapper objectMapper, Object value) throws IOException {

        final JsonNode node = objectMapper.valueToTree(value);

        // node MUST be an object
        if (!node.isObject()) {
            throw new IOException("Only objects are supported");
        }

        // build a Map<String,String> ourselves of this object
        final Map<String,String> params = new LinkedHashMap<>();

        Iterator<Map.Entry<String,JsonNode>> fieldIterator = node.fields();

        while (fieldIterator.hasNext()) {
            Map.Entry<String,JsonNode> field = fieldIterator.next();
            String fieldName = field.getKey();
            JsonNode fieldNode = field.getValue();

            if (fieldNode.isValueNode()) {
                params.put(fieldName, fieldNode.asText());
            } else if (fieldNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode)fieldNode;
                Iterator<JsonNode> arrayIterator = arrayNode.elements();
                StringBuilder sb = new StringBuilder();

                while (arrayIterator.hasNext()) {
                    JsonNode arrayFieldNode = arrayIterator.next();

                    if (arrayFieldNode.isValueNode()) {
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        sb.append(arrayFieldNode.asText());
                    } else {
                        // we can only serialize arrays 1-level deep!
                        throw new IOException("Only objects that contain 1-level deep of iterables are supported");
                    }
                }

                params.put(fieldName, sb.toString());
            }
        }

        return params;
    }

    static public <T> T fromQueryParams(ObjectMapper objectMapper, Map<String,String> params, Class<T> type) throws IOException {

        // build a map of key -> values, where comma-delimited values are turned into lists
        final Map<String,Object> data = new HashMap<>();

        for (Map.Entry<String,String> entry : params.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();

            if (value != null && value.contains(",")) {
                String[] tokens = value.split(",");
                data.put(key, tokens);
            } else {
                data.put(key, value);
            }
        }

        return objectMapper.convertValue(data, type);
    }

}