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

import br.unisales.ecovolt_os.model.OrdemServico;
import br.unisales.ecovolt_os.service.BicicletaService;
import br.unisales.ecovolt_os.service.MecanicoService;
import br.unisales.ecovolt_os.service.OrdemServicoService;
import br.unisales.ecovolt_os.service.PecaService;

@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    @Autowired
    private BicicletaService bicicletaService;

    @Autowired
    private MecanicoService mecanicoService;

    @Autowired
    private PecaService pecaService;

    // 1. Listar todas as Ordens de Serviço (GET /ordens)
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ordens", ordemServicoService.listarTodas());
        return "ordens/lista"; // Retorna templates/ordens/lista.html
    }

    // 2. Abrir formulário para nova Ordem de Serviço (GET /ordens/nova)
    @GetMapping("/nova")
    public String novaFormulario(Model model) {
        model.addAttribute("ordemServico", new OrdemServico());
        model.addAttribute("bicicletas", bicicletaService.listarTodas());
        model.addAttribute("mecanicos", mecanicoService.listarTodos());
        return "ordens/formulario"; // Retorna templates/ordens/formulario.html
    }

    // 3. Salvar abertura da OS (POST /ordens/abrir)
    @PostMapping("/abrir")
    public String abrir(@ModelAttribute OrdemServico ordemServico,
                        @RequestParam("bicicletaId") Long bicicletaId,
                        @RequestParam("mecanicoId") Long mecanicoId) {
        OrdemServico osSalva = ordemServicoService.abrirOS(ordemServico, bicicletaId, mecanicoId);
        // Redireciona direto para a tela de gerenciamento/detalhes desta OS
        return "redirect:/ordens/" + osSalva.getId();
    }

    // 4. Tela de detalhes da OS: adicionar peças e finalizar atendimento (GET /ordens/{id})
    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        OrdemServico os = ordemServicoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));
        
        model.addAttribute("os", os);
        model.addAttribute("pecasDisponiveis", pecaService.listarTodas());
        return "ordens/detalhes"; // Retorna templates/ordens/detalhes.html
    }

    // 5. Adicionar peça (item) à Ordem de Serviço (POST /ordens/{id}/adicionar-item)
    @PostMapping("/{id}/adicionar-item")
    public String adicionarItem(@PathVariable Long id,
                                @RequestParam("pecaId") Long pecaId,
                                @RequestParam("quantidade") Integer quantidade) {
        ordemServicoService.adicionarItem(id, pecaId, quantidade);
        return "redirect:/ordens/" + id;
    }

    // 6. Registrar laudo técnico, concluir OS e baixar estoque (POST /ordens/{id}/concluir)
    @PostMapping("/{id}/concluir")
    public String concluir(@PathVariable Long id,
                           @RequestParam("laudoTecnico") String laudoTecnico) {
        ordemServicoService.concluirOS(id, laudoTecnico);
        return "redirect:/ordens/" + id;
    }
}