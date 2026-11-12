package br.unisales.ecovolt_os.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final OrdemServicoRepository ordemServicoRepository;
    private final BicicletaRepository bicicletaRepository;
    private final MecanicoRepository mecanicoRepository;
    private final PecaRepository pecaRepository;
    private final ItemOrdemServicoRepository itemOrdemServicoRepository;

    public OrdemServicoService(OrdemServicoRepository ordemServicoRepository,
                               BicicletaRepository bicicletaRepository,
                               MecanicoRepository mecanicoRepository,
                               PecaRepository pecaRepository,
                               ItemOrdemServicoRepository itemOrdemServicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.mecanicoRepository = mecanicoRepository;
        this.pecaRepository = pecaRepository;
        this.itemOrdemServicoRepository = itemOrdemServicoRepository;
    }

    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.findAll();
    }

    public Optional<OrdemServico> buscarPorId(Long id) {
        return ordemServicoRepository.findById(id);
    }

    @Transactional
    public OrdemServico abrirOS(OrdemServico ordemServico, Long bicicletaId, Long mecanicoId) {
        Bicicleta bicicleta = bicicletaRepository.findById(bicicletaId)
                .orElseThrow(() -> new RuntimeException("Bicicleta não encontrada"));
        Mecanico mecanico = mecanicoRepository.findById(mecanicoId)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado"));

        ordemServico.setBicicleta(bicicleta);
        ordemServico.setMecanico(mecanico);
        ordemServico.setDataAbertura(LocalDateTime.now());
        ordemServico.setStatus(StatusOS.ABERTA);
        ordemServico.setValorTotalGeral(BigDecimal.ZERO);

        return ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public void adicionarItem(Long ordemServicoId, Long pecaId, Integer quantidade) {
        OrdemServico os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        if (os.getStatus() == StatusOS.CONCLUIDA) {
            throw new IllegalStateException("Não é possível adicionar itens a uma OS já concluída.");
        }

        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new RuntimeException("Peça não encontrada"));

        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (!peca.verificarDisponibilidade(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente para a peça: " + peca.getNome());
        }

        ItemOrdemServico item = new ItemOrdemServico();
        item.setOrdemServico(os);
        item.setPeca(peca);
        item.setQuantidade(quantidade);
        item.setPrecoVenda(peca.getPrecoUnitario());
        itemOrdemServicoRepository.save(item);

        if (os.getItens() == null) {
            os.setItens(new ArrayList<>());
        }
        os.getItens().add(item);

        if (os.getStatus() == StatusOS.ABERTA) {
            os.setStatus(StatusOS.EM_ANDAMENTO);
        }

        recalcularTotal(os);
        ordemServicoRepository.save(os);
    }

    @Transactional
    public void concluirOS(Long ordemServicoId, String laudoTecnico) {
        OrdemServico os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        if (os.getStatus() == StatusOS.CONCLUIDA) {
            throw new IllegalStateException("Esta Ordem de Serviço já foi concluída.");
        }

        // Realiza a baixa automática no estoque das peças utilizadas
        if (os.getItens() != null) {
            for (ItemOrdemServico item : os.getItens()) {
                Peca peca = item.getPeca();
                if (!peca.verificarDisponibilidade(item.getQuantidade())) {
                    throw new IllegalStateException("Estoque insuficiente para baixar a peça: " + peca.getNome());
                }
                peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - item.getQuantidade());
                pecaRepository.save(peca);
            }
        }

        os.setLaudoTecnico(laudoTecnico);
        os.setStatus(StatusOS.CONCLUIDA);
        os.setDataConclusao(LocalDateTime.now());
        recalcularTotal(os);

        ordemServicoRepository.save(os);
    }

    private void recalcularTotal(OrdemServico os) {
        if (os.getItens() == null || os.getItens().isEmpty()) {
            os.setValorTotalGeral(BigDecimal.ZERO);
            return;
        }
        BigDecimal total = os.getItens().stream()
                .map(ItemOrdemServico::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        os.setValorTotalGeral(total);
    }
}