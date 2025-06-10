package controller;

import model.GuanteTraductorModel;
import view.GuanteTraductorView;
import javafx.beans.binding.Bindings;

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

        view.getConectarBtn().setOnAction(e -> {
            if ("Conectar dispositivo".equals(view.getConectarBtn().getText())) {
                model.connectDevice();
            } else {
                model.disconnectDevice();
            }
        });

        view.getInicioBtn().setOnAction(e -> view.showMainArea());
        view.getBluetoothMenuBtn().setOnAction(e -> view.showBluetoothPane());
        view.getNotificacionBtn().setOnAction(e -> view.showNotifPane());
        view.getOpcionesBtn().setOnAction(e -> view.showOptionsPane());
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

        view.getToggleBluetooth().setOnAction(e -> {
            model.toggleBluetooth();
        });

        model.bluetoothOnProperty().addListener((obs, oldVal, newVal) -> {
            view.getToggleBluetooth().setSelected(newVal);
        });

        view.getConnectionStatus().textProperty().bind(model.connectionStatusProperty());

        view.getConectarBtn().disableProperty().bind(model.bluetoothOnProperty().not());

        model.connectionStatusProperty().addListener((obs, oldVal, newVal) -> {
            if ("GTLS Conectado".equals(newVal)) {
                view.setConectarBtnText("Desconectar dispositivo");
            } else {
                view.setConectarBtnText("Conectar dispositivo");
            }
        });
    }
}