package br.unisales.ecovolt_os.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisales.ecovolt_os.model.Mecanico;
import br.unisales.ecovolt_os.repository.MecanicoRepository;

@Service
public class MecanicoService {

    @Autowired
    private MecanicoRepository mecanicoRepository;

    // Salvar ou atualizar mecânico
    public Mecanico salvar(Mecanico mecanico) {
        return mecanicoRepository.save(mecanico);
    }

    // Listar todos os mecânicos
    public List<Mecanico> listarTodos() {
        return mecanicoRepository.findAll();
    }

    // Buscar mecânico por ID
    public Optional<Mecanico> buscarPorId(Long id) {
        return mecanicoRepository.findById(id);
    }

    // Excluir mecânico
    public void excluir(Long id) {
        mecanicoRepository.deleteById(id);
    }
}