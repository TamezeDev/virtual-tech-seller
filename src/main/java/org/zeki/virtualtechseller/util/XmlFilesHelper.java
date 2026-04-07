package org.zeki.virtualtechseller.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import org.zeki.virtualtechseller.model.product.NewProduct;
import org.zeki.virtualtechseller.model.product.Product;
import org.zeki.virtualtechseller.model.product.ProductsXML;
import org.zeki.virtualtechseller.model.product.UsedProduct;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.List;

public final class XmlFilesHelper {
    private XmlFilesHelper() {
    }

    public static ProductsXML importProducts(ActionEvent event, Label feedbackLabel) {

        try {
            // SELECT YOUR XML FILE
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccionar productos");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
            Node parent = (Node) event.getSource();
            Stage stage = (Stage) parent.getScene().getWindow();
            File xmlFile = fileChooser.showOpenDialog(stage);
            File xsdFile = new File("docs/test_app/xml_xsd/import_products.xsd");
            // CREATE SCHEMA VALIDATION
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile);
            // CREATE OBJECT CLASS
            JAXBContext context = JAXBContext.newInstance(ProductsXML.class, NewProduct.class, UsedProduct.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);

            return (ProductsXML) unmarshaller.unmarshal(xmlFile);

        } catch (IllegalArgumentException e) {
            feedbackLabel.setText("Operación cancelada");
            Feedback.showFeedback(feedbackLabel);
        } catch (SAXException e) {
            feedbackLabel.setText("Formato de documento incompatible");
            Feedback.showFeedback(feedbackLabel);
            return new ProductsXML();
        } catch (JAXBException e) {
            feedbackLabel.setText("Error al validar el documento xml");
            Feedback.showFeedback(feedbackLabel);
        }

        return new ProductsXML();
    }

    public static void exportProducts(ActionEvent event, Label feedbackLabel, List<Product> products) {
        try {
            // SELECT WHERE SAVE XML FILE
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar productos");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")
            );
            fileChooser.setInitialFileName("exported_products.xml");
            Node parent = (Node) event.getSource();
            Stage stage = (Stage) parent.getScene().getWindow();
            File xmlFile = fileChooser.showSaveDialog(stage);
            // CREATE SCHEMA VALIDATION
            File xsdFile = new File("docs/test_app/xml_xsd/export_products.xsd");
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile);
            ProductsXML productsXML = new ProductsXML();
            productsXML.setProducts(products);
            // EXTRACT OBJECTS
            JAXBContext context = JAXBContext.newInstance(ProductsXML.class, NewProduct.class, UsedProduct.class);
            Marshaller marshaller;
            marshaller = context.createMarshaller();
            marshaller.setSchema(schema);

            marshaller.marshal(productsXML, xmlFile);

            feedbackLabel.setText("Productos exportados correctamente");
            Feedback.showFeedback(feedbackLabel);

        } catch (IllegalArgumentException e) {
            feedbackLabel.setText("Operación cancelada");
            Feedback.showFeedback(feedbackLabel);
        } catch (SAXException e) {
            feedbackLabel.setText("Error con el esquema XSD");
            Feedback.showFeedback(feedbackLabel);
        } catch (JAXBException e) {
            feedbackLabel.setText("Error al exportar el XML");
            e.printStackTrace();
            Feedback.showFeedback(feedbackLabel);
        }
    }
}
