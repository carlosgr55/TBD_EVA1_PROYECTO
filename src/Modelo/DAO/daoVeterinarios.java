package Modelo.DAO;

import Conexion.Conexion;
import Excepciones.ClientesExcepciones;
import Excepciones.UserNotFound;
import Modelo.VO.Cliente;
import Modelo.VO.Veterinario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class daoVeterinarios {

    private final static Connection conexion = Conexion.getConexion();

    public static void agregar(Veterinario veterinario) throws SQLException {
        String usuario = veterinario.getUsuario();
        String password = veterinario.getPassword();
        String nombre = veterinario.getNombre();
        String apellido = veterinario.getApellido();
        String telefono = veterinario.getTelefono();
        String correo = veterinario.getCorreo();
        double sueldo = veterinario.getSueldo();
        String cedula = veterinario.getCedula();

        PreparedStatement pst = conexion.prepareStatement("INSERT INTO veterinarios (usuario, password, "
                + "nombre, apellido, sueldo, cedula, telefono, correo ) "
                + "VALUES (?,?,?,?,?,?,?,?)");
        pst.setString(1, usuario.trim());
        pst.setString(2, password.trim());
        pst.setString(3, nombre.trim());
        pst.setString(4, apellido.trim());
        pst.setDouble(5, sueldo);
        pst.setString(6, cedula.trim());
        pst.setString(7, telefono.trim());
        pst.setString(8, correo.trim());
        pst.executeUpdate();
    }

    public static boolean existe(String user) throws SQLException {
        PreparedStatement pst = conexion.prepareStatement("SELECT * FROM veterinarios where usuario= '" + user + "'");
        ResultSet rst = pst.executeQuery();
        return rst.next();
    }

    public static Veterinario consulta(String user) throws SQLException, UserNotFound {
        Veterinario veterinario = null;
        PreparedStatement pst = conexion.prepareStatement("SELECT * FROM veterinarios WHERE usuario = '" + user + "'");
        ResultSet rst = pst.executeQuery();
        if (rst.next()) {
            String usuario = rst.getString("usuario");
            String password = rst.getString("password");
            String nombre = rst.getString("nombre");
            String apellido = rst.getString("apellido");
            String telefono = rst.getString("telefono");
            String correo = rst.getString("correo");
            double sueldo = rst.getDouble("sueldo");
            String cedula = rst.getString("cedula");

            veterinario = new Veterinario(cedula, sueldo, usuario, password, nombre, apellido, telefono, correo);

            return veterinario;
        } else {
            throw new UserNotFound("Veterinario no encontrado");
        }
    }

    public static void borrar(String user) throws SQLException {
        PreparedStatement pst = conexion.prepareStatement("DELETE FROM veterinarios WHERE usuario = '" + user + "'");
        pst.executeUpdate();
    }

    public static void modificar(Veterinario vetNuevo, String user) throws SQLException {
        String usuario = vetNuevo.getUsuario();
        String password = vetNuevo.getPassword();
        String nombre = vetNuevo.getNombre();
        String apellido = vetNuevo.getApellido();
        String telefono = vetNuevo.getTelefono();
        String correo = vetNuevo.getCorreo();
        double sueldo = vetNuevo.getSueldo();
        String cedula = vetNuevo.getCedula();

        String statement = "UPDATE veterinarios "
                + "SET usuario = ?, password = ?, nombre = ?, apellido = ?, telefono = ?, "
                + "correo = ?, sueldo = ?, cedula = ? "
                + "WHERE usuario = ?;";

        PreparedStatement pst = conexion.prepareStatement(statement);
        pst.setString(1, usuario);
        pst.setString(2, password);
        pst.setString(3, nombre);
        pst.setString(4, apellido);
        pst.setString(5, telefono);
        pst.setString(6, correo);
        pst.setDouble(7, sueldo);
        pst.setString(8, cedula);
        pst.setString(9, user);

        pst.executeUpdate();
    }

    public static ArrayList<Veterinario> getVeterinarios() throws SQLException {
        ArrayList<Veterinario> lista = new ArrayList<>();

        PreparedStatement pst = conexion.prepareStatement("SELECT * FROM veterinarios");
        ResultSet rst = pst.executeQuery();

        while (rst.next()) {
            String usuario = rst.getString("usuario");
            String password = rst.getString("password");
            String nombre = rst.getString("nombre");
            String apellido = rst.getString("apellido");
            String telefono = rst.getString("telefono");
            String correo = rst.getString("correo");
            double sueldo = rst.getDouble("sueldo");
            String cedula = rst.getString("cedula");
            lista.add(new Veterinario(cedula, sueldo, usuario, password, nombre, apellido, telefono, correo));
        }
        return lista;
    }

    public static ArrayList<String> veterinariosPotenciales() throws SQLException {
        ArrayList<String> lista = new ArrayList<>();
        PreparedStatement pst = conexion.prepareStatement("SELECT usuario FROM veterinarios");

        ResultSet rst = pst.executeQuery();

        while (rst.next()) {
            lista.add(rst.getString("usuario"));
        }
        return lista;
    }

}
