package com.alura.literatura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConvierteDatosAPI implements IConvierteDatosAPI {
    private final ObjectMapper mapper = new  ObjectMapper();
    @Override
    public <T> T lockingLibro(String json, Class<T> clase){
        try {
            return mapper.readValue(json, clase);
        }catch (JsonProcessingException e){
            throw  new RuntimeException(e);
        }
    }
}
