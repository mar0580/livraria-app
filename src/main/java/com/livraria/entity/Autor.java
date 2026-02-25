package com.livraria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "autor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_au")
    private Integer id;

    @NotBlank(message = "Nome do autor é obrigatório")
    @Column(name = "nome", length = 40, nullable = false)
    private String nome;
}

