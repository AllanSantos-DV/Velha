module com.allan.velha {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.allan.velha to javafx.fxml;
    opens com.allan.velha.presentation.controller to javafx.fxml;
    opens com.allan.velha.presentation.model to javafx.base;

    exports com.allan.velha;
    exports com.allan.velha.domain.model;
    exports com.allan.velha.domain.service;
    exports com.allan.velha.domain.service.impl;
    exports com.allan.velha.presentation.controller;
    exports com.allan.velha.presentation.model;
}