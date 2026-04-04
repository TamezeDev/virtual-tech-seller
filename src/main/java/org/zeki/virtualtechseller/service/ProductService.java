package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.AppContext;
import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.exhibition.ExhibitionProductsDto;
import org.zeki.virtualtechseller.dto.product.CategoryDto;
import org.zeki.virtualtechseller.dto.product.NewProductDto;
import org.zeki.virtualtechseller.dto.product.UsedProductDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.exception.DuplicateExhibitionNameException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.product.CartItem;
import org.zeki.virtualtechseller.model.product.Category;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.repository.ProductRepository;
import org.zeki.virtualtechseller.util.AlertHelper;
import org.zeki.virtualtechseller.util.TransactionHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    public ResultService<CartItem> stockCartItems() {
        List<CartItem> cartItems = ((Client) SessionManager.getInstance().getCurrentUser()).getCartItems();
        try {
            for (CartItem cartItem : cartItems) {
                if (!productRepository.availableExhibitionStock(cartItem.getProduct(), cartItem.getExhibition(), cartItem.getQuantity())) {
                    return new ResultService<>(false, "Stock insuficiente en el evento " + cartItem.getExhibition().getName() + " del producto ", cartItem);
                }
            }
            return new ResultService<>(true, "Hay stock y disponibilidad", null);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;

        } catch (SQLException e) {
            String message = "Error obteniendo disponibilidad de los productos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }
    }

    public List<Category> getAllCategories() {
        try {
            return productRepository.getAllProductCategories();
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return Collections.emptyList();

        } catch (SQLException e) {
            String message = "Error categorías de los productos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return Collections.emptyList();
        }
    }

    public ResultService<Void> addCategory(CategoryDto categoryDto) {
        try {
            if (!productRepository.addNewCategory(categoryDto)) {
                return new ResultService<>(false, "No se pudo crear la categoría nueva");
            }
            return new ResultService<>(true, "Categoria agregada correctamente");

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, null);

        } catch (DuplicateExhibitionNameException e) {
            return new ResultService<>(false, e.getMessage()); // THROW EXCEPTION ON DUPLICATE DB NAME
        } catch (SQLException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;
        }
    }

    public ResultService<Void> addNewProduct(NewProductDto productDto, UsedProductDto usedProductDto) {
        Connection connection = null;

        try {
            connection = TransactionHelper.begin(AppContext.getInstance().getConnectionManager());

            ResultService<Void> result = setBaseProduct(connection, productDto, usedProductDto);
            if (!result.isSuccess()) {
                TransactionHelper.rollback(connection);
                return result;
            }

            result = setDeterminateProduct(connection, productDto, usedProductDto);
            if (!result.isSuccess()) {
                TransactionHelper.rollback(connection);
                return result;
            }

            connection.commit();
            return new ResultService<>(true, "Producto añadido correctamente");

        } catch (DBConnectionException e) {
            TransactionHelper.rollback(connection);
            AlertHelper.showDBConnectAlert();
            return new ResultService<>(false, "Error de conexión");

        } catch (SQLException e) {
            TransactionHelper.rollback(connection);
            AlertHelper.showSQLAlert("Error en la transacción");
            return new ResultService<>(false, "No se pudo completar la transacción");

        } finally {
            TransactionHelper.close(connection);
        }
    }

    private ResultService<Void> setBaseProduct(Connection connection, NewProductDto newProductDto, UsedProductDto usedProductDto) {
        try {
            // ADD TO PRODUCT TABLE
            int rsBaseProduct = 0;
            if (usedProductDto == null) {
                rsBaseProduct = productRepository.addProduct(connection, newProductDto, null);
            } else if (newProductDto == null) {
                rsBaseProduct = productRepository.addProduct(connection, null, usedProductDto);
            }
            if (rsBaseProduct == 0) {
                return new ResultService<>(false, "No se pudo insertar el producto");
            }
            // GET PRODUCT ID
            int idProduct = productRepository.getLastProductID(connection);
            if (idProduct == -1) {
                return new ResultService<>(false, "No se pudo obtener el id del producto insertado");
            }
            // SET USED OR NEW PRODUCT
            if (usedProductDto == null) newProductDto.setIdProduct(idProduct);
            else usedProductDto.setIdProduct(idProduct);

            return new ResultService<>(true, "OK");

        } catch (DuplicateExhibitionNameException e) {
            return new ResultService<>(false, e.getMessage()); // THROW EXCEPTION ON DUPLICATE DB NAME
        } catch (SQLException e) {
            TransactionHelper.rollback(connection);
            AlertHelper.showSQLAlert("Error en la transacción de añadir producto");
            return new ResultService<>(false, "No se pudo completar la transacción");
        }
    }

    private ResultService<Void> setDeterminateProduct(Connection connection, NewProductDto newProductDto, UsedProductDto usedProductDto) {
        try {
            int rsBaseProduct = 0;
            if (usedProductDto == null) {
                rsBaseProduct = productRepository.addNewProduct(connection, newProductDto);
            } else if (newProductDto == null) {
                rsBaseProduct = productRepository.addUsedProduct(connection, usedProductDto);
            }
            if (rsBaseProduct == 0) {
                return new ResultService<>(false, "No se pudo insertar el producto determinado");
            }

            return new ResultService<>(true, "OK");
        } catch (SQLException e) {
            TransactionHelper.rollback(connection);
            AlertHelper.showSQLAlert("Error en la transacción de añadir producto");
            return new ResultService<>(false, "No se pudo completar la transacción");

        }
    }

    public ResultService<List<ExhibitionProductsDto>> getFullProductAssociateOrNot() {

        try {
            List<ExhibitionProductsDto> result = productRepository.getFullProductAssociateOrNot();
            if (result.isEmpty()) {
                return new ResultService<>(false, "La lista de productos está vacía");
            }
            return new ResultService<>(true, "Mostrando listado de productos", result);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return null;

        } catch (SQLException e) {
            String message = "Error obteniendo disponibilidad de los productos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return null;
        }
    }

    public boolean removeEventItem(ExhibitionProductsDto exhibitionProductsDto) {
        try {
            return productRepository.removeExhibitionItem(exhibitionProductsDto);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;

        } catch (SQLException e) {
            String message = "Error eliminando el producto de la sala";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            e.printStackTrace();
            return false;
        }
    }

    public boolean decreaseEventItem(ExhibitionProductsDto exhibitionProductsDto, int quantity) {

        try {
            return productRepository.decreaseExhibitionItems(exhibitionProductsDto, quantity);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;

        } catch (SQLException e) {
            String message = "Error actualizando cantidad de productos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            e.printStackTrace();
            return false;
        }
    }


    public boolean increaseEventItem(ExhibitionProductsDto exhibitionProductsDto, Exhibition exhibition, int quantity) {

        try {
            return productRepository.increaseExhibitionItems(exhibitionProductsDto, exhibition, quantity);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;

        } catch (SQLException e) {
            String message = "Error actualizando cantidad de productos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }
}

