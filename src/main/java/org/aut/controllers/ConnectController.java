package org.aut.controllers;

import org.aut.dataAccessors.ConnectAccessor;
import org.aut.models.Connect;
import org.aut.utils.exceptions.NotAcceptableException;
import org.aut.utils.exceptions.NotFoundException;

import java.sql.SQLException;

public class ConnectController {
    public static void addConnect(Connect connect) throws SQLException, NotFoundException, NotAcceptableException {
        if (connect == null) {
            throw new NotAcceptableException("Null connect");
        } else if (!UserController.userExistsById(connect.getApplicant_id()) || !UserController.userExistsById(connect.getAcceptor_id())) {
            throw new NotFoundException("User not found");
        } else if (ConnectAccessor.getAcceptedConnect(connect.getAcceptor_id() , connect.getApplicant_id()) != null || ConnectAccessor.getWaitingConnectOfAcceptor(connect.getApplicant_id(), connect.getAcceptor_id()) != null) {
            throw new NotAcceptableException("Connect already exists");
        } else if (connect.getAcceptor_id().equals(connect.getApplicant_id())) {
            throw new NotAcceptableException("User can't connect to himself");
        }
        Connect waitingConnect;
        if ((waitingConnect = ConnectAccessor.getWaitingConnectOfAcceptor(connect.getApplicant_id(), connect.getAcceptor_id())) != null) {
            updateConnect(new Connect(waitingConnect.getApplicant_id(), waitingConnect.getAcceptor_id(), waitingConnect.getNote(), Connect.AcceptState.ACCEPTED));
        }
        ConnectAccessor.addConnect(connect);
    }

    public static void updateConnect(Connect connect) throws SQLException, NotFoundException , NotAcceptableException {
        System.out.println("hey 1.5");
        if (ConnectAccessor.getWaitingConnectOfAcceptor(connect.getApplicant_id() , connect.getAcceptor_id()) != null) {
            System.out.println("hey2");
            ConnectAccessor.updateConnect(connect);
        } else {
            System.out.println("hey 2.5");
            throw new NotFoundException("Connect not found");
        }
    }

    public static void deleteConnect(Connect connect) throws SQLException, NotFoundException, NotAcceptableException {
        if(ConnectAccessor.getAcceptedConnect(connect.getAcceptor_id() , connect.getApplicant_id()) == null) {
            throw new NotFoundException("Connect not found");
        } else {
            ConnectAccessor.deleteConnect(connect);
        }
    }
}
