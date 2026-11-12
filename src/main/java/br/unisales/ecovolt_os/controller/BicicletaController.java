package br.unisales.ecovolt_os.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.unisales.ecovolt_os.model.Bicicleta;
import br.unisales.ecovolt_os.service.BicicletaService;
import br.unisales.ecovolt_os.service.ClienteService;

@Controller
@RequestMapping("/bicicletas")
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @Autowired
    private ClienteService clienteService;

    // Listar todas as bicicletas cadastradas (GET /bicicletas)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("bicicletas", bicicletaService.listarTodas());
        return "bicicletas/lista"; // Retorna templates/bicicletas/lista.html
    }

    // Abrir formulário para vincular nova bicicleta a um cliente (GET /bicicletas/nova)
    @GetMapping("/nova")
    public String novaFormulario(Model model) {
        model.addAttribute("bicicleta", new Bicicleta());
        // Envia a lista de clientes para popular o campo <select> no HTML
        model.addAttribute("clientes", clienteService.listarTodos());
        return "bicicletas/formulario"; // Retorna templates/bicicletas/formulario.html
    }

    // Salvar bicicleta vinculada ao cliente selecionado (POST /bicicletas/salvar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Bicicleta bicicleta, @RequestParam("clienteId") Long clienteId) {
        bicicletaService.salvar(bicicleta, clienteId);
        return "redirect:/bicicletas";
    }

    // Abrir formulário preenchido para edição (GET /bicicletas/editar/{id})
    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Bicicleta bicicleta = bicicletaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Bicicleta não encontrada"));
        model.addAttribute("bicicleta", bicicleta);
        model.addAttribute("clientes", clienteService.listarTodos());
        return "bicicletas/formulario";
    }

    // Excluir bicicleta (GET /bicicletas/excluir/{id})
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        bicicletaService.excluir(id);
        return "redirect:/bicicletas";
    }
}