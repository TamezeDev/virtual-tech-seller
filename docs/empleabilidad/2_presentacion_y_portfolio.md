# Portfolio: Virtual Tech Seller
**Módulo:** Itinerario Personal para la Empleabilidad

---

## Presentación Profesional del Proyecto

### ¿Qué es el proyecto?
**Virtual Tech Seller** es una aplicación de escritorio desarrollada íntegramente en Java. Su objetivo es gestionar exposiciones temporales de artículos tecnológicos, administrando un catálogo de productos (nuevos o usados), las ventas, el control de stock y el acceso de distintos perfiles de empleados y clientes.

### ¿Qué problema resuelve?
Actualmente, muchos minoristas de hardware y organizadores de eventos tecnológicos gestionan su inventario, ventas y analíticas utilizando hojas de cálculo desconectadas o software obsoleto. **Virtual Tech Seller** resuelve esto unificando todo en un único sistema centralizado y persistente. Evita la sobreventa de productos mediante el control de concurrencia de stock en tiempo real, asegura la trazabilidad de cada compra y protege los datos mediante encriptación y roles de acceso estandarizados.

### ¿Para quién está pensado?
Está diseñado para **empresas del sector tecnológico y organizadores de eventos** (como ferias  o exposiciones de hardware) que necesitan una herramienta robusta en el backend para gestionar su almacén, y a la vez una interfaz intuitiva para que sus clientes puedan ojear el catálogo y realizar compras en el propio evento.

### Stack Tecnológico Utilizado
* **Backend:** Java 25 (Programación Orientada a Objetos avanzada, Patrones Singleton y Factory).
* **Persistencia de Datos:** MariaDB / MySQL conectado mediante JDBC nativo.
* **Interfaz Gráfica:** JavaFX (OpenJFX) con FXML.
* **Seguridad:** Librería BCrypt para el hasheo seguro de contraseñas.
* **Intercambio de Datos:** JAXB para la importación/exportación de catálogos en formato XML (validados con XSD).
* **Herramientas de Construcción:** Apache Maven para la inyección de dependencias.

## Galería y Demostración Funcional (Portfolio)

A continuación, se exponen las funcionalidades clave del sistema a través de su interfaz:

### Acceso y Seguridad (Gestión de Sesiones)
El sistema cuenta con una pasarela de acceso que discrimina entre Administradores, Moderadores y Clientes. Implementa validaciones estrictas (Regex) y contraseñas cifradas en base de datos.
![Pantalla de Inicio](../img/index.png)

### Gestión de Inventario y Base de Datos (Backend)
El núcleo de la aplicación permite a los Administradores realizar operaciones CRUD directas sobre la base de datos MariaDB. En esta vista se gestiona la adición manual de stock, modificación de precios y control de estados (Nuevo/Usado), aplicando el polimorfismo en el cálculo final de los impuestos o descuentos.
![Control de Productos](../img/products_control.png)

### Experiencia del Cliente y Catálogo Reactivo
Los clientes navegan por un catálogo visual. El sistema utiliza el Patrón Observer (`ObservableList`) para repintar la interfaz en tiempo real. Si un artículo agota su stock mientras un cliente lo quiere adquirir, la base de datos bloquea su compra mediante transacciones seguras (Rollback/Commit).
![Catálogo de Cliente](../img/catalog.png)

### Analítica de Datos y Reportería
El rol de Moderador tiene acceso a la inteligencia de negocio (BI). Mediante colecciones de Java y consultas SQL, el sistema extrae e interpola datos de ventas entre fechas y calcula el rendimiento económico de cada exposición.
![Analítica de Ventas](../img/analyst_sales.png)

## ¿Qué he aprendido desarrollándolo?

El desarrollo de este proyecto desde cero me ha permitido consolidar mis conocimientos como Desarrollador Backend:
1. **Arquitectura de Software:** He aprendido a modularizar el código separando estrictamente los Modelos, Vistas, Controladores (MVC) y la capa de Servicios y Repositorios (DAO).
2. **Integridad de Datos:** He comprendido la importancia de las *Transacciones SQL* para evitar bases de datos corruptas si un proceso falla a la mitad, así como el control de excepciones personalizadas en Java.
3. **Mapeo y Polimorfismo:** Trasladar la herencia de objetos Java (ej. `Product` heredado por `NewProduct` y `UsedProduct`) a un modelo de bases de datos relacional ha sido un desafío que me ha enseñado a estructurar la información eficientemente.
4. **Seguridad:** He interiorizado que un desarrollador nunca debe confiar en el input del usuario (implementando validadores de formularios estables) ni almacenar datos sensibles en texto plano (implementando BCrypt).
5. **Planificación y Diseño Previo:** He aprendido que lo fundamental para programar no empezar a escribir código de forma impulsiva. Todo proyecto sólido exige una fase previa exhaustiva de análisis de requisitos con el cliente, documentación y diseño de arquitectura (diagramas de clases UML, esquemas Entidad-Relación y Modelo Relacional). He comprobado que esta planificación estructurada es la única manera real de garantizar que el software final sea estable, escalable y no requiera refactorizaciones masivas a mitad del desarrollo.

## Repositorio Oficial
Todo el código fuente, scripts de base de datos (DDL/DML/DCL) y documentación técnica de la arquitectura se encuentra versionado y disponible en el siguiente repositorio:

🔗 **https://github.com/TamezeDev/virtual-tech-seller** 
