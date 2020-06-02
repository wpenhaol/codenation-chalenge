package br.com.codenation.app.domain.model;

import lombok.Data;

@Data
public class Chalenge {

    private Integer numero_casas;
    private String token;
    private String cifrado;
    private String decifrado;
    private String resumo_criptografico;
}
