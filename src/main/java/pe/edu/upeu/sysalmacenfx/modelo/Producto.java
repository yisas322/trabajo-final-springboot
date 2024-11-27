package pe.edu.upeu.sysalmacenfx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_producto")  //Bonnier (1p)
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 120, message = "El nombre debe tener entre 2 y 120 caracteres")
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;
    @Positive(message = "El Precio Unitario debe ser positivo")
    @Column(name = "pu", nullable = false)
    private Double pu;
    @PositiveOrZero(message = "El Precio Unitario Anterior debe ser positivo o cero")
    @Column(name = "puold", nullable = false)
    private Double puOld;
    @Positive(message = "La utilidad debe ser positiva")
    @Column(name = "utilidad", nullable = false)
    private Double utilidad;
    @Positive(message = "El Stock debe ser positivo o cero")
    @Column(name = "stock", nullable = false)
    private Double stock;
    @PositiveOrZero(message = "El Stock Anterior debe ser positivo o cero")
    @Column(name = "stockold", nullable = false)
    private Double stockOld;

    @NotNull(message = "Categoria no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName = "id_categoria",
            nullable = false, foreignKey = @ForeignKey(name = "FK_CATEGORIA_PRODUCTO") )
    private Categoria categoria;

    @NotNull(message = "Marca no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_marca", referencedColumnName = "id_marca",
            nullable = false, foreignKey = @ForeignKey(name = "FK_MARCA_PRODUCTO"))
    private Marca marca;

    @NotNull(message = "Unidad Medida no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_unidad", referencedColumnName = "id_unidad",
            nullable = false, foreignKey = @ForeignKey(name = "FK_UNIDADMEDIDA_PRODUCTO"))
    private UnidadMedida unidadMedida;

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPu() {
        return pu;
    }

    public void setPu(Double pu) {
        this.pu = pu;
    }

    public Double getPuOld() {
        return puOld;
    }

    public void setPuOld(Double puOld) {
        this.puOld = puOld;
    }

    public Double getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Double utilidad) {
        this.utilidad = utilidad;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getStockOld() {
        return stockOld;
    }

    public void setStockOld(Double stockOld) {
        this.stockOld = stockOld;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}