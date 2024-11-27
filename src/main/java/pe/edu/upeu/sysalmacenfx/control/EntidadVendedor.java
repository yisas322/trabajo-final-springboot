package pe.edu.upeu.sysalmacenfx.control;

public class EntidadVendedor {
    int id;
    String dni;
    String nombre;
    String telefono;
    String estado;
    String user;
     public EntidadVendedor(){

     }

    public EntidadVendedor(int id, String dni, String nombre, String telefono, String estado, String user){
        this.id=id;
        this.dni=dni;
        this.nombre=nombre;
        this.telefono=telefono;
        this.estado=estado;
        this.user=user;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
