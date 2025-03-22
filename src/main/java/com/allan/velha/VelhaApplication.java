package com.allan.velha;

import com.allan.velha.domain.service.impl.NeuralNetworkServiceImpl;
import com.allan.velha.presentation.controller.VelhaController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class VelhaApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/allan/velha/presentation/view/Velha-view.fxml"));

        // Criar serviços
        NeuralNetworkServiceImpl neuralNetworkService = new NeuralNetworkServiceImpl();

        // Criar e injetar o controller
        VelhaController controller = new VelhaController(neuralNetworkService);
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Jogo da Velha");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/jogo-da-velha.png"))));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}