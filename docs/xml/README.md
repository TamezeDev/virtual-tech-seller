# Lenguajes de Marcas - XML y XSD

## Descripción y Propósito

En este módulo se ha implementado un sistema robusto de intercambio de datos en formato **XML** para el proyecto **Virtual Tech Seller**. 

El objetivo es representar el catálogo de productos de la tienda (tanto nuevos como de segunda mano) de forma estructurada, y garantizar que cualquier archivo que entre o salga del sistema cumpla estrictamente con unas reglas de negocio predefinidas mediante esquemas **XSD**.

## Archivos Incluidos en la Entrega

Se han generado dos flujos principales (Exportación e Importación), organizados en los siguientes ficheros:

### Flujo de Exportación con validación
* **`exported_products.xml`**: Un archivo XML real generado por la aplicación que contiene todo el catálogo de productos actual de la base de datos.
* **`export_products.xsd`**: El esquema que valida estrictamente cómo debe ser un archivo de exportación de la tienda.
* **`successful_export.jpg`**: Captura de pantalla de la aplicación demostrando el éxito de la exportación.

### Flujo de Importación con validación
* **`valid_import_products.xml`**: Un lote de productos externos listos para ser introducidos en el catálogo.
* **`invalid_import_products.xml`**: Un archivo alterado intencionadamente con fallos estructurales y de datos para demostrar el bloqueo de seguridad.
* **`import_products.xsd`**: El esquema de validación que actúa de "aduana" para decidir qué XML puede entrar al sistema.
* **`successful_validation.jpg`**: Captura de la aplicación leyendo y validando correctamente el XML válido.
* **`wrong_validation.jpg`**: Captura demostrando cómo el sistema detecta el XML erróneo y rechaza la importación mostrando un mensaje de error.

## ¿Qué datos representa el XML?

El esquema refleja la complejidad de nuestro catálogo, soportando herencia polimórfica:

* **Estructura Común:** Todo producto tiene un `<name>`, `<description>`, `<basePrice>`, `<urlImage>` y un nodo `<category>`.
* **Variantes (`<choice>`):**
  * `<newProduct>`: Incluye elementos exclusivos como `<stock>` (control de inventario) y `<releaseDate>`.
  * `<usedProduct>`: Incluye elementos exclusivos como `<discountPercentage>` (descuento por estado de uso) y `<remark>` (reseña del estado físico del producto).

## Restricciones y Validaciones Técnicas del XSD

Los archivos XSD diseñados (`import_products.xsd` y `export_products.xsd`) no son meramente estructurales; aplican restricciones lógicas de negocio reales:

1. **Tipos de Datos Múltiples:** Uso de `xs:string`, `xs:integer`, `xs:decimal`, `xs:boolean` y `xs:date`.
2. **Restricciones de Longitud (`xs:minLength`, `xs:maxLength`):** Por ejemplo, un nombre de producto debe tener entre 3 y 60 caracteres.
3. **Límites Numéricos (`xs:minInclusive`, `xs:maxInclusive`):** 
   * Los precios deben ser positivos.
   * Los descuentos en productos usados no pueden superar el 99.9%.
4. **Enumeraciones (`xs:enumeration`):** El nombre de la categoría en la importación está estrictamente limitado a valores exactos (ej. "Smartphones", "Videojuegos", "Componentes PC").
5. **Cardinalidades (`minOccurs`, `maxOccurs`):** Control estricto de elementos obligatorios frente a elementos opcionales.

## Integración con el Proyecto (Contexto Global)

Esta entrega no es un ejercicio aislado. Los archivos XML y XSD interactúan directamente con la base de datos y la lógica Java (POO) del Proyecto Intermodular:

* Cuando el usuario pulsa en **"Importar XML"**, Java utiliza el XSD (`import_products.xsd`) para validar el archivo.
* Si la validación falla (como se ve en `wrong_validation.jpg`), el sistema detiene el proceso y protege la integridad de la base de datos.
* Si el archivo es válido, la aplicación lee los nodos `<newProduct>` y `<usedProduct>`, instancia los objetos Java correspondientes y los expone para visualizarlos en una tabla. Si el usuario está conforme, acepta y los datos persisten en MariaDB mediante JDBC.