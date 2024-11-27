package pe.edu.upeu.sysalmacenfx.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class VentaR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleVentaR> detalles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleVentaR> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaR> detalles) {
        this.detalles.clear();
        if (detalles != null) {
            this.detalles.addAll(detalles);
            // Asegurar que cada detalle tenga referencia a esta venta
            this.detalles.forEach(detalle -> detalle.setVenta(this));
        }
    }
}
