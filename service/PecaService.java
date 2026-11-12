package br.unisales.ecovolt_os.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisales.ecovolt_os.model.Peca;
import br.unisales.ecovolt_os.repository.PecaRepository;

@Service
public class PecaService {

    @Autowired
    private PecaRepository pecaRepository;

    // Salvar ou atualizar peça no estoque
    public Peca salvar(Peca peca) {
        return pecaRepository.save(peca);
    }

    // Listar todas as peças cadastradas
    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }

    // Buscar peça por ID
    public Optional<Peca> buscarPorId(Long id) {
        return pecaRepository.findById(id);
    }

    // Excluir peça
    public void excluir(Long id) {
        pecaRepository.deleteById(id);
    }

    // Regra de negócio: Baixar estoque ao concluir uma Ordem de Serviço
    public void baixarEstoque(Long pecaId, Integer quantidadeUtilizada) {
        Peca peca = pecaRepository.findById(pecaId)
                .orElseThrow(() -> new RuntimeException("Peça não encontrada com ID: " + pecaId));

        if (!peca.verificarDisponibilidade(quantidadeUtilizada)) {
            throw new RuntimeException("Estoque insuficiente para a peça: " + peca.getNome());
        }

        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - quantidadeUtilizada);
        pecaRepository.save(peca);
    }
}