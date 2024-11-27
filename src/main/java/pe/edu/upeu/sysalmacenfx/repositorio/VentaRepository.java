package pe.edu.upeu.sysalmacenfx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysalmacenfx.modelo.VentaR;

@Repository
public interface VentaRepository extends JpaRepository<VentaR, Long> {
}
