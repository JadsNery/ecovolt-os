package br.unisales.ecovolt_os.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unisales.ecovolt_os.model.Bicicleta;
import br.unisales.ecovolt_os.model.Cliente;
import br.unisales.ecovolt_os.repository.BicicletaRepository;
import br.unisales.ecovolt_os.repository.ClienteRepository;

@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Salvar ou atualizar uma bicicleta vinculando-a a um cliente existente
    public Bicicleta salvar(Bicicleta bicicleta, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + clienteId));
        
        bicicleta.setCliente(cliente);
        return bicicletaRepository.save(bicicleta);
    }

    // Listar todas as bicicletas cadastradas
    public List<Bicicleta> listarTodas() {
        return bicicletaRepository.findAll();
    }

    // Buscar bicicleta por ID
    public Optional<Bicicleta> buscarPorId(Long id) {
        return bicicletaRepository.findById(id);
    }

    // Excluir bicicleta
    public void excluir(Long id) {
        bicicletaRepository.deleteById(id);
    }
}