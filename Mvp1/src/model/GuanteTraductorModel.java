package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GuanteTraductorModel {

    private List<String> mensajesAlmacenados;
    private ConfiguracionApp configuracionPersistente;

    private final StringProperty translatedText = new SimpleStringProperty("Texto traducido aparece aquí");
    private final BooleanProperty playing = new SimpleBooleanProperty(false);
    private final BooleanProperty muted = new SimpleBooleanProperty(false);
    private final BooleanProperty bluetoothOn = new SimpleBooleanProperty(false);
    private final StringProperty connectionStatus = new SimpleStringProperty("");

    public GuanteTraductorModel() {
        cargarDatosInicialesDesdeSimulacionBD();

        bluetoothOn.set(configuracionPersistente.isBluetoothActivado());
        if (!bluetoothOn.get()) {
            connectionStatus.set("");
        } else {
            if ("GTLS Conectado".equals(configuracionPersistente.getEstadoConexionBluetooth())) {
                 connectionStatus.set(configuracionPersistente.getEstadoConexionBluetooth());
            } else {
                 connectionStatus.set("");
            }
        }

        bluetoothOn.addListener((obs, oldVal, newVal) -> {
            configuracionPersistente.setBluetoothActivado(newVal);
            if (!newVal) {
                connectionStatus.set("");
                playing.set(false);
            } else {
                if (!"GTLS Conectado".equals(connectionStatus.get())) {
                    connectionStatus.set("");
                }
            }
            guardarConfiguracionEnSimulacionBD(configuracionPersistente);
        });

        connectionStatus.addListener((obs, oldVal, newVal) -> {
            configuracionPersistente.setEstadoConexionBluetooth(newVal);
            guardarConfiguracionEnSimulacionBD(configuracionPersistente);
        });

        translatedText.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty() && playing.get() && !muted.get() && bluetoothOn.get() && "GTLS Conectado".equals(connectionStatus.get())) {
                speakText(newVal);
            }
        });
    }

    private void cargarDatosInicialesDesdeSimulacionBD() {
        mensajesAlmacenados = new ArrayList<>(Arrays.asList(
                "Hola, ¿cómo estás?",
                "Buenos días",
                "Buenas noches",
                "Ayuda",
                "Gracias",
                "Lo siento"
        ));
        configuracionPersistente = new ConfiguracionApp();
    }

    private void guardarConfiguracionEnSimulacionBD(ConfiguracionApp config) {
    }

    public StringProperty translatedTextProperty() {
        return translatedText;
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public BooleanProperty mutedProperty() {
        return muted;
    }

    public BooleanProperty bluetoothOnProperty() {
        return bluetoothOn;
    }

    public StringProperty connectionStatusProperty() {
        return connectionStatus;
    }

    public void togglePlayPause() {
        if (bluetoothOn.get() && "GTLS Conectado".equals(connectionStatus.get())) {
            playing.set(!playing.get());

            if (playing.get()) {
                if (!mensajesAlmacenados.isEmpty()) {
                    translatedText.set(mensajesAlmacenados.get(new Random().nextInt(mensajesAlmacenados.size())));
                } else {
                    translatedText.set("No hay mensajes para traducir.");
                }
            }
        } else {
            translatedText.set("Bluetooth no activado o dispositivo no conectado.");
            playing.set(false);
        }
    }

    public void toggleMute() {
        muted.set(!muted.get());
    }

    public void toggleBluetooth() {
        bluetoothOn.set(!bluetoothOn.get());
    }

    public void connectDevice() {
        if (bluetoothOn.get()) {
            connectionStatus.set("GTLS Conectado");
        } else {
            connectionStatus.set("Bluetooth no activado");
        }
    }

    public void disconnectDevice() {
        if ("GTLS Conectado".equals(connectionStatus.get())) {
            if (bluetoothOn.get()) {
                connectionStatus.set("");
            } else {
                connectionStatus.set("");
            }
            playing.set(false);
            translatedText.set("Dispositivo desconectado.");
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