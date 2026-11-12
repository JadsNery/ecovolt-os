package br.unisales.ecovolt_os.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.unisales.ecovolt_os.model.Peca;
import br.unisales.ecovolt_os.service.PecaService;

@Controller
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaService pecaService;

    // Listar todas as peças em estoque (GET /pecas)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pecas", pecaService.listarTodas());
        return "pecas/lista"; // Retorna templates/pecas/lista.html
    }

    // Abrir formulário para cadastrar nova peça (GET /pecas/nova)
    @GetMapping("/nova")
    public String novoFormulario(Model model) {
        model.addAttribute("peca", new Peca());
        return "pecas/formulario"; // Retorna templates/pecas/formulario.html
    }

    // Salvar peça cadastrada ou editada (POST /pecas/salvar)
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Peca peca) {
        pecaService.salvar(peca);
        return "redirect:/pecas";
    }

    // Abrir formulário preenchido para edição (GET /pecas/editar/{id})
    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Peca peca = pecaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Peça não encontrada"));
        model.addAttribute("peca", peca);
        return "pecas/formulario";
    }

    // Excluir peça do estoque (GET /pecas/excluir/{id})
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        pecaService.excluir(id);
        return "redirect:/pecas";
    }
}