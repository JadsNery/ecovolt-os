package br.unisales.ecovolt_os.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataAbertura;
    private LocalDate dataPrevisao;
    private LocalDateTime dataConclusao;

    // Mapeamento do Enum para salvar como texto (ABERTA, EM_ANDAMENTO, CONCLUIDA) no banco
    @Enumerated(EnumType.STRING)
    private StatusOS status;

    private String relatoCliente;
    private String laudoTecnico;
    private BigDecimal valorTotalGeral;

    // Relacionamentos baseados no diagrama de classes
    @ManyToOne
    @JoinColumn(name = "bicicleta_id")
    private Bicicleta bicicleta;

    @ManyToOne
    @JoinColumn(name = "mecanico_id")
    private Mecanico mecanico;

    @OneToMany(mappedBy = "ordemServico")
    private List<ItemOrdemServico> itens;

    // Declaração do Enum interno conforme manual de uso e diagrama
    public enum StatusOS {
        ABERTA, EM_ANDAMENTO, CONCLUIDA
    }

    // Construtor vazio exigido pelo JPA
    public OrdemServico() {
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }

    public LocalDate getDataPrevisao() { return dataPrevisao; }
    public void setDataPrevisao(LocalDate dataPrevisao) { this.dataPrevisao = dataPrevisao; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public StatusOS getStatus() { return status; }
    public void setStatus(StatusOS status) { this.status = status; }

    public String getRelatoCliente() { return relatoCliente; }
    public void setRelatoCliente(String relatoCliente) { this.relatoCliente = relatoCliente; }

    public String getLaudoTecnico() { return laudoTecnico; }
    public void setLaudoTecnico(String laudoTecnico) { this.laudoTecnico = laudoTecnico; }

    public BigDecimal getValorTotalGeral() { return valorTotalGeral; }
    public void setValorTotalGeral(BigDecimal valorTotalGeral) { this.valorTotalGeral = valorTotalGeral; }

    public Bicicleta getBicicleta() { return bicicleta; }
    public void setBicicleta(Bicicleta bicicleta) { this.bicicleta = bicicleta; }

    public Mecanico getMecanico() { return mecanico; }
    public void setMecanico(Mecanico mecanico) { this.mecanico = mecanico; }

    public List<ItemOrdemServico> getItens() { return itens; }
    public void setItens(List<ItemOrdemServico> itens) { this.itens = itens; }

    // Método indicativo baseado no diagrama de classes
    public void calcularValorTotal() {
        // A lógica de soma dos subtotais dos itens será implementada aqui ou no Service
    }
}