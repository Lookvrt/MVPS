package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class GuanteTraductorView extends BorderPane {

    private TextArea translatedTextArea;
    private Button playPauseBtn;
    private Button muteBtn;
    private ToggleButton toggleBluetooth;
    private Button conectarBtn;
    private Label connectionStatus;
    private Button inicioBtn;
    private Button bluetoothMenuBtn;
    private Button notificacionBtn;
    private Button opcionesBtn;

    private TextField nuevoMensajeTextField;
    private Button agregarMensajeBtn;
    private ListView<String> mensajesListView;

    private VBox mainArea;
    private VBox bluetoothPane;
    private VBox notifPane;
    private VBox optionsPane;

    public GuanteTraductorView() {
        initializeUI();
    }

    private void initializeUI() {
        translatedTextArea = new TextArea();
        translatedTextArea.setPromptText("Texto traducido aparece aquí");
        translatedTextArea.setEditable(false);
        translatedTextArea.setPrefHeight(150);

        playPauseBtn = new Button("▶");
        playPauseBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

        muteBtn = new Button("🔊");
        muteBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

        HBox controlBox = new HBox(20, playPauseBtn, muteBtn);
        controlBox.setAlignment(Pos.CENTER);

        mainArea = new VBox(20, translatedTextArea, controlBox);
        mainArea.setPadding(new Insets(20));
        mainArea.setAlignment(Pos.CENTER);

        toggleBluetooth = new ToggleButton("Bluetooth OFF");
        toggleBluetooth.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");

        connectionStatus = new Label("No conectado");
        conectarBtn = new Button("Conectar dispositivo");
        conectarBtn.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px;");
        conectarBtn.setDisable(true);

        bluetoothPane = new VBox(20, toggleBluetooth, conectarBtn, connectionStatus);
        bluetoothPane.setPadding(new Insets(20));
        bluetoothPane.setAlignment(Pos.CENTER);

        notifPane = new VBox(10);
        notifPane.setPadding(new Insets(20));
        notifPane.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Notificaciones");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label subTitle = new Label("GTLSM V1.0");
        subTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: steelblue; -fx-padding: 0 0 10px 0;");
        
        Text update1 = new Text("+ Traducción de señas en tiempo real");
        Text update2 = new Text("+ Conectividad Bluetooth avanzada con GTLS");
        Text update3 = new Text("+ Control de audio (silenciar/reactivar voz)");
        Text update4 = new Text("+ Navegación intuitiva mediante menú inferior");
        Text update5 = new Text("+ Sistema de notificaciones de la aplicación");
        Text version = new Text("(Próximamente más funcionalidades en futuras versiones)");

        notifPane.getChildren().addAll(title, subTitle, update1, update2, update3, update4, update5, version);


        optionsPane = new VBox(15);
        optionsPane.setAlignment(Pos.TOP_CENTER);
        optionsPane.setPadding(new Insets(20));

        Label opcionesTitle = new Label("Opciones de la Aplicación");
        opcionesTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label gestionMensajesLabel = new Label("Gestión de Mensajes:");
        gestionMensajesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");

        nuevoMensajeTextField = new TextField();
        nuevoMensajeTextField.setPromptText("Escribe un nuevo mensaje...");
        nuevoMensajeTextField.setPrefWidth(200);

        agregarMensajeBtn = new Button("Agregar Mensaje");
        agregarMensajeBtn.setStyle("-fx-font-size: 14px;");

        HBox agregarMensajeBox = new HBox(10, nuevoMensajeTextField, agregarMensajeBtn);
        agregarMensajeBox.setAlignment(Pos.CENTER);

        mensajesListView = new ListView<>();
        mensajesListView.setPrefHeight(150);
        mensajesListView.setPlaceholder(new Label("No hay mensajes disponibles."));

        optionsPane.getChildren().addAll(opcionesTitle, gestionMensajesLabel, agregarMensajeBox, new Label("Mensajes actuales:"), mensajesListView);


        HBox menuBar = new HBox(30);
        menuBar.setPadding(new Insets(15));
        menuBar.setAlignment(Pos.CENTER);
        menuBar.setStyle("-fx-background-color: #d0e6f6;");

        inicioBtn = new Button("🏠");
        bluetoothMenuBtn = new Button("📶");
        notificacionBtn = new Button("🔔");
        opcionesBtn = new Button("⚙");

        String menuBtnStyle = "-fx-font-size: 24px; -fx-padding: 15px 20px;";
        inicioBtn.setStyle(menuBtnStyle);
        bluetoothMenuBtn.setStyle(menuBtnStyle);
        notificacionBtn.setStyle(menuBtnStyle);
        opcionesBtn.setStyle(menuBtnStyle);

        menuBar.getChildren().addAll(inicioBtn, bluetoothMenuBtn, notificacionBtn, opcionesBtn);

        setCenter(mainArea);
        setBottom(menuBar);
    }

    public TextArea getTranslatedTextArea() {
        return translatedTextArea;
    }

    public Button getPlayPauseBtn() {
        return playPauseBtn;
    }

    public Button getMuteBtn() {
        return muteBtn;
    }

    public ToggleButton getToggleBluetooth() {
        return toggleBluetooth;
    }

    public Button getConectarBtn() {
        return conectarBtn;
    }

    public Label getConnectionStatus() {
        return connectionStatus;
    }

    public Button getInicioBtn() {
        return inicioBtn;
    }

    public Button getBluetoothMenuBtn() {
        return bluetoothMenuBtn;
    }

    public Button getNotificacionBtn() {
        return notificacionBtn;
    }

    public Button getOpcionesBtn() {
        return opcionesBtn;
    }

    public TextField getNuevoMensajeTextField() {
        return nuevoMensajeTextField;
    }

    public Button getAgregarMensajeBtn() {
        return agregarMensajeBtn;
    }

    public ListView<String> getMensajesListView() {
        return mensajesListView;
    }

    public void showMainArea() {
        setCenter(mainArea);
    }

    public void showBluetoothPane() {
        setCenter(bluetoothPane);
    }

    public void showNotifPane() {
        setCenter(notifPane);
    }

    public void showOptionsPane() {
        setCenter(optionsPane);
    }
}