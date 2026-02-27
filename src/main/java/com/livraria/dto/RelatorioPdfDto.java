package com.livraria.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelatorioPdfDto {
    private byte[] conteudo;
    private String nomeArquivo;
    private long tamanho;
}
