# Módulo de Programación - Virtual Tech Seller

Este directorio documenta la implementación en Java de la lógica de negocio de la aplicación **Virtual Tech Seller**, desarrollada para el módulo **Programación**.

## ¿Qué hace la aplicación?

**Virtual Tech Seller** es una aplicación de escritorio diseñada para gestionar exposiciones temporales de artículos tecnológicos. A nivel de programación, es un sistema que implementa una arquitectura basada en **Programación Orientada a Objetos (POO)** para modelar entidades del mundo real (Usuarios, Administradores, Productos Nuevos, Productos Usados y Eventos).

El software permite mantener un catálogo persistente y proporciona una interfaz gráfica para que distintos tipos de usuarios interactúen con dicho catálogo (comprando, gestionando stock o analizando ventas) mediante reglas de negocio estrictas.

## Funciones Principales

El código fuente demuestra el uso de fundamentos de programación (condicionales, bucles, modularización y colecciones), estructurando las siguientes funcionalidades:

* **Gestión de Sesiones (Login/Register):** Sistema de control de acceso basado en el rol de usuario (`ADMIN`, `MODERATOR`, `CLIENT`).
* **Validación Estricta de Formularios:** Implementación de utilidades personalizadas (ej. `FormularyHelper.java`) para garantizar la integridad de los datos en los registros y formularios. Se validan:
  * Coincidencia de contraseñas dobles y formato seguro (procesamiento de caracteres ASCII para exigir mayúsculas, minúsculas, números y símbolos).
  * Formatos de email mediante reglas lógicas y Expresiones Regulares (Regex) para los números de teléfono.
  * Lógica temporal de eventos (fechas de inicio y fin coherentes respecto al día actual).
* **Operaciones sobre Productos:** Interfaz para crear, leer y modificar artículos del catálogo.
* **Operaciones CRUD sobre Carrito:** Permite crear, leer, modificar y eliminar artículos de los eventos a un carrito personal para cada usuario por cada compra.
* **Sistema de seguridad de compras:** Lógica transaccional que permite a los clientes añadir productos, calcular subtotales y efectuar compras seguras (controlando que el stock sea suficiente en tiempo real).
* **Análisis de Datos:** Procesamiento de registros para calcular ingresos por evento y conteo de visitas de usuarios, procesado mediante colecciones y filtros en Java.
* **Intercambio XML:** Serialización y deserialización de objetos Java hacia/desde ficheros XML mediante JAXB.

### Uso de Polimorfismo en la Lógica de Negocio
El proyecto hace un uso extensivo de la herencia y el polimorfismo. Un claro ejemplo es la clase abstracta `Product` y su método abstracto `calculateUnitPrice()`. Las clases hijas implementan este cálculo de manera distinta según su naturaleza:
* `NewProduct`: Retorna el precio base aplicándole el IVA estipulado (21%).
* `UsedProduct`: Retorna el precio base aplicándole el descuento dinámico por tara o desgaste, ignorando los impuestos de productos nuevos.
Esto permite al carrito de la compra iterar sobre una lista genérica de `Product` y calcular los precios finales automáticamente sin necesidad de usar estructuras condicionales (`instanceof`).
## ¿Qué parte usa la Base de Datos? (Conexión JDBC)

El núcleo persistente de la aplicación no simula datos en memoria, sino que está **conectado a MariaDB a través de JDBC** (`mysql-connector-j`). 

La base de datos se utiliza activamente en:
1. **Autenticación:** Validando el usuario y comparando el hash de la contraseña mediante consultas `SELECT`.
2. **Consultas Complejas (Joins):** Para listar productos que pertenecen a una exposición específica (tabla N:M `products_exhibitions`).
3. **Manejo de Stock (Updates):** Cuando un cliente confirma el carrito, Java lanza sentencias `UPDATE` para restar el stock en la base de datos de manera segura y registra la venta con un `INSERT` en la tabla `sales`.
4. **Herencia Mapeada:** La jerarquía polimórfica de Java (la clase abstracta `Product` de la que heredan `NewProduct` y `UsedProduct`) se mapea en base de datos leyendo de tres tablas distintas y construyendo los objetos correspondientes en memoria.

### Gestión de Errores y Seguridad (Principio de Mínimo Privilegio)
Se ha implementado una capa sólida de control de excepciones.
* **Excepciones Personalizadas:** Se han creado clases propias como `DuplicateNameException` o `DBConnectionException` que extienden de `Exception` para envolver los errores SQL (`SQLException`) y ofrecer un manejo limpio a las capas superiores sin exponer el *Stack Trace* al usuario.
* **Seguridad JDBC:** La clase `ConnectionManager` gestiona la conexión a la base de datos inyectando credenciales. El diseño de base de datos asegura el principio de otorgar el mínimo privilegio (ej: el usuario Moderador solo tiene permisos `SELECT`, por lo que si alguien interceptase su sesión, no podría alterar la base de datos).

## Instrucciones de Ejecución

Para ejecutar el código fuente y levantar la aplicación:

1. Asegúrate de tener **JDK 25** instalado en tu sistema.
2. Inicia el servidor MariaDB localmente en el puerto `3306`.
3. Verifica que la base de datos `virtual_tech_seller_db` esté creada y poblada (usando los scripts de la carpeta `/sql`).
4. Importa el proyecto en tu IDE (ej. IntelliJ IDEA o Eclipse) como un proyecto **Maven** para que se descarguen las dependencias (`pom.xml`), especialmente JavaFX y el conector JDBC.
5. Modos de ejecución de la Aplicación
  
 a.  Ejecuta directamente la clase `Main.java` para arrancar la interfaz de la aplicación desde un IDE como IntelliJ.
 
 b. Acceder a la carpeta (`/virtual-tech-seller/out/artifacts/virtualTechSeller`) 
 - ejecutar el siguiente comando en la terminal:
   ```bash
   java -jar /virtualTechSeller.jar
   ```
c. Si tienes un sistema operativo Windows puedes descargarlo desde el enlace:

🔗 **https://drive.google.com/file/d/1qGUDTLOCVHnNUY8RbWItKNqms90Y6afx/view?usp=sharing** 
- Descomprime con winrar y ejecuta `virtualTechSeller.exe` teniendo previamente levantado y configurado el servidor.

*(Nota: Para acceder a la app y probar todas sus vistas, puedes ser administrador utilizando el usuario predefinido `admin1@virtualtechseller.com` con contraseña `Admin-123`, o ser un cliente mediante `client1@virtualtechseller.com` con contraseña `Client-123` o incluso ser moderador a traves de `moderator1@virtualtechseller.com` con contraseña `Moderator-123`).
*

### Organización del Código Fuente
El código (situado en `src/main/java`) está estrictamente modularizado por responsabilidades (Separación de Conceptos):

* `app/`: Clases con datos de estado y acceso global para el funcionamiento general de la aplicación.
* `controller/`: Gestión de eventos y vinculación lógica de la interfaz gráfica (JavaFX).
* `database/`: Clases de configuración y conexión directa con el motor MariaDB (`ConnectionManager`).
* `dto/`: Objetos de Transferencia de Datos (*Data Transfer Objects*) utilizados para el intercambio ligero de información con las vistas del usuario.
* `exception/`: Clases con excepciones personalizadas de errores específicos (ej. fallos de base de datos o validación).
* `model/`: Clases de dominio que representan las entidades del negocio usando POO pura (herencia, abstracción).
* `repository/` (DAO): Clases exclusivas para la ejecución de consultas y sentencias persistentes (JDBC).
* `service/`: Gestiona la lógica de negocio pura, actuando como intermediario entre los repositorios y los controladores (hasheo de contraseñas, validaciones lógicas de compra, etc).
* `util/`: Clases estáticas con herramientas de uso genérico (validadores de formularios como `FormularyHelper`, gestores de transiciones entre *Scenes* de JavaFX, etc).