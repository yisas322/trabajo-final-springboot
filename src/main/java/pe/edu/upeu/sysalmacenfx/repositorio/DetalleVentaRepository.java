package pe.edu.upeu.sysalmacenfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysalmacenfx.modelo.DetalleVentaR;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaR, Long> {
}
