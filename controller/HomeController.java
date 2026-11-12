package br.unisales.ecovolt_os.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.unisales.ecovolt_os.service.ClienteService;
import br.unisales.ecovolt_os.service.OrdemServicoService;
import br.unisales.ecovolt_os.service.PecaService;

@Controller
public class HomeController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private PecaService pecaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalOrdens", ordemServicoService.listarTodas().size());
        model.addAttribute("totalPecas", pecaService.listarTodas().size());
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        return "index"; // Retorna o arquivo src/main/resources/templates/index.html
    }
}