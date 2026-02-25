package com.livraria.controller;

import com.livraria.entity.Livro;
import com.livraria.repository.AssuntoRepository;
import com.livraria.repository.AutorRepository;
import com.livraria.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);

    @Autowired
    private final LivroService livroService;
    @Autowired
    private final AutorRepository autorRepository;
    @Autowired
    private final AssuntoRepository assuntoRepository;

    public LivroController(LivroService livroService, AutorRepository autorRepository, AssuntoRepository assuntoRepository) {
        this.livroService = livroService;
        this.autorRepository = autorRepository;
        this.assuntoRepository = assuntoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        try {
            model.addAttribute("livros", livroService.listarTodos());
        } catch (Exception e) {
            logger.error("Erro ao listar livros", e);
            model.addAttribute("livros", List.of());
        }
        return "livro/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        try {
            model.addAttribute("livro", new Livro());
            model.addAttribute("autores", autorRepository.findAll());
            model.addAttribute("assuntos", assuntoRepository.findAll());
            return "livro/form";
        } catch (Exception e) {
            // Caso o banco ou repositório falhe, ainda exibimos o formulário com listas vazias e mensagem amigável
            logger.error("Erro ao carregar formulário de novo livro", e);
            model.addAttribute("livro", new Livro());
            model.addAttribute("autores", Collections.emptyList());
            model.addAttribute("assuntos", Collections.emptyList());
            model.addAttribute("errorMessage", "Não foi possível carregar autores/assuntos: " + e.getMessage());
            return "livro/form";
        }
    }

    @PostMapping
    public String salvar(
            @Valid Livro livro,
            BindingResult result,
            @RequestParam(name = "autoresSelecionados", required = false) Set<Integer> autoresSelecionados,
            @RequestParam(name = "assuntosSelecionados", required = false) Set<Integer> assuntosSelecionados,
            Model model) {

        if (result.hasErrors()) {
            logger.warn("Erros de validação no formulário: {}", result.getErrorCount());
            model.addAttribute("autores", autorRepository.findAll());
            model.addAttribute("assuntos", assuntoRepository.findAll());
            return "livro/form";
        }

        try {
            if (autoresSelecionados == null) autoresSelecionados = new HashSet<>();
            if (assuntosSelecionados == null) assuntosSelecionados = new HashSet<>();

            livroService.salvar(livro, autoresSelecionados, assuntosSelecionados);
            logger.info("Livro salvo com sucesso: {}", livro.getTitulo());
            return "redirect:/livros";
        } catch (Exception e) {
            logger.error("Erro ao salvar livro", e);
            model.addAttribute("autores", autorRepository.findAll());
            model.addAttribute("assuntos", assuntoRepository.findAll());
            model.addAttribute("errorMessage", "Erro ao salvar livro: " + e.getMessage());
            return "livro/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        try {
            Livro livro = livroService.buscarPorId(id);
            model.addAttribute("livro", livro);
            model.addAttribute("autores", autorRepository.findAll());
            model.addAttribute("assuntos", assuntoRepository.findAll());
            return "livro/form";
        } catch (Exception e) {
            logger.error("Erro ao buscar livro para edição", e);
            model.addAttribute("errorMessage", "Livro não encontrado");
            return "error/generic";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id) {
        try {
            livroService.excluir(id);
            logger.info("Livro deletado com sucesso: {}", id);
        } catch (Exception e) {
            logger.error("Erro ao deletar livro", e);
        }
        return "redirect:/livros";
    }
}
