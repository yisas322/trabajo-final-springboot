package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysalmacenfx.modelo.ProductoR;
import pe.edu.upeu.sysalmacenfx.repositorio.ProductoRRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoSService {

    @Autowired
    private ProductoRRepository productoRepository;

    public List<ProductoR> obtenerProductosDisponibles() {
        return productoRepository.findByStockGreaterThan(0);
    }

    // Actualizar stock después de una venta
    @Transactional
    public boolean actualizarStock(Long productoId, Integer cantidad) {
        return productoRepository.actualizarStock(productoId, cantidad) > 0;
    }
}