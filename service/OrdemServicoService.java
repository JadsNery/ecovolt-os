package br.unisales.ecovolt_os.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisales.ecovolt_os.model.Bicicleta;
import br.unisales.ecovolt_os.model.ItemOrdemServico;
import br.unisales.ecovolt_os.model.Mecanico;
import br.unisales.ecovolt_os.model.OrdemServico;
import br.unisales.ecovolt_os.model.OrdemServico.StatusOS;
import br.unisales.ecovolt_os.model.Peca;
import br.unisales.ecovolt_os.repository.BicicletaRepository;
import br.unisales.ecovolt_os.repository.ItemOrdemServicoRepository;
import br.unisales.ecovolt_os.repository.MecanicoRepository;
import br.unisales.ecovolt_os.repository.OrdemServicoRepository;
import br.unisales.ecovolt_os.repository.PecaRepository;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ItemOrdemServicoRepository itemOrdemServicoRepository;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private PecaService pecaService;

    // 1. Abrir nova Ordem de Serviço
    public OrdemServico abrirOS(OrdemServico os, Long bicicletaId, Long mecanicoId) {
        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new RuntimeException("Bicicleta não encontrada com ID: " + bicicletaId));
        
        Mecanico mecanico = mecanicoRepository.findById(mecanicoId)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado com ID: " + mecanicoId));

        os.setBicicleta(bicicleta);
        os.setMecanico(mecanico);
        os.setDataAbertura(LocalDateTime.now());
        os.setStatus(StatusOS.ABERTA);
        os.setValorTotalGeral(BigDecimal.ZERO);

        return ordemServicoRepository.save(os);
    }

    // 2. Adicionar peças à OS e recalcular o valor total
    @Transactional
    public OrdemServico adicionarItem(Long osId, Long pecaId, Integer quantidade) {
        OrdemServico os = ordemServicoRepository.findById(osId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada com ID: " + osId));
        
        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new RuntimeException("Peça não encontrada com ID: " + pecaId));

        // Cria o item e captura o preço atual da peça
        ItemOrdemServico item = new ItemOrdemServico();
        item.setOrdemServico(os);
        item.setPeca(peca);
        item.setQuantidade(quantidade);
        item.setPrecoVenda(peca.getPrecoUnitario());
        itemOrdemServicoRepository.save(item);

        // Atualiza a lista de itens na memória e recalcula o total geral
        os.getItens().add(item);
        recalcularValorTotal(os);

        // Se ainda estava apenas ABERTA, muda para EM_ANDAMENTO
        if (os.getStatus() == StatusOS.ABERTA) {
            os.setStatus(StatusOS.EM_ANDAMENTO);
        }

        return ordemServicoRepository.save(os);
    }

    // Método auxiliar para somar os subtotais de todos os itens
    private void recalcularValorTotal(OrdemServico os) {
        BigDecimal total = BigDecimal.ZERO;
        if (os.getItens() != null) {
            for (ItemOrdemServico item : os.getItens()) {
                total = total.add(item.calcularSubtotal());
            }
        }
        os.setValorTotalGeral(total);
    }

    // 3. Concluir OS, registrar laudo técnico e dar baixa automática no estoque
    @Transactional
    public OrdemServico concluirOS(Long osId, String laudoTecnico) {
        OrdemServico os = ordemServicoRepository.findById(osId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada com ID: " + osId));

        if (os.getStatus() == StatusOS.CONCLUIDA) {
            throw new RuntimeException("Esta Ordem de Serviço já foi concluída.");
        }

        os.setLaudoTecnico(laudoTecnico);
        os.setDataConclusao(LocalDateTime.now());
        os.setStatus(StatusOS.CONCLUIDA);

        // Dá baixa no estoque para cada peça vinculada a esta OS
        if (os.getItens() != null) {
            for (ItemOrdemServico item : os.getItens()) {
                pecaService.baixarEstoque(item.getPeca().getId(), item.getQuantidade());
            }
        }

        return ordemServicoRepository.save(os);
    }

    // Listar todas as Ordens de Serviço
    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.findAll();
    }

    // Buscar OS por ID
    public Optional<OrdemServico> buscarPorId(Long id) {
        return ordemServicoRepository.findById(id);
    }
}