package pe.edu.upeu.sysalmacenfx.repositorio;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysalmacenfx.modelo.ProductoR;

import java.util.List;

@Repository
public interface ProductoRRepository extends JpaRepository<ProductoR, Long> {
    // Buscar productos con stock disponible para la venta
    List<ProductoR> findByStockGreaterThan(Integer stockMinimo);

    // Actualizar stock después de una venta
    @Modifying
    @Transactional
    @Query("UPDATE ProductoR p SET p.stock = p.stock - :cantidad WHERE p.id = :productoId AND p.stock >= :cantidad")
    int actualizarStock(Long productoId, Integer cantidad);
}