package br.unisales.ecovolt_os.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unisales.ecovolt_os.model.Bicicleta;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Long> {
}