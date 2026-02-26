package com.livraria.controller;

import com.livraria.entity.Livro;
import com.livraria.service.LivroService;
import com.livraria.service.AutorService;
import com.livraria.service.AssuntoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);

    private final LivroService livroService;
    private final AutorService autorService;
    private final AssuntoService assuntoService;

    public LivroController(LivroService livroService, AutorService autorService, AssuntoService assuntoService) {
        this.livroService = livroService;
        this.autorService = autorService;
        this.assuntoService = assuntoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroService.listarTodos());
        return "livro/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("livro", new Livro());
        model.addAttribute("autores", autorService.listarTodos());
        model.addAttribute("assuntos", assuntoService.listarTodos());
        return "livro/form";
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
            model.addAttribute("autores", autorService.listarTodos());
            model.addAttribute("assuntos", assuntoService.listarTodos());
            return "livro/form";
        }

        livroService.salvar(livro, autoresSelecionados, assuntosSelecionados);
        return "redirect:/livros";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        Livro livro = livroService.buscarPorId(id);
        model.addAttribute("livro", livro);
        model.addAttribute("autores", autorService.listarTodos());
        model.addAttribute("assuntos", assuntoService.listarTodos());
        return "livro/form";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id) {
        livroService.excluir(id);
        logger.info("Livro deletado com sucesso: {}", id);
        return "redirect:/livros";
    }
}
