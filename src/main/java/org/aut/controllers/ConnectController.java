package org.aut.controllers;

import org.aut.dataAccessors.ConnectAccessor;
import org.aut.dataAccessors.UserAccessor;
import org.aut.models.Connect;
import org.aut.models.User;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;

import java.sql.SQLException;

public class ConnectController {
    public static void addConnect(Connect connect) throws SQLException, NotFoundException, NotAcceptableException {
        if (connect == null) {
            throw new NotAcceptableException("Null connect");
        } else if (!UserController.userExistsById(connect.getApplicantId()) || !UserController.userExistsById(connect.getAcceptorId())) {
            throw new NotFoundException("User not found");
        } else if (ConnectAccessor.connectExists(connect)) {
            throw new NotAcceptableException("Connect already exists");
        } else if (connect.getAcceptorId().equals(connect.getApplicantId())) {
            throw new NotAcceptableException("User can't connect to himself");
        }
    }

    public static void updateConnect(Connect connect) throws SQLException, NotFoundException {
        if (ConnectAccessor.connectExists(connect)) {
            ConnectAccessor.updateConnect(connect);
        } else {
            throw new NotFoundException("Connect not found");
        }
    }
}
