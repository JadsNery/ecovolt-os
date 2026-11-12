package br.unisales.ecovolt_os.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String numeroSerie;
    private String potenciaMotor;
    private String capacidadeBateria;

    // Relacionamento: Muitas bicicletas podem pertencer a um Cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Construtor vazio exigido pelo JPA
    public Bicicleta() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public String getPotenciaMotor() { return potenciaMotor; }
    public void setPotenciaMotor(String potenciaMotor) { this.potenciaMotor = potenciaMotor; }

    public String getCapacidadeBateria() { return capacidadeBateria; }
    public void setCapacidadeBateria(String capacidadeBateria) { this.capacidadeBateria = capacidadeBateria; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}