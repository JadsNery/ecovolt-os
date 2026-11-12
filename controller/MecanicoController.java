package br.unisales.ecovolt_os.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.unisales.ecovolt_os.model.Mecanico;
import br.unisales.ecovolt_os.service.MecanicoService;

@Controller
@RequestMapping("/mecanicos")
public class MecanicoController {

    @Autowired
    private MecanicoService mecanicoService;

    // Listar todos os mecânicos cadastrados (GET /mecanicos)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mecanicos", mecanicoService.listarTodos());
        return "mecanicos/lista"; // Retorna templates/mecanicos/lista.html
    }

    // Abrir formulário para cadastrar novo mecânico (GET /mecanicos/novo)
    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("mecanico", new Mecanico());
        return "mecanicos/formulario"; // Retorna templates/mecanicos/formulario.html
    }

    // Salvar mecânico cadastrado ou editado (POST /mecanicos/salvar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Mecanico mecanico) {
        mecanicoService.salvar(mecanico);
        return "redirect:/mecanicos";
    }

    // Abrir formulário preenchido para edição (GET /mecanicos/editar/{id})
    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Mecanico mecanico = mecanicoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado"));
        model.addAttribute("mecanico", mecanico);
        return "mecanicos/formulario";
    }

    // Excluir mecânico (GET /mecanicos/excluir/{id})
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        mecanicoService.excluir(id);
        return "redirect:/mecanicos";
    }
}