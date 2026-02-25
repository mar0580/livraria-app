package com.livraria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_l")
    private Integer id;

    @NotBlank
    @Column(name = "titulo", length = 40, nullable = false)
    private String titulo;

    @NotBlank
    @Column(name = "editora", length = 40, nullable = false)
    private String editora;

    @NotNull
    @Min(1)
    @Column(name = "edicao", nullable = false)
    private Integer edicao;

    @NotBlank
    @Pattern(regexp = "\\d{4}", message = "Ano de publicação deve ter 4 dígitos")
    @Column(name = "ano_publicacao", length = 4, nullable = false)
    private String anoPublicacao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToMany
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_cod_l"),
            inverseJoinColumns = @JoinColumn(name = "autor_cod_au")
    )
    @Builder.Default
    private Set<Autor> autores = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "livro_assunto",
            joinColumns = @JoinColumn(name = "livro_cod_l"),
            inverseJoinColumns = @JoinColumn(name = "assunto_cod_as")
    )
    @Builder.Default
    private Set<Assunto> assuntos = new HashSet<>();
}
