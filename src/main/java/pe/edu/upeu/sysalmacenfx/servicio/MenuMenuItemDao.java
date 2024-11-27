package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.sysalmacenfx.dto.MenuMenuItenTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MenuMenuItemDao implements MenuMenuItenDaoI{

    @Override
    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma) {
        List<MenuMenuItenTO> lista = new ArrayList<>();
        lista.add(new MenuMenuItenTO("Principal", "Reg. Venta", "automo"));
        lista.add(new MenuMenuItenTO("Principal", "Reg. Producto", "miregproduct"));
        List<MenuMenuItenTO> accesoReal = new ArrayList<>();
        switch (perfil) {
            case "Administrador":
                accesoReal.add(lista.get(0));
                accesoReal.add(lista.get(1));
                break;
            case "Root":
                accesoReal = lista;
                break;
            case "Reporte":
                accesoReal.add(lista.get(0));
                break;
            default:
                throw new AssertionError();
        }
        return accesoReal;
    }

}
