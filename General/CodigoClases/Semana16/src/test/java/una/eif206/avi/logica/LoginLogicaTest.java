package una.eif206.avi.logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginLogicaTest {

    private LoginLogica loginLogica;


    @BeforeEach
    void setUp() throws Exception {

        loginLogica = new LoginLogica();

        loginLogica.inicializarDatos();
    }


    @Test
    void iniciarSesionConCredencialesCorrectasEnJsonDebeRetornarTrue()
            throws Exception {

        boolean resultado =
                loginLogica.iniciarSesion(
                        "admin",
                        "1234",
                        "JSON"
                );

        assertTrue(resultado);
    }


    @Test
    void iniciarSesionConCredencialesCorrectasEnXmlDebeRetornarTrue()
            throws Exception {

        boolean resultado =
                loginLogica.iniciarSesion(
                        "admin",
                        "1234",
                        "XML"
                );

        assertTrue(resultado);
    }


    @Test
    void iniciarSesionConContrasenaIncorrectaDebeRetornarFalse()
            throws Exception {

        boolean resultado =
                loginLogica.iniciarSesion(
                        "admin",
                        "incorrecta",
                        "JSON"
                );

        assertFalse(resultado);
    }


    @Test
    void iniciarSesionConUsuarioIncorrectoDebeRetornarFalse()
            throws Exception {

        boolean resultado =
                loginLogica.iniciarSesion(
                        "pedro",
                        "1234",
                        "JSON"
                );

        assertFalse(resultado);
    }


    @Test
    void iniciarSesionSinUsuarioDebeLanzarExcepcion() {

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> loginLogica.iniciarSesion(
                                "",
                                "1234",
                                "JSON"
                        )
                );

        assertEquals(
                "Debe ingresar el usuario.",
                excepcion.getMessage()
        );
    }


    @Test
    void iniciarSesionSinContrasenaDebeLanzarExcepcion() {

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> loginLogica.iniciarSesion(
                                "admin",
                                "",
                                "JSON"
                        )
                );

        assertEquals(
                "Debe ingresar la contraseña.",
                excepcion.getMessage()
        );
    }


    @Test
    void iniciarSesionSinFuenteDebeLanzarExcepcion() {

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> loginLogica.iniciarSesion(
                                "admin",
                                "1234",
                                ""
                        )
                );

        assertEquals(
                "Debe seleccionar JSON o XML.",
                excepcion.getMessage()
        );
    }


    @Test
    void iniciarSesionConFuenteInvalidaDebeLanzarExcepcion() {

        IllegalArgumentException excepcion =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> loginLogica.iniciarSesion(
                                "admin",
                                "1234",
                                "TXT"
                        )
                );

        assertEquals(
                "Fuente de datos no válida.",
                excepcion.getMessage()
        );
    }
}