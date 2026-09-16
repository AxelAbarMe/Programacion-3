# Resumen de Testing (JUnit 5 + Maven QA)

## Tipos de pruebas

| Tipo | Convención de nombre | Qué corre | Plugin Maven | Fase |
|:--|:--|:--|:--|:--|
| **Unitaria** (`Test`) | `*Test.java` | Lógica pura, sin tocar disco/red/API | **Surefire** | `mvn test` |
| **Integración** (`IT`) | `*IT.java` | Archivos reales (XML, PDF), llamadas HTTP | **Failsafe** | `mvn verify` |

## Anotaciones clave

| Anotación | Ejecución |
|:--|:--|
| `@Test` | Un caso de prueba individual |
| `@BeforeEach` | Antes de **cada** `@Test` (reinicia estado) |
| `@BeforeAll` | **Una sola vez**, antes de todos los `@Test` (debe ser `static`) |
| `@ParameterizedTest` + `@ValueSource` | Repite el mismo test con varios valores |
| `@EnabledIfEnvironmentVariable` | Corre solo si existe una variable de entorno |

---

## Ejemplo 1 — Prueba unitaria (`*Test.java`)

No toca archivos ni red; prueba la Lógica de forma aislada.

```java
class AuthLogicTest {

    private final AuthLogic authLogic = new AuthLogic();

    @ParameterizedTest
    @ValueSource(strings = {"Abcdefg1!", "Xy9$zzzz", "Cl4ve#Segura"})
    void satisfiesPolicy_validPassword_returnsTrue(String password) {
        assertTrue(authLogic.satisfiesPolicy(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "12345678", "ABCDEFGH1"})
    void satisfiesPolicy_invalidPassword_returnsFalse(String password) {
        assertFalse(authLogic.satisfiesPolicy(password));
    }

    @Test
    void satisfiesPolicy_nullPassword_returnsFalse() {
        assertFalse(authLogic.satisfiesPolicy(null));
    }

    @Test
    void verifyPhone_matchingPhoneNumber_returnsTrue() {
        User user = new User("111111111", "Juan Perez", "x", false);
        user.setPhoneNumber("2222-3333");
        assertTrue(authLogic.verifyPhone(user, "2222-3333"));
    }
}
```

## Ejemplo 2 — Prueba de integración (`*IT.java`) sobre XML

Usa `@BeforeEach` para reiniciar los archivos antes de cada prueba, evitando contaminación entre tests.

```java
class UserLogicIT {

    private final UserLogic userLogic = new UserLogic();

    @BeforeEach
    void resetXml() throws Exception {
        TestDataSupport.resetXmlFiles(); // limpia users.xml, categories.xml, etc.
    }

    @Test
    void addUser_validData_persists() throws Exception {
        User created = userLogic.addUser("100000000", "Juan Perez", "1111-2222");

        assertEquals("100000000", created.getId());
        assertEquals("100000000", created.getPassword()); // clave inicial = id
    }

    @Test
    void addUser_duplicateId_throwsException() throws Exception {
        userLogic.addUser("100000003", "Juan Perez", "1111-2222");
        assertThrows(Exception.class,
                () -> userLogic.addUser("100000003", "Otro Nombre", "3333-4444"));
    }

    @Test
    void updateUserInfo_validData_updatesNameAndPhone() throws Exception {
        userLogic.addUser("100000006", "Juan Perez", "1111-2222");
        assertTrue(userLogic.updateUserInfo("100000006", "Juan Actualizado", "5555-6666"));

        User found = userLogic.findUserById("100000006");
        assertEquals("Juan Actualizado", found.getName());
    }
}
```

## Ejemplo 3 — Prueba de integración con `@BeforeAll` (genera PDF real)

```java
class PrintLogicIT {

    private final PrintLogic printLogic = new PrintLogic();

    @BeforeAll
    static void createOutputDir() throws Exception {
        Files.createDirectories(Path.of("target/test-output")); // una sola vez
    }

    @Test
    void generatePdf_withItems_createsNonEmptyFile() throws Exception {
        ArrayList<CategoryCountDTO> rows = new ArrayList<>();
        rows.add(new CategoryCountDTO("Sala de Juntas", 3));

        File pdf = printLogic.generatePdf(rows, CategoryCountDTO.class, "target/test-output/prueba.pdf");

        assertTrue(pdf.exists());
        assertTrue(pdf.length() > 0);
    }

    @Test
    void generatePdf_manyItems_createsMultiplePagesWithoutError() throws Exception {
        ArrayList<CategoryCountDTO> rows = new ArrayList<>();
        for (int i = 0; i < 60; i++) rows.add(new CategoryCountDTO("Categoria " + i, i));

        File pdf = printLogic.generatePdf(rows, CategoryCountDTO.class, "target/test-output/prueba_muchas.pdf");
        assertTrue(pdf.exists());
    }
}
```

## Ejemplo 4 — Prueba de integración condicional (API externa)

Solo corre si existe la variable de entorno, para no fallar en un entorno de CI sin clave.

```java
@EnabledIfEnvironmentVariable(named = "GEMINI_API_KEY", matches = ".+")
class GeminiServiceIT {

    @Test
    void requestJSON_simplePrompt_returnsNonEmptyText() throws Exception {
        GeminiService geminiService = new GeminiService();
        JsonArray availableCategories = new JsonArray();
        availableCategories.add("cat1");

        String result = geminiService.requestJSON(
                "Necesito una sala mañana de 9am a 10am", availableCategories);

        assertNotNull(result);
        assertFalse(result.isBlank());
    }
}
```

## Clase de apoyo (`TestDataSupport`) — usada en `@BeforeEach`

```java
public final class TestDataSupport {
    public static final Path DATA_DIR = Path.of("target/test-data/resourcemanager/data");

    private TestDataSupport() {} // clase utilitaria: solo métodos estáticos

    public static void resetXmlFiles() throws Exception {
        Files.createDirectories(DATA_DIR);
        writeEmptyRoot("categories");
        writeEmptyRoot("resources");
        writeEmptyRoot("reservations");
        writeEmptyRoot("users");
    }

    private static void writeEmptyRoot(String rootTag) throws Exception {
        String xmlContent = "<?xml version=\"1.0\"?><" + rootTag + "></" + rootTag + ">";
        Files.writeString(DATA_DIR.resolve(rootTag + ".xml"), xmlContent);
    }
}
```

---

## Configuración en `pom.xml`

```xml
<properties>
    <junit.version>5.11.3</junit.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope> <!-- solo disponible al correr pruebas -->
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Surefire: pruebas UNITARIAS -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.5.2</version>
            <configuration>
                <excludes>
                    <exclude>**/*IT.java</exclude> <!-- excluye las de integración -->
                </excludes>
            </configuration>
        </plugin>

        <!-- Failsafe: pruebas de INTEGRACIÓN -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>3.5.2</version>
            <executions>
                <execution>
                    <goals>
                        <goal>integration-test</goal>
                        <goal>verify</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <includes>
                    <include>**/*IT.java</include> <!-- solo las que terminan en "IT" -->
                </includes>
                <systemPropertyVariables>
                    <!-- redirige archivos a carpeta de prueba, sin tocar datos reales -->
                    <resourcemanager.dataDir>${project.build.directory}/test-data</resourcemanager.dataDir>
                </systemPropertyVariables>
            </configuration>
        </plugin>
    </plugins>
</build>
```

> **Idea central:** Surefire garantiza feedback rápido en cada compilación (`*Test`), mientras que Failsafe valida el sistema completo contra archivos/red reales solo en la fase `verify` (`*IT`), sin frenar el ciclo normal de desarrollo.
