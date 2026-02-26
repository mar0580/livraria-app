package com.livraria.controller;

import com.livraria.entity.Autor;
import com.livraria.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", autorService.listarTodos());
        return "autor/lista";
    }

    @GetMapping("/novo")
    public String novo(Autor autor) {
        return "autor/form";
    }

    @PostMapping
    public String salvar(@Valid Autor autor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "autor/form";
        }
        autorService.salvar(autor);
        return "redirect:/autores";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        Autor autor = autorService.buscarPorId(id);
        model.addAttribute("autor", autor);
        return "autor/form";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id) {
        autorService.excluir(id);
        return "redirect:/autores";
    }
}
