package br.unisales.ecovolt_os.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisales.ecovolt_os.model.Mecanico;
import br.unisales.ecovolt_os.repository.MecanicoRepository;

@Service
public class MecanicoService {

    private final MecanicoRepository mecanicoRepository;

    public MecanicoService(MecanicoRepository mecanicoRepository) {
        this.mecanicoRepository = mecanicoRepository;
    }

    public List<Mecanico> listarTodos() {
        return mecanicoRepository.findAll();
    }

    public Optional<Mecanico> buscarPorId(Long id) {
        return mecanicoRepository.findById(id);
    }

    @Transactional
    public Mecanico salvar(Mecanico mecanico) {
        return mecanicoRepository.save(mecanico);
    }

    @Transactional
    public void excluir(Long id) {
        mecanicoRepository.deleteById(id);
    }
}