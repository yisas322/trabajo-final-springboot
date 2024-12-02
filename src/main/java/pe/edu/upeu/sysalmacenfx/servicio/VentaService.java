package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysalmacenfx.modelo.DetalleVentaR;
import pe.edu.upeu.sysalmacenfx.modelo.VentaR;
import pe.edu.upeu.sysalmacenfx.repositorio.VentaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {
    private static final BigDecimal IGV_RATE = new BigDecimal("0.18"); // 18% IGV

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoSService productoService;

    @Autowired
    private CarritoService carritoService;

    @Transactional
    public VentaR procesarVenta() {
        List<DetalleVentaR> items = new ArrayList<>(carritoService.getItems()); // Crear una copia de los items
        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Crear nueva venta
        VentaR venta = new VentaR();

        venta.setFecha(LocalDateTime.now());

        // Calcular subtotal
        BigDecimal subtotal = carritoService.getTotal();
        venta.setSubtotal(subtotal);

        // Calcular IGV (18% del subtotal)
        BigDecimal igv = subtotal.multiply(IGV_RATE).setScale(2, RoundingMode.HALF_UP);
        venta.setIgv(igv);

        // Calcular total (subtotal + IGV)
        BigDecimal total = subtotal.add(igv);
        venta.setTotal(total);
        venta.setDetalles(new ArrayList<>()); // Inicializar la lista de detalles

        // Asignar la venta a cada detalle y actualizar stock
        for (DetalleVentaR detalle : items) {
            DetalleVentaR nuevoDetalle = new DetalleVentaR(); // Crear nuevo objeto para cada detalle
            nuevoDetalle.setVenta(venta);
            nuevoDetalle.setProducto(detalle.getProducto());
            nuevoDetalle.setCantidad(detalle.getCantidad());
            nuevoDetalle.setPrecioUnitario(detalle.getPrecioUnitario());
            nuevoDetalle.setSubtotal(detalle.getSubtotal());

            if (!productoService.actualizarStock(
                    detalle.getProducto().getId(),
                    detalle.getCantidad()
            )) {
                throw new RuntimeException("Error al actualizar stock del producto: "
                        + detalle.getProducto().getNombre());
            }

            venta.getDetalles().add(nuevoDetalle);
        }

        // Guardar la venta
        venta = ventaRepository.save(venta);

        // Verificar que los detalles se guardaron
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            throw new RuntimeException("Error: Los detalles de la venta no se guardaron correctamente");
        }

        System.out.println("Venta guardada con " + venta.getDetalles().size() + " detalles");

        // Limpiar el carrito DESPUÉS de verificar que todo se guardó correctamente
        carritoService.limpiarCarrito();

        return venta;
    }
}