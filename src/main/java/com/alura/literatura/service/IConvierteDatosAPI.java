package com.alura.literatura.service;

public interface IConvierteDatosAPI {
    <T> T lockingLibro(String json, Class<T> clase);
}
