package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GuanteTraductorModel {

    // Cambiamos a ObservableList para que la vista pueda reaccionar a los cambios
    private ObservableList<String> quickMessages;
    private ConfiguracionApp configuracionPersistente;

    private final StringProperty translatedText = new SimpleStringProperty("Texto traducido aparece aquí");
    private final BooleanProperty playing = new SimpleBooleanProperty(false);
    private final BooleanProperty muted = new SimpleBooleanProperty(false);
    private final BooleanProperty bluetoothOn = new SimpleBooleanProperty(false);
    private final StringProperty gtlsDeviceStatus = new SimpleStringProperty("GTLS");

    private final IntegerProperty batteryLevel = new SimpleIntegerProperty(100);
    private final BooleanProperty isBatteryLow = new SimpleBooleanProperty(false);
    private final BooleanProperty batterySaverOn = new SimpleBooleanProperty(false);
    private int simulationStep = 0;

    public GuanteTraductorModel() {
        // Inicializa la configuración persistente
        this.configuracionPersistente = new ConfiguracionApp();
        // Carga los datos iniciales, incluyendo mensajes rápidos
        cargarDatosInicialesDesdeSimulacionBD();

        bluetoothOn.set(configuracionPersistente.isBluetoothActivado());

        String savedGtlsStatus = configuracionPersistente.getEstadoConexionBluetooth();
        if ("GTLS Conectado".equals(savedGtlsStatus) && bluetoothOn.get()) {
            gtlsDeviceStatus.set("Conectado");
        } else {
            gtlsDeviceStatus.set("GTLS");
        }

        bluetoothOn.addListener((obs, oldVal, newVal) -> {
            configuracionPersistente.setBluetoothActivado(newVal);
            if (!newVal) {
                gtlsDeviceStatus.set("GTLS");
                playing.set(false);
            }
            guardarConfiguracionEnSimulacionBD(configuracionPersistente);
        });

        gtlsDeviceStatus.addListener((obs, oldVal, newVal) -> {
            if ("Conectado".equals(newVal)) {
                configuracionPersistente.setEstadoConexionBluetooth("GTLS Conectado");
            } else {
                configuracionPersistente.setEstadoConexionBluetooth("");
            }
            guardarConfiguracionEnSimulacionBD(configuracionPersistente);
        });

        translatedText.addListener((obs, oldVal, newVal) -> {
            // Solo habla si hay texto, se está reproduciendo, no está muteado, bluetooth está ON y GTLS está conectado
            if (newVal != null && !newVal.isEmpty() && playing.get() && !muted.get() && bluetoothOn.get() && "Conectado".equals(gtlsDeviceStatus.get())) {
                speakText(newVal);
            }
        });

        batteryLevel.addListener((obs, oldVal, newVal) -> {
            isBatteryLow.set(newVal.intValue() <= 30);
        });
    }

    private void cargarDatosInicialesDesdeSimulacionBD() {
        // Inicializa la lista de mensajes rápidos con algunos valores por defecto
        quickMessages = FXCollections.observableArrayList(
                "Hola, ¿cómo estás?",
                "Buenos días",
                "Buenas noches",
                "Ayuda",
                "Gracias",
                "Lo siento",
                "¿Me entiendes?",
                "Necesito agua"
        );
        // La configuración persistente se inicializa en el constructor de GuanteTraductorModel
        // y se actualiza al cargar datos (aunque aquí solo se inicia con valores por defecto)
    }

    private void guardarConfiguracionEnSimulacionBD(ConfiguracionApp config) {
        System.out.println("Configuración guardada en simulación: Bluetooth=" + config.isBluetoothActivado() + ", GTLS=" + config.getEstadoConexionBluetooth());
        // En una aplicación real, aquí guardarías en un archivo, base de datos, etc.
        // Podrías añadir lógica para guardar 'quickMessages' aquí también si necesitas persistencia para ellos.
    }

    // --- Getters de propiedades ---
    public StringProperty translatedTextProperty() { return translatedText; }
    public BooleanProperty playingProperty() { return playing; }
    public BooleanProperty mutedProperty() { return muted; }
    public BooleanProperty bluetoothOnProperty() { return bluetoothOn; }
    public StringProperty gtlsDeviceStatusProperty() { return gtlsDeviceStatus; }
    public IntegerProperty batteryLevelProperty() { return batteryLevel; }
    public BooleanProperty isBatteryLowProperty() { return isBatteryLow; }
    public BooleanProperty batterySaverOnProperty() { return batterySaverOn; }

    // --- Métodos de Mensajes Rápidos ---
    public ObservableList<String> getQuickMessages() {
        return quickMessages;
    }

    public void addQuickMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            quickMessages.add(message.trim());
        }
    }

    public void editQuickMessage(int index, String newMessage) {
        if (index >= 0 && index < quickMessages.size() && newMessage != null && !newMessage.trim().isEmpty()) {
            quickMessages.set(index, newMessage.trim());
        }
    }

    public void deleteQuickMessage(int index) {
        if (index >= 0 && index < quickMessages.size()) {
            quickMessages.remove(index);
        }
    }

    public void sendQuickMessageToTranslation(String message) {
        if (message != null && !message.trim().isEmpty()) {
            translatedText.set(message);
        }
    }

    // --- Métodos de Lógica de la Aplicación ---
    public void triggerLowBatterySimulation() {
        if (batteryLevel.get() <= 0) {
            batteryLevel.set(100);
            simulationStep = 0;
            return;
        }

        switch (simulationStep) {
            case 0: batteryLevel.set(80); simulationStep = 1; break;
            case 1: batteryLevel.set(50); simulationStep = 2; break;
            case 2: batteryLevel.set(30); simulationStep = 3; break;
            case 3: batteryLevel.set(15); simulationStep = 4; break;
            case 4: batteryLevel.set(5); simulationStep = 5; break;
            case 5: batteryLevel.set(0); simulationStep = 6; break;
            default: batteryLevel.set(100); simulationStep = 0; break;
        }
    }

    public void togglePlayPause() {
        if (bluetoothOn.get() && "Conectado".equals(gtlsDeviceStatus.get())) {
            playing.set(!playing.get());

            if (playing.get()) {
                // Ya no usamos mensajesAlmacenados directamente aquí para la traducción "aleatoria"
                // El texto se establecerá a través de la selección de un mensaje rápido
                // o desde la entrada del guante real.
                // Para simular, podrías tomar uno aleatorio de quickMessages si lo deseas,
                // o dejar que el usuario envíe uno desde la lista de mensajes rápidos.
                if (quickMessages != null && !quickMessages.isEmpty()) {
                    translatedText.set(quickMessages.get(new Random().nextInt(quickMessages.size())));
                } else {
                    translatedText.set("No hay mensajes para traducir.");
                }
            } else {
                translatedText.set("Texto traducido aparece aquí"); // Limpiar al pausar
            }
        } else {
            playing.set(false);
            translatedText.set("Bluetooth no activado o dispositivo no conectado.");
        }
    }

    public void toggleMute() {
        muted.set(!muted.get());
    }

    public void toggleBluetooth() {
        bluetoothOn.set(!bluetoothOn.get());
    }

    public void toggleGtlsDeviceConnection() {
        if (!bluetoothOn.get()) {
            javafx.application.Platform.runLater(() -> {
                translatedText.set("Active Bluetooth para conectar GTLS.");
            });
            return;
        }

        if ("GTLS".equals(gtlsDeviceStatus.get())) {
            gtlsDeviceStatus.set("Conectando...");
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Simula tiempo de conexión
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Hilo de conexión GTLS interrumpido.");
                }
                javafx.application.Platform.runLater(() -> {
                    gtlsDeviceStatus.set("Conectado");
                    translatedText.set("GTLS Conectado.");
                });
            }).start();
        } else if ("Conectado".equals(gtlsDeviceStatus.get())) {
            gtlsDeviceStatus.set("GTLS");
            playing.set(false);
            translatedText.set("GTLS Desconectado.");
        }
    }

    public void speakText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder pb = null;

        try {
            if (os.contains("win")) {
                String escapedText = text.replace("'", "''");
                pb = new ProcessBuilder("powershell.exe",
                        "-command",
                        "Add-Type -AssemblyName System.Speech; " +
                        "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('" + escapedText + "');");
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("say", text);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                pb = new ProcessBuilder("espeak", text);
            } else {
                System.err.println("Sistema operativo no soportado para síntesis de voz: " + os);
                return;
            }

            if (pb != null) {
                Process process = pb.start();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("Error del proceso de voz: " + line);
                }
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    System.err.println("Error al ejecutar el comando de voz. Código de salida: " + exitCode);
                }
            }
        } catch (Exception e) {
            System.err.println("Excepción al intentar hablar el texto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}