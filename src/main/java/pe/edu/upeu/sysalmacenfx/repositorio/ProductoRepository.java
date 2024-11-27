package pe.edu.upeu.sysalmacenfx.repositorio;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysalmacenfx.modelo.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query(value = "select p.* from upeu_producto p where id_categoria =:idCate", nativeQuery = true)
    public List<Producto>  buscarIdCategoria(@Param("idCate") Long idCate);

    // Buscar productos con stock disponible para la venta
    List<Producto> findByStockGreaterThan(Integer stockMinimo);

    // Actualizar stock después de una venta
    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :productoId AND p.stock >= :cantidad")
    int actualizarStock(Long productoId, Integer cantidad);
}
