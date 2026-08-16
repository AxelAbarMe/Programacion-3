# Características de Java y Fundamentos de UX

## Características de Java

Es preferible tener muchas clases pequeñas, funcionales y eficientes, en lugar de pocas clases grandes y saturadas de código, debido a que esto facilita la mantenibilidad del software.

### Pilares del lenguaje

* **Encapsulamiento:** Ocultar los detalles internos de una clase y exponer solo lo necesario.
* **Abstracción:** Representar entidades complejas mediante sus características esenciales.
* **Reutilización:** Aprovechar código ya existente en nuevas implementaciones.
* **No hay punteros:** Java no permite el manejo directo de direcciones de memoria como en C/C++.
* **Todo son referencias:** Las variables de tipo objeto almacenan referencias a la ubicación del objeto en memoria, no el objeto en sí.
* **Garbage Collector:** Mecanismo automático que libera la memoria ocupada por objetos que ya no están en uso, evitando que el programador deba gestionarla manualmente.

### Clases y tipos de datos

* **Tipos primitivos:** Tipos de datos básicos del lenguaje (int, double, char, boolean, entre otros).
* **Clases envoltorio (Wrapper Classes):** Representan los tipos primitivos como objetos, por ejemplo `Integer`, `Double` y `Character`.

### Herencia

* **Herencia simple:** Se implementa mediante la palabra clave `extends`; una clase solo puede heredar de una única superclase.
* **Interfaces:** Se implementan mediante la palabra clave `implements`, permitiendo que una clase adopte múltiples comportamientos.
* **Polimorfismo:** Capacidad de un objeto de comportarse de distintas formas según el contexto, logrado mediante la sobreescritura de métodos (`override`).

### Paquetes

* Se utilizan las palabras clave `package` (para definir un paquete) e `import` (para utilizar clases de otros paquetes).
* La API de Java está organizada internamente en paquetes, agrupando clases relacionadas por funcionalidad.

### Diseño y portabilidad

* **Verificador de Bytecode:** Componente de la JVM que valida el bytecode antes de ejecutarlo, garantizando que sea seguro.
* **Portabilidad mediante la JVM:** El mismo bytecode puede ejecutarse en cualquier sistema operativo que cuente con una Máquina Virtual de Java (JVM), sin necesidad de recompilar.
* **APIs robustas y multiplataforma:** Java ofrece un conjunto amplio de librerías estándar que funcionan de forma consistente en distintos entornos.

### Genéricos (Generics)

* Permiten definir clases, interfaces y métodos que operan con tipos de datos parametrizables, aumentando la reutilización y la seguridad de tipos en tiempo de compilación.
* Sintaxis típica: `Map<K, V>`

### Evolución del lenguaje

* **Lambdas:** Expresiones que permiten escribir funciones de forma concisa, sin necesidad de almacenar estado, y que se evalúan en tiempo de ejecución.
* **Streams:** Abstracción para procesar secuencias de datos (colecciones) de forma declarativa, facilitando operaciones como filtrado, transformación y agregación.

---

## UX, Usabilidad y Accesibilidad

### Experiencia de Usuario (UX)

La UX describe cómo interactúa un usuario con un producto. Para evaluarla, se deben responder preguntas como:

* ¿El usuario queda satisfecho?
* ¿La interacción es eficiente?
* ¿Qué tan pronunciada es la curva de aprendizaje?
* ¿Cómo maneja el sistema los errores? ¿Qué tan amigable resulta para el usuario?

La experiencia debe considerarse de forma integral y completa. Además, debe evolucionar constantemente y ser validada de forma periódica con los propios usuarios, para evitar ofrecer productos que no satisfagan sus necesidades reales.

### Principios de UX

* Enfocarse en el usuario.
* Mantener consistencia en el diseño.
* Buscar un diseño sencillo.
* Respetar las raíces o identidad del producto.
* Adaptarse al usuario, no forzar al usuario a adaptarse al producto.
* Priorizar la facilidad de uso.
* Realizar pruebas constantemente.
* Aplicar el principio de "menos es más".

En resumen: **preguntar, validar, probar y ejecutar.**

### Usabilidad

Un producto puede ser sencillo, económico o elegante, pero el usuario que lo utiliza puede tener necesidades complejas, sofisticadas o, por el contrario, buscar simplicidad.

#### Principios de usabilidad

* Curva de aprendizaje.
* Eficiencia de uso.
* Capacidad de recordar cómo usar el producto (memoria).
* Manejo y prevención de errores.
* Satisfacción general del usuario.

#### Usabilidad vs. Utilidad vs. Útil

* **Utilidad:** Que el producto cumpla la función para la cual fue diseñado.
* **Usabilidad:** Qué tan fácil resulta usar el producto.
* **Útil = Usabilidad + Utilidad:** Un producto verdaderamente útil combina ambas cualidades.

#### ¿Cuándo aplicar principios de usabilidad?

* Al observar el comportamiento real de los usuarios.
* Al analizar y aprender de la competencia.
* Durante la creación de prototipos.
* Al migrar hacia una nueva versión del producto.
* Incluso cuando el producto ya cuenta con su versión más reciente (evaluación continua).

### Heurísticas de usabilidad

* Visibilidad del estado del sistema.
* Coincidencia entre el sistema y el mundo real (contexto del usuario).
* Control y libertad del usuario.
* Flexibilidad y eficiencia de uso.
* Simplicidad en el diseño.
* Permitir que el usuario pueda salir o corregir errores por cuenta propia de forma sencilla.

### Accesibilidad

Consiste en diseñar productos considerando a personas con distintos tipos de discapacidad o limitación, abarcando aspectos:

* Visuales
* Auditivos
* Sensitivos
* Cognitivos
* Motrices
* De diseño

Estas limitaciones pueden ser **permanentes**, **incidentales** (temporales) o **ambientales** (causadas por el entorno o contexto en el que se usa el producto).
