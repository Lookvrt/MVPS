package controller;

import model.GuanteTraductorModel;
import view.GuanteTraductorView;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GuanteTraductorController {

    private final GuanteTraductorModel model;
    private final GuanteTraductorView view;

    public GuanteTraductorController(GuanteTraductorModel model, GuanteTraductorView view) {
        this.model = model;
        this.view = view;
        attachEvents();
        bindProperties();
    }

    private void attachEvents() {
        view.getPlayPauseBtn().setOnAction(e -> model.togglePlayPause());
        view.getMuteBtn().setOnAction(e -> model.toggleMute());

        view.getConectarBtn().setOnAction(e -> model.connectDevice());

        view.getInicioBtn().setOnAction(e -> view.showMainArea());
        view.getBluetoothMenuBtn().setOnAction(e -> view.showBluetoothPane());
        view.getNotificacionBtn().setOnAction(e -> view.showNotifPane());
        view.getOpcionesBtn().setOnAction(e -> view.showOptionsPane());

        view.getAgregarMensajeBtn().setOnAction(e -> {
            String nuevoMensaje = view.getNuevoMensajeTextField().getText();
            if (nuevoMensaje != null && !nuevoMensaje.trim().isEmpty()) {
                model.agregarNuevoMensaje(nuevoMensaje);
                view.getNuevoMensajeTextField().clear();
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText(null);
                alert.setContentText("El mensaje no puede estar vacío.");
                alert.showAndWait();
            }
        });
    }

    private void bindProperties() {
        view.getTranslatedTextArea().textProperty().bind(model.translatedTextProperty());

        model.playingProperty().addListener((obs, oldVal, newVal) -> {
            view.getPlayPauseBtn().setText(newVal ? "⏸" : "▶");
        });

        model.mutedProperty().addListener((obs, oldVal, newVal) -> {
            view.getMuteBtn().setText(newVal ? "🔇" : "🔊");
        });

        view.getToggleBluetooth().textProperty().bind(
                Bindings.when(model.bluetoothOnProperty())
                        .then("Bluetooth ON")
                        .otherwise("Bluetooth OFF")
        );
        view.getToggleBluetooth().selectedProperty().bindBidirectional(model.bluetoothOnProperty());

        view.getConnectionStatus().textProperty().bind(model.connectionStatusProperty());

        view.getConectarBtn().disableProperty().bind(model.bluetoothOnProperty().not());

        view.getMensajesListView().setItems(model.getMensajesUI());
    }
}