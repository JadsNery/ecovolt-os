package br.unisales.ecovolt_os.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.unisales.ecovolt_os.model.Bicicleta;
import br.unisales.ecovolt_os.model.Cliente;
import br.unisales.ecovolt_os.repository.BicicletaRepository;
import br.unisales.ecovolt_os.repository.ClienteRepository;

@Service
public class BicicletaService {

    private final BicicletaRepository bicicletaRepository;
    private final ClienteRepository clienteRepository;

    public BicicletaService(BicicletaRepository bicicletaRepository, ClienteRepository clienteRepository) {
        this.bicicletaRepository = bicicletaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Bicicleta> listarTodas() {
        return bicicletaRepository.findAll();
    }

    public Optional<Bicicleta> buscarPorId(Long id) {
        return bicicletaRepository.findById(id);
    }

    @Transactional
    public Bicicleta salvar(Bicicleta bicicleta, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        bicicleta.setCliente(cliente);
        return bicicletaRepository.save(bicicleta);
    }

    @Transactional
    public void excluir(Long id) {
        bicicletaRepository.deleteById(id);
    }
}