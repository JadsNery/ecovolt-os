package br.unisales.ecovolt_os.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisales.ecovolt_os.model.Peca;
import br.unisales.ecovolt_os.repository.PecaRepository;

@Service
public class PecaService {

    private final PecaRepository pecaRepository;

    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }

    public Optional<Peca> buscarPorId(Long id) {
        return pecaRepository.findById(id);
    }

    @Transactional
    public Peca salvar(Peca peca) {
        return pecaRepository.save(peca);
    }

    @Transactional
    public void excluir(Long id) {
        pecaRepository.deleteById(id);
    }
}