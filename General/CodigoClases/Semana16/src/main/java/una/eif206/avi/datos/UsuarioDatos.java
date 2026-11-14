package una.eif206.avi.datos;

import una.eif206.avi.dto.UsuarioDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDatos {

    public boolean validarCredenciales(
            UsuarioDTO usuario)
            throws SQLException {

        String sql = """
                SELECT pk_usuario
                FROM usuario
                WHERE usuario = ?
                  AND password = ?
                  AND activo = TRUE
                """;

        try (
                Connection conexion =
                        ConexionBD.obtenerConexion();

                PreparedStatement sentencia =
                        conexion.prepareStatement(sql)
        ) {

            sentencia.setString(
                    1,
                    usuario.getUsuario()
            );

            sentencia.setString(
                    2,
                    usuario.getContrasena()
            );

            try (
                    ResultSet resultado =
                            sentencia.executeQuery()
            ) {

                return resultado.next();
            }
        }
    }
}