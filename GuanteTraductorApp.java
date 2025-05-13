import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

public class GuanteTraductorApp extends Application {

    private boolean isPlaying = false;
    private boolean isMuted = false;
    private boolean bluetoothOn = false;
    private TextArea translatedTextArea;
    private final String[] mensajes = {
            "Hola, ¿cómo estás?",
            "Buenos días",
            "Buenas noches",
            "Ayuda",
            "Gracias",
            "Lo siento"
    };

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 600);

        // Área principal de traducción
        VBox mainArea = new VBox(20);
        mainArea.setPadding(new Insets(20));
        mainArea.setAlignment(Pos.CENTER);

        translatedTextArea = new TextArea();
        translatedTextArea.setPromptText("Texto traducido aparece aquí");
        translatedTextArea.setEditable(false);
        translatedTextArea.setPrefHeight(150);

        Button playPauseBtn = new Button("▶");
        playPauseBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");
        playPauseBtn.setOnAction(e -> {
            isPlaying = !isPlaying;
            if (isPlaying) {
                translatedTextArea.setText(mensajes[new Random().nextInt(mensajes.length)]);
                playPauseBtn.setText("⏸");
            } else {
                playPauseBtn.setText("▶");
            }
        });

        Button muteBtn = new Button("🔊");
        muteBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");
        muteBtn.setOnAction(e -> {
            isMuted = !isMuted;
            muteBtn.setText(isMuted ? "🔇" : "🔊");
        });

        Button editBtn = new Button("Editar texto");
        editBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        editBtn.setOnAction(e -> translatedTextArea.setEditable(true));

        HBox controlBox = new HBox(20, playPauseBtn, muteBtn, editBtn);
        controlBox.setAlignment(Pos.CENTER);

        mainArea.getChildren().addAll(translatedTextArea, controlBox);
        root.setCenter(mainArea);

        // Menú inferior
        HBox menuBar = new HBox(30);
        menuBar.setPadding(new Insets(15));
        menuBar.setAlignment(Pos.CENTER);
        menuBar.setStyle("-fx-background-color: #d0e6f6;");

        Button inicioBtn = new Button("🏠");
        Button bluetoothBtn = new Button("📶");
        Button notificacionBtn = new Button("🔔");
        Button opcionesBtn = new Button("⚙");

        String menuBtnStyle = "-fx-font-size: 24px; -fx-padding: 15px 20px;";
        inicioBtn.setStyle(menuBtnStyle);
        bluetoothBtn.setStyle(menuBtnStyle);
        notificacionBtn.setStyle(menuBtnStyle);
        opcionesBtn.setStyle(menuBtnStyle);

        menuBar.getChildren().addAll(inicioBtn, bluetoothBtn, notificacionBtn, opcionesBtn);
        root.setBottom(menuBar);

        // Acciones del menú
        inicioBtn.setOnAction(e -> root.setCenter(mainArea));

        bluetoothBtn.setOnAction(e -> {
            VBox bluetoothPane = new VBox(20);
            bluetoothPane.setPadding(new Insets(20));
            bluetoothPane.setAlignment(Pos.CENTER);

            ToggleButton toggleBluetooth = new ToggleButton("Bluetooth OFF");
            toggleBluetooth.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
            Label connectionStatus = new Label("No conectado");
            Button conectarBtn = new Button("Conectar dispositivo");
            conectarBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

            toggleBluetooth.setOnAction(ev -> {
                bluetoothOn = toggleBluetooth.isSelected();
                toggleBluetooth.setText(bluetoothOn ? "Bluetooth ON" : "Bluetooth OFF");
                connectionStatus.setText(bluetoothOn ? "Activado" : "No conectado");
                conectarBtn.setDisable(!bluetoothOn);
            });

            conectarBtn.setDisable(true);
            conectarBtn.setOnAction(ev -> connectionStatus.setText("GTLS Conectado"));

            bluetoothPane.getChildren().addAll(toggleBluetooth, conectarBtn, connectionStatus);
            root.setCenter(bluetoothPane);
        });

        notificacionBtn.setOnAction(e -> {
            VBox notifPane = new VBox(10);
            notifPane.setPadding(new Insets(20));
            notifPane.setAlignment(Pos.TOP_LEFT);

            Label title = new Label("Notificaciones - GTLSM V1.0");
            title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            Text update1 = new Text("- Traducción en tiempo real desde guante");
            Text update2 = new Text("- Conexión Bluetooth a dispositivo GTLS");
            Text update3 = new Text("- Edición de texto traducido");
            Text update4 = new Text("- Modo altavoz y silencio");
            Text update5 = new Text("- Navegación entre pantallas desde menú inferior");
            Text update6 = new Text("- Nuevo sistema de notificaciones disponible");
            Text version = new Text("Más actualizaciones próximamente en versiones futuras...");

            notifPane.getChildren().addAll(title, update1, update2, update3, update4, update5, update6, version);
            root.setCenter(notifPane);
        });

        opcionesBtn.setOnAction(e -> {
            VBox optionsPane = new VBox();
            optionsPane.setAlignment(Pos.CENTER);
            optionsPane.setPadding(new Insets(20));
            optionsPane.getChildren().add(new Label("Próximamente..."));
            root.setCenter(optionsPane);
        });

        primaryStage.setTitle("Traductor de Guante de Señas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

