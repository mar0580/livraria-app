package com.livraria.controller;

import com.livraria.entity.Assunto;
import com.livraria.service.AssuntoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/assuntos")
public class AssuntoController {

    private final AssuntoService assuntoService;

    public AssuntoController(AssuntoService assuntoService) {
        this.assuntoService = assuntoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("assuntos", assuntoService.listarTodos());
        return "assunto/lista";
    }

    @GetMapping("/novo")
    public String novo(Assunto assunto) {
        return "assunto/form";
    }

    @PostMapping
    public String salvar(@Valid Assunto assunto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "assunto/form";
        }
        assuntoService.salvar(assunto);
        return "redirect:/assuntos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model) {
        Assunto assunto = assuntoService.buscarPorId(id);
        model.addAttribute("assunto", assunto);
        return "assunto/form";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Integer id) {
        assuntoService.excluir(id);
        return "redirect:/assuntos";
    }
}
