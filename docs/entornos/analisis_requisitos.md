# Acta de Reunión y Análisis de Requisitos
**Proyecto:** Virtual Tech Seller  
**Fecha de la reunión:** 2 de marzo de 2026  
**Asistentes:** Equipo de Desarrollo y Cliente  
**Objetivo:** Toma de requisitos para el desarrollo de la nueva plataforma de ventas tecnólogicas por eventos.

---

## Contexto Comercial
El cliente solicita una aplicación de escritorio para digitalizar su modelo de negocio. Actualmente venden productos tecnológicos (tanto nuevos como de segunda mano) y organizan "Exposiciones" o eventos de venta temporal. Necesitan un sistema centralizado que permita a los administradores gestionar el inventario, a los clientes realizar compras seguras, y a los moderadores analizar el rendimiento de cada evento.

Tras la reunión con el cliente, se han extraído y consensuado los siguientes requisitos para el desarrollo de la aplicación:

##  Requisitos Funcionales (Lógica de Negocio)

### Gestión de Usuarios y Roles
El sistema contará con 3 niveles de privilegios:
1. **Administradores:** Tienen el control total. Registran a otros administradores, moderadores y clientes. Son los encargados de subir/eliminar artículos de las exposiciones.
2. **Moderadores:** Perfil analítico. Pueden consultar y filtrar cómo van las ventas de cada exposición y auditar las visitas registradas en los eventos.
3. **Clientes:** Pueden registrarse en la plataforma, pero su cuenta requiere la autorización/activación de un administrador para acceder. Pueden ver catálogos, acceder a exposiciones, recargar su saldo (crédito) y realizar compras.

### Catálogo de Productos
* Los artículos pertenecen a **Categorías** predefinidas.
* Existen dos variantes de productos en el modelo de negocio:
  * **Productos Nuevos:** Tienen control de stock y fecha de lanzamiento.
  * **Productos Usados:** Tienen un descuento aplicado según el estado en el que se encuentren (observaciones/taras).

### Exposiciones y Ventas
* Las ventas se organizan por **Exposiciones** (eventos temporales).
* Los clientes pueden acceder a la exposición y ojear el catálogo disponible para efectuar compras.
* El sistema debe registrar un historial de ventas para saber exactamente qué se vende en cada exposición.
* El sistema debe registrar las visitas de los usuarios a los eventos para su posterior análisis estadístico (labor del moderador).
* Los clientes tendrán acceso a un historial personal con sus productos comprados.

### Funcionalidades Críticas y de Alta Prioridad (MPO)
* **Gestión de Carrito y Concurrencia:** Los clientes usarán un carrito de la compra. Si un usuario añade un artículo pero otro cliente se adelanta y lo agota, o un administrador lo retira del evento, el sistema debe bloquear la compra y actualizar el carrito del usuario al stock real disponible.
* **Seguridad de Datos:** Las contraseñas no pueden ser visibles en la base de datos bajo ninguna circunstancia. Deben estar protegidas mediante encriptación (BCrypt).
* **Limitaciones de Edición (Reglas de Negocio):** 
  * Los administradores no pueden alterar los datos principales de un evento que ya ha comenzado o ha finalizado.
  * Para los productos ya registrados, solo se podrá modificar el precio o incrementar el stock (si es nuevo). El nombre y sus características base serán inmutables para mantener la coherencia del histórico de ventas.

## Requisitos No Funcionales (GUI)

* **Entorno:** La interfaz debe estar optimizada para equipos de escritorio o laptops (Desktop App).
* **Diseño visual:** El cliente exige una portada atractiva y llamativa.
* **Navegabilidad:** Los menús deben contar con botones de fácil acceso y tiempos de carga rápidos entre las distintas pantallas.
* **Multimedia:** Todos los productos deben mostrar su nombre, descripción y una imagen representativa. En caso de no tener acceso a ella, el sistema asignará una imagen genérica automáticamente.
* **Integración tecnológica:** El diseño general debe ser coherente con el entorno tecnológico actual (Java, BBDD Relacional).

## Requisitos de Diseño y Arquitectura (Requisitos Técnicos)

Para asegurar la mantenibilidad y escalabilidad del proyecto, el equipo de desarrollo acuerda los siguientes estándares técnicos:

1. **Paradigma POO:** Uso coherente de herencia y polimorfismo. Se implementarán interfaces que detallen los contratos de comportamiento de los objetos.
2. **Intercambio de Datos:** Se implementará una función para exportar/importar artículos mediante ficheros **XML**. Estos ficheros deberán ser validados obligatoriamente contra un esquema **XSD** antes de permitir su inserción en la base de datos.
3. **Estructura del Proyecto:** Arquitectura organizada por paquetes (ej. MVC o similar). Cada clase tendrá una **única responsabilidad** (SRP). Por ejemplo: los controladores gestionarán las vistas y los servicios acudirán a los repositorios/DAO para interactuar con la Base de Datos.
4. **Documentación:** Se detallarán de forma explícita las dependencias utilizadas (Drivers JDBC, librerías, etc.) en el informe final del proyecto.

---
*Documento aprobado para iniciar la fase de diseño de Base de Datos y Arquitectura de Software.*