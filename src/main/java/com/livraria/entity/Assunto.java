package com.livraria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "assunto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_as")
    private Integer id;

    @NotBlank(message = "Descrição do assunto é obrigatória")
    @Column(name = "descricao", length = 20, nullable = false)
    private String descricao;
}
