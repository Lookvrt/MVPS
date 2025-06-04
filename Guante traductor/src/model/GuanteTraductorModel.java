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
    private final StringProperty connectionStatus = new SimpleStringProperty("No conectado");

    private final ObservableList<String> mensajesUI = FXCollections.observableArrayList();


    public GuanteTraductorModel() {
        cargarDatosInicialesDesdeSimulacionBD();

        bluetoothOn.set(configuracionPersistente.isBluetoothActivado());
        connectionStatus.set(configuracionPersistente.getEstadoConexionBluetooth());
        mensajesUI.addAll(mensajesAlmacenados);

        bluetoothOn.addListener((obs, oldVal, newVal) -> {
            configuracionPersistente.setBluetoothActivado(newVal);
            guardarConfiguracionEnSimulacionBD(configuracionPersistente);
        });
        connectionStatus.addListener((obs, oldVal, newVal) -> {
            configuracionPersistente.setEstadoConexionBluetooth(newVal);
            guardarConfiguracionEnSimulacionBD(configuracionPersistente);
        });

        translatedText.addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty() && playing.get() && !muted.get()) {
                speakText(newVal);
            }
        });
    }

    private void cargarDatosInicialesDesdeSimulacionBD() {
        System.out.println("Simulando carga de datos desde la 'base de datos en memoria'...");
        mensajesAlmacenados = new ArrayList<>(Arrays.asList(
                "Hola, ¿cómo estás?",
                "Buenos días",
                "Buenas noches",
                "Ayuda",
                "Gracias",
                "Lo siento"
        ));

        configuracionPersistente = new ConfiguracionApp();
        System.out.println("Datos cargados. Mensajes: " + mensajesAlmacenados.size() + ", Configuración: " + configuracionPersistente);
    }

    private void guardarConfiguracionEnSimulacionBD(ConfiguracionApp config) {
        System.out.println("Simulando guardado de configuración en 'base de datos en memoria': " + config);
    }

    private void insertarMensajeEnSimulacionBD(String mensaje) {
        if (!mensajesAlmacenados.contains(mensaje)) {
            mensajesAlmacenados.add(mensaje);
            System.out.println("Simulando inserción de mensaje en 'base de datos en memoria': " + mensaje);
        }
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

    public ObservableList<String> getMensajesUI() {
        return mensajesUI;
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
            System.out.println("No se puede iniciar la traducción. Bluetooth no activado o dispositivo no conectado.");
            translatedText.set("Bluetooth no activado o dispositivo no conectado.");
            playing.set(false);
        }
    }

    public void toggleMute() {
        muted.set(!muted.get());
    }

    public void toggleBluetooth() {
        bluetoothOn.set(!bluetoothOn.get());
        if (bluetoothOn.get()) {
            connectionStatus.set("Activado");
        } else {
            connectionStatus.set("No conectado");
        }
    }

    public void connectDevice() {
        if (bluetoothOn.get()) {
            connectionStatus.set("GTLS Conectado");
        }
    }

    public void agregarNuevoMensaje(String nuevoMensaje) {
        if (nuevoMensaje != null && !nuevoMensaje.trim().isEmpty()) {
            String mensajeNormalizado = nuevoMensaje.trim();
            insertarMensajeEnSimulacionBD(mensajeNormalizado);
            if (!mensajesUI.contains(mensajeNormalizado)) {
                mensajesUI.add(mensajeNormalizado);
            }
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