package com.brunao.brunao.service;

public class FilmeService {


    public String validarCodigo(Integer codigo) {
        if (codigo == null) {
            return "{\n" +
                    "    \"message\": \"Código é obrigatório\",\n" +
                    "}";
        }
        return null;
    }
}
