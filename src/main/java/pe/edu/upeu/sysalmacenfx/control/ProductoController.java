package pe.edu.upeu.sysalmacenfx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.componente.ColumnInfo;
import pe.edu.upeu.sysalmacenfx.componente.ComboBoxAutoComplete;
import pe.edu.upeu.sysalmacenfx.componente.TableViewHelper;
import pe.edu.upeu.sysalmacenfx.componente.Toast;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Producto;
import pe.edu.upeu.sysalmacenfx.servicio.CategoriaService;
import pe.edu.upeu.sysalmacenfx.servicio.MarcaService;
import pe.edu.upeu.sysalmacenfx.servicio.ProductoService;
import pe.edu.upeu.sysalmacenfx.servicio.UnidadMedidaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ProductoController {

    @FXML
    TextField txtNombreProducto, txtPUnit,
            txtPUnitOld, txtUtilidad, txtStock, txtStockOld,
            txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxCategoria;
    @FXML
    ComboBox<ComboBoxOption> cbxProducto;
    @FXML
    ComboBox<ComboBoxOption> cbxUnidMedida;
    @FXML
    private TableView<Producto> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    MarcaService ms;
    @Autowired
    CategoriaService cs;
    @Autowired
    ProductoService ps;
    @Autowired
    UnidadMedidaService ums;

    private Validator validator;
    ObservableList<Producto> listarProducto;
    Producto formulario;
    Long idProductoCE=0L;


    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();



        cbxCategoria.setTooltip(new Tooltip());
        cbxCategoria.getItems().addAll(cs.listarCombobox());
        cbxCategoria.setOnAction(event -> {
            ComboBoxOption selectedProductxx=cbxCategoria.getSelectionModel().getSelectedItem();

            if (selectedProductxx != null) {
                String selectedId=selectedProductxx.getKey();

                selectProdCat(selectedId);

                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });

        //new ComboBoxAutoComplete<>(cbxCategoria);





        cbxUnidMedida.setTooltip(new Tooltip());
        cbxUnidMedida.getItems().addAll(ums.listarCombobox());
        cbxUnidMedida.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxUnidMedida.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedIdx = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedIdx);
            }
        });
        new ComboBoxAutoComplete<>(cbxUnidMedida);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Nombre del Cliente", new ColumnInfo("nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("P. Unitario", new ColumnInfo("pu", 150.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Utilidad", new ColumnInfo("utilidad", 100.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Marca", new ColumnInfo("marca.nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Categoria", new ColumnInfo("categoria.nombre", 200.0));
        columns.put("Unid. Medida", new ColumnInfo("unidadMedida.nombreMedida",150.0));


        Consumer<Producto> updateAction = (Producto producto) -> {
            System.out.println("Actualizar: " + producto);
            editForm(producto);
        };
        Consumer<Producto> deleteAction = (Producto producto) -> {System.out.println("Actualizar: " + producto);
            ps.delete(producto.getIdProducto()); /*deletePerson(usuario);*/
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns,updateAction, deleteAction );

        tableView.setTableMenuButtonVisible(true);
        listar();

    }

    public void selectProdCat(String ddd) {
        // Imprime el ID de la categoría seleccionada
        System.out.println("PRD: " + ddd);

        // Limpia los items previos y agrega los productos correspondientes a la categoría
        cbxProducto.setTooltip(new Tooltip());
        cbxProducto.getItems().clear();
        cbxProducto.getItems().addAll(ps.listarCombobox(Long.parseLong(ddd)));  // Aquí obtienes los productos según la categoría

        // Configuramos el evento de selección del ComboBox de productos
        cbxProducto.setOnAction(eventx -> {
            // Obtenemos el producto seleccionado
            ComboBoxOption selectedProductx = cbxProducto.getSelectionModel().getSelectedItem();

            if (selectedProductx != null) {
                String selectedIdx = selectedProductx.getKey();  // Obtenemos el ID del producto seleccionado
                System.out.println("ID del producto seleccionado: " + selectedIdx);

                // Llamamos al servicio para obtener el producto por su ID
                Producto productoSeleccionado = ps.searchById(Long.parseLong(selectedIdx));

                // Si el producto es encontrado, actualizamos el precio
                if (productoSeleccionado != null) {
                    // Actualizamos el precio en el campo de texto (txtPUnit)
                    txtPUnit.setText(String.valueOf(productoSeleccionado.getPu())); // Se pone el precio en el TextField
                } else {
                    // Si el producto no se encuentra, limpiamos el precio
                    txtPUnit.clear();
                }
            }
        });
    }


    public void listar() {
        try {
            tableView.getItems().clear();
            listarProducto = FXCollections.observableArrayList(ps.list()); // Verifica que ps.list() esté devolviendo los productos correctamente
            tableView.getItems().addAll(listarProducto);
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Captura cualquier error durante el listado
        }
    }

    public void limpiarError(){
        txtNombreProducto.getStyleClass().remove("text-field-error");
        txtPUnit.getStyleClass().remove("text-field-error");
        txtPUnitOld.getStyleClass().remove("text-field-error");
        txtUtilidad.getStyleClass().remove("text-field-error");
        txtStock.getStyleClass().remove("text-field-error");
        txtStockOld.getStyleClass().remove("text-field-error");
        cbxProducto.getStyleClass().remove("text-field-error");
        cbxCategoria.getStyleClass().remove("text-field-error");
        cbxUnidMedida.getStyleClass().remove("text-field-error");
    }

    public void clearForm(){
        txtNombreProducto.setText("");
        txtPUnit.setText("");
        txtPUnitOld.setText("");
        txtUtilidad.setText("");
        txtStock.setText("");
        txtStockOld.setText("");
        cbxProducto.getSelectionModel().select(null);
        cbxCategoria.getSelectionModel().select(null);
        cbxUnidMedida.getSelectionModel().select(null);
        idProductoCE=0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion(){
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Producto>> violacionesOrdenadasPorPropiedad) {
        // Crear un LinkedHashMap para ordenar las violaciones
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();

        // Mostrar el primer mensaje de error
        for (ConstraintViolation<Producto> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if(campo.equals("nombre")) {
                erroresOrdenados.put("nombre", violacion.getMessage());
                txtNombreProducto.getStyleClass().add("text-field-error");
            } else if (campo.equals("pu")) {
                erroresOrdenados.put("pu", violacion.getMessage());
                txtPUnit.getStyleClass().add("text-field-error");
            } else if (campo.equals("puOld")) {
                erroresOrdenados.put("puold", violacion.getMessage());
                txtPUnitOld.getStyleClass().add("text-field-error");
            } else if (campo.equals("utilidad")) {
                erroresOrdenados.put("utilidad", violacion.getMessage());
                txtUtilidad.getStyleClass().add("text-field-error");
            } else if (campo.equals("stock")) {
                erroresOrdenados.put("stock", violacion.getMessage());
                txtStock.getStyleClass().add("text-field-error");
            } else if (campo.equals("stockOld")) {
                erroresOrdenados.put("stockold", violacion.getMessage());
                txtStockOld.getStyleClass().add("text-field-error");
            } else if (campo.equals("Producto")) {
                erroresOrdenados.put("Producto", violacion.getMessage());
                cbxProducto.getStyleClass().add("text-field-error");
            } else if (campo.equals("categoria")) {
                erroresOrdenados.put("categoria", violacion.getMessage());
                cbxCategoria.getStyleClass().add("text-field-error");
            } else if (campo.equals("unidadMedida")) {
                erroresOrdenados.put("unidadmedida", violacion.getMessage());
                cbxUnidMedida.getStyleClass().add("text-field-error");
            }
        }

        // Verificar si hay errores antes de acceder al primer error
        if (!erroresOrdenados.isEmpty()) {
            // Mostrar el primer error en el orden deseado
            Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(primerError.getValue()); // Mostrar el mensaje del primer error
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        } else {
            // Si no hay errores, mostrar un mensaje de éxito o vacío
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        }
    }

    @FXML
    public void validarFormulario() {
        formulario = new Producto();
        formulario.setNombre(txtNombreProducto.getText());
        formulario.setPu(Double.parseDouble(txtPUnit.getText().isEmpty() ? "0" : txtPUnit.getText()));
        formulario.setPuOld(Double.parseDouble(txtPUnitOld.getText().isEmpty() ? "0" : txtPUnitOld.getText()));
        formulario.setUtilidad(Double.parseDouble(txtUtilidad.getText().isEmpty() ? "0" : txtUtilidad.getText()));
        formulario.setStock(Double.parseDouble(txtStock.getText().isEmpty() ? "0" : txtStock.getText()));
        formulario.setStockOld(Double.parseDouble(txtStockOld.getText().isEmpty() ? "0" : txtStockOld.getText()));

        String idxM = cbxProducto.getSelectionModel().getSelectedItem() == null ? "0" : cbxProducto.getSelectionModel().getSelectedItem().getKey();
        formulario.setMarca(ms.searchById(Long.parseLong(idxM)));

        String idxC = cbxCategoria.getSelectionModel().getSelectedItem() == null ? "0" : cbxCategoria.getSelectionModel().getSelectedItem().getKey();
        formulario.setCategoria(cs.searchById(Long.parseLong(idxC)));

        String idxUM = cbxUnidMedida.getSelectionModel().getSelectedItem() == null ? "0" : cbxUnidMedida.getSelectionModel().getSelectedItem().getKey();
        formulario.setUnidadMedida(ums.searchById(Long.parseLong(idxUM)));

        Set<ConstraintViolation<Producto>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Producto>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Si no hay violaciones, el formulario es válido
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            // Verificar si estamos actualizando o insertando un producto
            if (idProductoCE != 0L && idProductoCE > 0L) {
                formulario.setIdProducto(idProductoCE);
                ps.update(formulario); // Asegúrate de que el método update funcione correctamente
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
                clearForm();
            } else {
                ps.save(formulario); // Asegúrate de que el método save funcione correctamente
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, stage.getWidth() / 1.5, stage.getHeight() / 2);
                clearForm();
            }

            // Recargar la tabla para mostrar los productos actualizados
            listar(); // Asegúrate de que este método actualice la lista en la tabla
        } else {
            // Si hay violaciones, se validan los errores
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }


    private void filtrarProductos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            // Si el filtro está vacío, volver a mostrar la lista completa
            tableView.getItems().clear();
            tableView.getItems().addAll(listarProducto);
        } else {
            // Aplicar el filtro solo para bebidas y golosinas
            String lowerCaseFilter = filtro.toLowerCase();
            List<Producto> productosFiltrados = listarProducto.stream()
                    .filter(producto -> {
                        if (producto.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        if (producto.getMarca().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false; // Si no coincide con ningún campo
                    })
                    .collect(Collectors.toList());

            // Actualizar los items del TableView con los productos filtrados
            tableView.getItems().clear();
            tableView.getItems().addAll(productosFiltrados);
        }
    }

    public void editForm(Producto producto){
        txtNombreProducto.setText(producto.getNombre());
        txtPUnit.setText(producto.getPu().toString());
        txtPUnitOld.setText(producto.getPuOld().toString());
        txtUtilidad.setText(producto.getUtilidad().toString());
        txtStock.setText(producto.getStock().toString());
        txtStockOld.setText(producto.getStockOld().toString());
// Seleccionar el ítem en cbxMarca según el ID de Marca
        cbxProducto.getSelectionModel().select(
                cbxProducto.getItems().stream()
                        .filter(marca -> Long.parseLong(marca.getKey())==producto.getMarca().getIdMarca())
                        .findFirst()
                        .orElse(null)
        );
// Seleccionar el ítem en cbxCategoria según el ID de Categoria
        cbxCategoria.getSelectionModel().select(
                cbxCategoria.getItems().stream()
                        .filter(categoria -> Long.parseLong(categoria.getKey())==producto.getCategoria().getIdCategoria())
                        .findFirst()
                        .orElse(null)
        );
// Seleccionar el ítem en cbxUnidMedida según el ID de Unidad de Medida
        cbxUnidMedida.getSelectionModel().select(
                cbxUnidMedida.getItems().stream()
                        .filter(unidad -> Long.parseLong(unidad.getKey())==producto.getUnidadMedida().getIdUnidad())
                        .findFirst()
                        .orElse(null)
        );
        idProductoCE=producto.getIdProducto();
        limpiarError();
    }


}