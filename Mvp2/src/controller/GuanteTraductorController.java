package controller;

import model.GuanteTraductorModel;
import view.GuanteTraductorView;
import javafx.animation.TranslateTransition;
import javafx.animation.PauseTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class GuanteTraductorController {

    private final GuanteTraductorModel model;
    private final GuanteTraductorView view;
    private EventHandler<MouseEvent> dismissNotificationHandler;
    private PauseTransition notificationHideTimer;

    public GuanteTraductorController(GuanteTraductorModel model, GuanteTraductorView view) {
        this.model = model;
        this.view = view;

        notificationHideTimer = new PauseTransition(Duration.seconds(4));
        notificationHideTimer.setOnFinished(event -> hideBatteryNotification());

        dismissNotificationHandler = event -> {
            // Solo ocultar si el nivel de batería es mayor a 0 (para no ocultar si está en 0% y la app va a cerrar)
            if (view.getLowBatteryNotificationPane().isVisible() && model.batteryLevelProperty().get() > 0) {
                hideBatteryNotification();
                notificationHideTimer.stop();
            }
        };

        attachEvents();
        bindProperties();
        updateBatteryButtonText(model.batteryLevelProperty().get()); // Actualizar texto inicial del botón de batería
        updateBatteryDetailDisplay(model.batteryLevelProperty().get()); // Actualizar vista de detalles de batería al inicio
    }

    private void attachEvents() {
        view.getPlayPauseBtn().setOnAction(e -> model.togglePlayPause());
        view.getMuteBtn().setOnAction(e -> model.toggleMute());

        view.getBluetoothToggleBtn().setOnAction(e -> {
            model.toggleBluetooth();
        });

        view.getBluetoothToggleContainer().setOnMouseClicked(e -> {
            model.toggleBluetooth();
        });

        view.getGtlsBtn().setOnAction(e -> {
            model.toggleGtlsDeviceConnection();
        });

        // Navegación del menú inferior
        view.getInicioBtn().setOnAction(e -> view.showMainArea());
        view.getBluetoothMenuBtn().setOnAction(e -> view.showBluetoothPane());
        view.getNotificacionBtn().setOnAction(e -> view.showNotifPane());
        view.getOpcionesBtn().setOnAction(e -> view.showOptionsPane());

        // Botón de simulación de batería
        view.getNotificationSimButton().setOnAction(e -> {
            model.triggerLowBatterySimulation();
        });

        // Clic en la notificación de batería para cerrarla
        view.getLowBatteryNotificationPane().setOnMouseClicked(e -> {
            if (model.batteryLevelProperty().get() > 0) { // No permitir cerrar si la batería está en 0
                hideBatteryNotification();
                notificationHideTimer.stop();
            }
        });

        // Navegación a la pantalla de detalles de batería
        view.getBatteryOptionBox().setOnMouseClicked(e -> {
            view.showBatteryDetailPane();
            updateBatteryDetailDisplay(model.batteryLevelProperty().get()); // Asegurarse de actualizar al mostrar
        });

        // Botón de regreso en la pantalla de detalles de batería
        view.getBatteryDetailBackBtn().setOnAction(e -> {
            view.showOptionsPane();
        });

        // Toggle de ahorro de batería
        view.getBatterySaverToggleBtn().setOnAction(e -> toggleBatterySaver());

        // --- Eventos para Mensajes Rápidos (MSV Fast) ---
        view.getMsvFastOptionBox().setOnMouseClicked(e -> view.showMsvFastPane()); // Mostrar pantalla de MSV Fast
        view.getMsvFastBackBtn().setOnAction(e -> view.showOptionsPane()); // Volver a Opciones desde MSV Fast

        // Botones de gestión de mensajes rápidos
        view.getAddMessageBtn().setOnAction(e -> handleAddMessage());
        view.getEditMessageBtn().setOnAction(e -> handleEditMessage());
        view.getDeleteMessageBtn().setOnAction(e -> handleDeleteMessage());
        view.getSendMessageBtn().setOnAction(e -> handleSendMessage());
    }

    private void bindProperties() {
        view.getTranslatedTextArea().textProperty().bind(model.translatedTextProperty());

        model.playingProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                view.getPlayPauseBtn().setText(newVal ? "⏸" : "▶");
            });
        });

        model.mutedProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                view.getMuteBtn().setText(newVal ? "🔇" : "🔊");
            });
        });

        model.bluetoothOnProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                updateBluetoothToggleStyle(newVal);
                view.getPairedDevicesLabel().setVisible(newVal);
                view.getPairedDevicesLabel().setManaged(newVal);
                view.getGtlsBtn().setVisible(newVal);
                view.getGtlsBtn().setManaged(newVal);
                view.getGtlsConnectionStatusLabel().setVisible(newVal);
                view.getGtlsConnectionStatusLabel().setManaged(newVal);

                if (newVal) {
                    view.getBluetoothInfoLabel().setText("Bluetooth activado. Comunicándose con otros dispositivos Bluetooth cercanos.");
                } else {
                    view.getBluetoothInfoLabel().setText("Cuando Bluetooth está activado, su dispositivo puede comunicarse con otros dispositivos Bluetooth cercanos.");
                    model.gtlsDeviceStatusProperty().set("GTLS"); // Reset GTLS status when Bluetooth is off
                }
            });
        });
        updateBluetoothToggleStyle(model.bluetoothOnProperty().get()); // Set initial style
        boolean initialBluetoothOn = model.bluetoothOnProperty().get();
        view.getPairedDevicesLabel().setVisible(initialBluetoothOn);
        view.getPairedDevicesLabel().setManaged(initialBluetoothOn);
        view.getGtlsBtn().setVisible(initialBluetoothOn);
        view.getGtlsBtn().setManaged(initialBluetoothOn);
        view.getGtlsConnectionStatusLabel().setVisible(initialBluetoothOn);
        view.getGtlsConnectionStatusLabel().setManaged(initialBluetoothOn);


        BooleanProperty bluetoothIsOn = model.bluetoothOnProperty();
        BooleanBinding isGtlsConnecting = model.gtlsDeviceStatusProperty().isEqualTo("Conectando...");

        model.gtlsDeviceStatusProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if ("Conectado".equals(newVal)) {
                    view.getGtlsBtn().setText("GTLS");
                    view.getGtlsBtn().setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: transparent; -fx-padding: 5 0 5 0;");
                    view.getGtlsConnectionStatusLabel().setText("Conectado");
                } else if ("Conectando...".equals(newVal)) {
                    view.getGtlsBtn().setText("GTLS");
                    view.getGtlsBtn().setStyle("-fx-font-size: 16px; -fx-text-fill: gray; -fx-background-color: transparent; -fx-padding: 5 0 5 0;");
                    view.getGtlsConnectionStatusLabel().setText("Conectando...");
                } else { // "GTLS" (desconectado)
                    view.getGtlsBtn().setText("GTLS");
                    view.getGtlsBtn().setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: transparent; -fx-padding: 5 0 5 0;");
                    if (bluetoothIsOn.get()) { // Muestra "No conectado" solo si Bluetooth está encendido
                        view.getGtlsConnectionStatusLabel().setText("No conectado");
                    } else {
                        view.getGtlsConnectionStatusLabel().setText(""); // Si Bluetooth está apagado, no hay estado de conexión GTLS
                    }
                }
            });
        });

        // Deshabilitar botón GTLS si Bluetooth está apagado o si ya está conectando
        view.getGtlsBtn().disableProperty().bind(bluetoothIsOn.not().or(isGtlsConnecting));

        model.batteryLevelProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                updateBatteryButtonText(newVal.intValue());
                updateBatteryDetailDisplay(newVal.intValue()); // Actualizar la vista detallada de batería

                int currentBattery = newVal.intValue();

                if (currentBattery <= 5 && currentBattery > 0) {
                    view.getLowBatteryMessageLabel().setText("¡Batería críticamente baja del celular: " + currentBattery + "%! Recargue de inmediato.");
                    showBatteryNotification();
                } else if (currentBattery <= 30 && currentBattery > 5) {
                    view.getLowBatteryMessageLabel().setText("¡Batería baja del celular: " + currentBattery + "%! Considere cargar.");
                    showBatteryNotification();
                } else {
                    hideBatteryNotification();
                }

                if (currentBattery == 0) {
                    notificationHideTimer.stop();
                    hideBatteryNotification();
                    // Al llegar a 0%, la aplicación se cierra de forma controlada
                    Platform.exit();
                    System.exit(0);
                }
            });
        });

        model.batterySaverOnProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                updateBatterySaverToggleStyle(newVal);
            });
        });
        updateBatterySaverToggleStyle(model.batterySaverOnProperty().get());

        // --- Enlazar ListView de Mensajes Rápidos ---
        view.getQuickMessagesListView().setItems(model.getQuickMessages());
    }

    private void updateBluetoothToggleStyle(boolean bluetoothOn) {
        Button toggleButton = view.getBluetoothToggleBtn();
        StackPane toggleContainer = view.getBluetoothToggleContainer();
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), toggleButton);

        final double RAIL_WIDTH = 50;
        final double BUTTON_DIAMETER = 28;

        if (bluetoothOn) {
            toggleContainer.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 9;");
            transition.setToX(RAIL_WIDTH - BUTTON_DIAMETER);
            transition.play();
        } else {
            toggleContainer.setStyle("-fx-background-color: red; -fx-background-radius: 9;");
            transition.setToX(0);
            transition.play();
        }
    }

    private void updateBatteryButtonText(int batteryLevel) {
        Platform.runLater(() -> {
            view.getNotificationSimButton().setText(batteryLevel + "%");

            String textColor;
            String borderColor;

            if (batteryLevel >= 60) {
                textColor = "green";
                borderColor = "green";
            } else if (batteryLevel >= 31) {
                textColor = "orange";
                borderColor = "orange";
            } else if (batteryLevel >= 1 && batteryLevel <= 30) {
                textColor = "red";
                borderColor = "red";
            } else { // batteryLevel == 0
                textColor = "darkred";
                borderColor = "darkred";
                view.getNotificationSimButton().setDisable(true); // Deshabilitar el botón si la batería es 0
            }

            view.getNotificationSimButton().setStyle(
                    "-fx-font-size: 12px; " +
                    "-fx-padding: 3px 6px; " +
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: " + textColor + "; " +
                    "-fx-border-color: " + borderColor + "; " +
                    "-fx-border-radius: 3;"
            );
        });
    }

    private void showBatteryNotification() {
        if (!view.getLowBatteryNotificationPane().isVisible()) {
            view.getLowBatteryNotificationPane().setVisible(true);
            view.getLowBatteryNotificationPane().setManaged(true);
            view.getCenterContentStack().addEventFilter(MouseEvent.MOUSE_CLICKED, dismissNotificationHandler);
            notificationHideTimer.playFromStart();
        } else {
            notificationHideTimer.playFromStart(); // Reiniciar el temporizador si ya está visible
        }
    }

    private void hideBatteryNotification() {
        view.getLowBatteryNotificationPane().setVisible(false);
        view.getLowBatteryNotificationPane().setManaged(false);
        view.getCenterContentStack().removeEventFilter(MouseEvent.MOUSE_CLICKED, dismissNotificationHandler);
        notificationHideTimer.stop();
    }

    private void updateBatteryDetailDisplay(int level) {
        // Solo actualizar si el panel de detalles de batería está visible
        if (view.getBatteryDetailPane().isVisible()) {
            view.getDetailBatteryPercentageLabel().setText(level + " %");

            double maxProgressBarWidth = 360; // El ancho máximo que quieres para la barra
            double progressBarWidth = (level / 100.0) * maxProgressBarWidth;
            view.getBatteryProgressBar().setWidth(progressBarWidth);

            if (level > 75) {
                view.getBatteryProgressBar().setFill(Color.LIMEGREEN);
            } else if (level > 20) {
                view.getBatteryProgressBar().setFill(Color.ORANGE);
            } else {
                view.getBatteryProgressBar().setFill(Color.RED);
            }

            // Cálculo de tiempo restante simulado (basado en 20 horas de duración total)
            double totalMinutesAt100 = 20 * 60;
            double remainingMinutes = (level / 100.0) * totalMinutesAt100;
            int hours = (int) (remainingMinutes / 60);
            int minutes = (int) (remainingMinutes % 60);

            view.getTimeRemainingLabel().setText("Tiempo restante: aproximadamente " + hours + " h\ny " + minutes + " min");

            // Simulación de "Desde la última carga completa" y "Tiempo de uso de la pantalla"
            // Estos valores deberían ser gestionados de manera más robusta en un modelo real,
            // pero para la simulación, podemos hacerlos inversamente proporcionales al nivel de batería
            // o simplemente estáticos si el nivel de batería es el único factor.
            // Para mantener la simulación simple, los haré reflejar el consumo.
            int chargedHours = 24 - hours; // Si dura 20h, y tiene 10h restantes, se ha usado por 10h
            int chargedMinutes = 60 - minutes; // Simula minutos restantes de la última hora
            if (chargedMinutes < 0) {
                chargedMinutes += 60;
                chargedHours -= 1;
            }
            if (chargedHours < 0) chargedHours = 0; // Asegura que no sea negativo

            view.getLastChargeTimeLabel().setText( String.format("%d h y %d min", chargedHours, chargedMinutes));
            view.getScreenUsageTimeLabel().setText( String.format("%d h y %d min", (chargedHours / 4), (chargedMinutes / 3))); // Simulación de uso de pantalla
        }
    }

    private void toggleBatterySaver() {
        model.batterySaverOnProperty().set(!model.batterySaverOnProperty().get());
    }

    private void updateBatterySaverToggleStyle(boolean batterySaverOn) {
        Button toggleButton = view.getBatterySaverToggleBtn();
        StackPane toggleContainer = view.getBatterySaverToggleContainer();
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), toggleButton);

        final double BS_RAIL_WIDTH = 40;
        final double BS_BUTTON_DIAMETER = 24;

        if (batterySaverOn) {
            toggleContainer.setStyle("-fx-background-color: green; -fx-background-radius: 9;");
            transition.setToX(BS_RAIL_WIDTH - BS_BUTTON_DIAMETER);
            transition.play();
        } else {
            toggleContainer.setStyle("-fx-background-color: gray; -fx-background-radius: 9;");
            transition.setToX(0);
            transition.play();
        }
    }

    // --- Métodos de gestión de Mensajes Rápidos (MSV Fast) ---
    private void handleAddMessage() {
        Optional<String> result = view.showTextInputDialog(
            "Añadir Mensaje Rápido",
            "Introduce el nuevo mensaje:",
            "Mensaje:",
            ""
        );
        result.ifPresent(message -> model.addQuickMessage(message));
    }

    private void handleEditMessage() {
        int selectedIndex = view.getQuickMessagesListView().getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String currentMessage = view.getQuickMessagesListView().getSelectionModel().getSelectedItem();
            Optional<String> result = view.showTextInputDialog(
                "Editar Mensaje Rápido",
                "Edita el mensaje seleccionado:",
                "Mensaje:",
                currentMessage
            );
            result.ifPresent(newMessage -> model.editQuickMessage(selectedIndex, newMessage));
        } else {
            showAlert("Error", "No hay mensaje seleccionado", "Por favor, selecciona un mensaje para editar.");
        }
    }

    private void handleDeleteMessage() {
        int selectedIndex = view.getQuickMessagesListView().getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("Eliminar Mensaje Rápido");
            alert.setContentText("¿Estás seguro de que quieres eliminar el mensaje seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                model.deleteQuickMessage(selectedIndex);
            }
        } else {
            showAlert("Error", "No hay mensaje seleccionado", "Por favor, selecciona un mensaje para eliminar.");
        }
    }

    private void handleSendMessage() {
        String selectedMessage = view.getQuickMessagesListView().getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            model.sendQuickMessageToTranslation(selectedMessage);
            view.showMainArea(); // Volver a la pantalla principal después de enviar
        } else {
            showAlert("Error", "No hay mensaje seleccionado", "Por favor, selecciona un mensaje para enviar.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}