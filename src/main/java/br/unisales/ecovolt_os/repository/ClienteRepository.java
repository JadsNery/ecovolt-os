package br.unisales.ecovolt_os.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unisales.ecovolt_os.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // O JpaRepository já fornece métodos como save(), findAll(), findById() e deleteById()
}