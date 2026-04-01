package org.zeki.virtualtechseller.service;

import org.zeki.virtualtechseller.app.SessionManager;
import org.zeki.virtualtechseller.dto.AccessUserDto;
import org.zeki.virtualtechseller.dto.ExhibitionAccessDto;
import org.zeki.virtualtechseller.exception.DBConnectionException;
import org.zeki.virtualtechseller.exception.DuplicateExhibitionNameException;
import org.zeki.virtualtechseller.model.exhibition.Exhibition;
import org.zeki.virtualtechseller.model.exhibition.ExhibitionItem;
import org.zeki.virtualtechseller.model.user.Client;
import org.zeki.virtualtechseller.model.user.User;
import org.zeki.virtualtechseller.repository.ExhibitionRepository;
import org.zeki.virtualtechseller.util.AlertHelper;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionService() {
        this.exhibitionRepository = new ExhibitionRepository();
    }

    public ResultService<List<Exhibition>> getAllExhibitions() {
        try {
            List<Exhibition> exhibitions = exhibitionRepository.getExhibitionsData();
            if (exhibitions == null || exhibitions.isEmpty()) {
                return new ResultService<>(false, "No se pudo cargar la lista de eventos", null);
            }
            return new ResultService<>(true, "Mostrando listado de eventos", exhibitions);

        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error de conexión con el servidor", null);

        } catch (SQLException e) {
            String message = "Error obteniendo listado de eventos";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, null);
        }
    }

    public ResultService<List<ExhibitionItem>> getProductSelectedEvent() {
        Exhibition selectedExhibition = ((Client) SessionManager.getInstance().getCurrentUser()).getCurrentExhibition();
        try {
            List<ExhibitionItem> exhibitionItems = exhibitionRepository.getExhibitionItemsFromDB(selectedExhibition.getIdExhibition());
            if (exhibitionItems == null || exhibitionItems.isEmpty()) {
                return new ResultService<>(false, "No se pudo cargar los productos del evento", null);
            }
            return new ResultService<>(true, "Mostrando productos del evento " + selectedExhibition.getName(), exhibitionItems);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error de conexión con el servidor", null);

        } catch (SQLException e) {
            String message = "Error obteniendo productos del evento";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, null);
        }
    }

    public ResultService<Boolean> addNewExhibition(Exhibition exhibition) {

        try {
            if (!exhibitionRepository.addNewExhibition(exhibition))
                return new ResultService<>(false, "No se pudo crear la exhibición", null);
            return new ResultService<>(true, "Exhibición creada correctamente", null);
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return new ResultService<>(false, "Error de conexión con el servidor", null);
        } catch (DuplicateExhibitionNameException e) {
            return new ResultService<>(false, e.getMessage(), null); // THROW EXCEPTION ON DUPLICATE DB NAME

        } catch (SQLException e) {
            String message = "Error guardando la exhibición";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return new ResultService<>(false, message, null);
        }
    }

    public boolean changeActivateUSer(ExhibitionAccessDto exhibitionAccessDto) {
        try {
            return exhibitionRepository.changeActivateExhibition(exhibitionAccessDto.isActive(), exhibitionAccessDto.getIdExhibition());
        } catch (DBConnectionException e) {
            AlertHelper.showDBConnectAlert(); // SHOW DB CONNECTION ALERT
            return false;
        } catch (SQLException e) {
            String message = "Error actualizando el estado de activo de la exhibición";
            AlertHelper.showSQLAlert(message); // SHOW SQL ALERT TO USER
            return false;
        }
    }


}