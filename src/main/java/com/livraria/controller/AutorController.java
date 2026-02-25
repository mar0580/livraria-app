package com.livraria.controller;

import com.livraria.entity.Autor;
import com.livraria.repository.AutorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/autores")

public class AutorController {
    @Autowired
    private final AutorRepository autorRepository;

    public AutorController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", autorRepository.findAll());
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
        autorRepository.save(autor);
        return "redirect:/autores";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Autor inválido: " + id));
        model.addAttribute("autor", autor);
        return "autor/form";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id) {
        autorRepository.deleteById(id);
        return "redirect:/autores";
    }
}
