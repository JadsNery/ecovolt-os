package br.unisales.ecovolt_os.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.unisales.ecovolt_os.model.Cliente;
import br.unisales.ecovolt_os.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Listar todos os clientes (GET /clientes)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista"; // Retorna templates/clientes/lista.html
    }

    // Abrir formulário para novo cliente (GET /clientes/novo)
    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario"; // Retorna templates/clientes/formulario.html
    }

    // Salvar cliente cadastrado ou editado (POST /clientes/salvar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Cliente cliente) {
        clienteService.salvar(cliente);
        return "redirect:/clientes";
    }

    // Abrir formulário preenchido para edição (GET /clientes/editar/{id})
    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/formulario";
    }

    // Excluir cliente (GET /clientes/excluir/{id})
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return "redirect:/clientes";
    }
}