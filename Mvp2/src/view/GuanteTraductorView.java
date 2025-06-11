package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Priority;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ScrollPane;
import java.util.Optional;

public class GuanteTraductorView extends BorderPane {

    private TextArea translatedTextArea;
    private Button playPauseBtn;
    private Button muteBtn;

    private Label bluetoothTitleLabel;
    private Button bluetoothToggleBtn;
    private StackPane bluetoothToggleContainer;
    private Label bluetoothInfoLabel;

    private Label pairedDevicesLabel;
    private Button gtlsBtn;
    private Label gtlsConnectionStatusLabel;

    private Button inicioBtn;
    private Button bluetoothMenuBtn;
    private Button notificacionBtn;
    private Button opcionesBtn;

    private VBox mainArea;
    private VBox bluetoothPane;
    private ScrollPane notificationScrollPane;
    private VBox optionsPane;
    private VBox msvFastPane;

    private Button notificationSimButton;
    private StackPane lowBatteryNotificationPane;
    private Label lowBatteryMessageLabel;

    private StackPane centerContentStack;

    private VBox batteryOptionBox;
    private VBox msvFastOptionBox;

    // Propiedades para la PANTALLA DETALLADA DE BATERÍA
    private VBox batteryDetailPane;
    private Button batteryDetailBackBtn;
    private Label detailBatteryPercentageLabel;
    private Rectangle batteryProgressBar;
    private Label timeRemainingLabel;
    private Button batterySaverToggleBtn;
    private StackPane batterySaverToggleContainer;
    private Label lastChargeTimeLabel;
    private Label screenUsageTimeLabel;

    // Componentes para Mensajes Rápidos (MSV Fast)
    private ListView<String> quickMessagesListView;
    private Button addMessageBtn;
    private Button editMessageBtn;
    private Button deleteMessageBtn;
    private Button sendMessageBtn;
    private Button msvFastBackBtn;


    public GuanteTraductorView() {
        initializeUI();
    }

    private void initializeUI() {
        // Área principal de traducción
        translatedTextArea = new TextArea();
        translatedTextArea.setPromptText("Texto traducido aparece aquí");
        translatedTextArea.setEditable(false);
        translatedTextArea.setPrefHeight(150);
        translatedTextArea.setMaxWidth(360);

        playPauseBtn = new Button("▶");
        playPauseBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");
        muteBtn = new Button("🔊");
        muteBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

        HBox controlBox = new HBox(20, playPauseBtn, muteBtn);
        controlBox.setAlignment(Pos.CENTER);

        mainArea = new VBox(20, translatedTextArea, controlBox);
        mainArea.setPadding(new Insets(20));
        mainArea.setAlignment(Pos.CENTER);

        // Panel de Bluetooth
        bluetoothPane = new VBox(10);
        bluetoothPane.setPadding(new Insets(20));
        bluetoothPane.setAlignment(Pos.TOP_LEFT);

        HBox bluetoothControlBox = new HBox();
        bluetoothControlBox.setAlignment(Pos.CENTER_LEFT);
        bluetoothControlBox.setSpacing(10);

        bluetoothTitleLabel = new Label("Bluetooth");
        bluetoothTitleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 0;");

        final double RAIL_WIDTH = 50;
        final double RAIL_HEIGHT = 20;
        final double BUTTON_DIAMETER = 28;

        bluetoothToggleContainer = new StackPane();
        bluetoothToggleContainer.setPrefSize(RAIL_WIDTH, RAIL_HEIGHT);
        bluetoothToggleContainer.setStyle("-fx-background-color: red; -fx-background-radius: " + (RAIL_HEIGHT / 2) + ";");

        bluetoothToggleBtn = new Button("");
        bluetoothToggleBtn.setShape(new Circle(BUTTON_DIAMETER / 2));
        bluetoothToggleBtn.setPrefSize(BUTTON_DIAMETER, BUTTON_DIAMETER);
        bluetoothToggleBtn.setStyle("-fx-background-color: white; -fx-background-radius: " + (BUTTON_DIAMETER / 2) + ";");
        bluetoothToggleBtn.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));

        StackPane.setAlignment(bluetoothToggleBtn, Pos.CENTER_LEFT);

        double verticalMarginForButton = (RAIL_HEIGHT - BUTTON_DIAMETER) / 2;
        StackPane.setMargin(bluetoothToggleBtn, new Insets(verticalMarginForButton, 0, verticalMarginForButton, 0));

        bluetoothToggleContainer.getChildren().add(bluetoothToggleBtn);

        bluetoothControlBox.setPadding(new Insets(10, 0, 0, 0));

        bluetoothControlBox.getChildren().addAll(bluetoothTitleLabel, bluetoothToggleContainer);

        bluetoothInfoLabel = new Label("Cuando Bluetooth está activado, su dispositivo puede comunicarse con otros dispositivos Bluetooth cercanos.");
        bluetoothInfoLabel.setWrapText(true);
        bluetoothInfoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray; -fx-padding: 5px 0 15px 0;");

        pairedDevicesLabel = new Label("Dispositivos emparejados");
        pairedDevicesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");
        pairedDevicesLabel.setVisible(false);
        pairedDevicesLabel.setManaged(false);

        gtlsBtn = new Button("GTLS");
        gtlsBtn.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: transparent; -fx-padding: 5 0 5 0;");
        gtlsBtn.setVisible(false);
        gtlsBtn.setManaged(false);

        gtlsConnectionStatusLabel = new Label("No conectado");
        gtlsConnectionStatusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray; -fx-padding: 0 0 15px 0;");
        gtlsConnectionStatusLabel.setVisible(false);
        gtlsConnectionStatusLabel.setManaged(false);

        bluetoothPane.getChildren().addAll(
                bluetoothControlBox,
                bluetoothInfoLabel,
                pairedDevicesLabel,
                gtlsBtn,
                gtlsConnectionStatusLabel
        );

        // Panel de Notificaciones (Ahora dentro de un ScrollPane)
        VBox notifContentVBox = new VBox(10); // Este VBox contendrá todo el texto de notificaciones
        notifContentVBox.setPadding(new Insets(20));
        notifContentVBox.setAlignment(Pos.TOP_LEFT);

        Label notifTitle = new Label("Notificaciones");
        notifTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label versionTitle = new Label("Versiones del Sistema");
        versionTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: steelblue; -fx-padding: 0 0 10px 0;");

        String updateTextStyle = "-fx-font-size: 13px;";

        // Sección GTLSM V1.0
        Label v1Header = new Label("GTLSM V1.0");
        v1Header.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 0 0 0;");

        Text update1_v1 = new Text("+ Traducción de señas en tiempo real");
        Text update2_v1 = new Text("+ Conectividad Bluetooth avanzada con GTLS");
        Text update3_v1 = new Text("+ Control de audio (silenciar/reactivar voz)");
        Text update4_v1 = new Text("+ Navegación intuitiva mediante menú inferior");
        Text update5_v1 = new Text("+ Sistema de notificaciones de la aplicación");

        update1_v1.setStyle(updateTextStyle);
        update2_v1.setStyle(updateTextStyle);
        update3_v1.setStyle(updateTextStyle);
        update4_v1.setStyle(updateTextStyle);
        update5_v1.setStyle(updateTextStyle);

        // Sección GTLSM V2.0
        Label v2Header = new Label("GTLSM V2.0 (Última Versión)");
        v2Header.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 15px 0 0 0;");

        // TEXTO CONCISO Y SIN ASTERISCOS
        Text update1_v2 = new Text("+ Mensajes Rápidos (MSV Fast): Crea y envía frases comunes\nal instante.");
        Text update2_v2 = new Text("+ Batería: Detalles (%, tiempo) y opción de Ahorro de energía.");
        Text update3_v2 = new Text("+ Mejoras generales de rendimiento y estabilidad.");

        update1_v2.setStyle(updateTextStyle);
        update2_v2.setStyle(updateTextStyle);
        update3_v2.setStyle(updateTextStyle);

        Text futureVersionNote = new Text("(Próximamente más funcionalidades en futuras versiones)");
        futureVersionNote.setStyle(updateTextStyle);
        VBox.setMargin(futureVersionNote, new Insets(10, 0, 0, 0));

        notifContentVBox.getChildren().addAll(
            notifTitle,
            versionTitle,
            v1Header,
            update1_v1, update2_v1, update3_v1, update4_v1, update5_v1,
            v2Header,
            update1_v2, update2_v2, update3_v2,
            futureVersionNote
        );
        
        notificationScrollPane = new ScrollPane();
        notificationScrollPane.setContent(notifContentVBox);
        notificationScrollPane.setFitToWidth(true);
        notificationScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        notificationScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        notificationScrollPane.setPrefHeight(250); // Ajusta según el tamaño de la ventana
        notificationScrollPane.setPadding(Insets.EMPTY);


        // Panel de Opciones
        optionsPane = new VBox(15);
        optionsPane.setAlignment(Pos.TOP_CENTER);
        optionsPane.setPadding(new Insets(20));

        Label opcionesTitle = new Label("Opciones de la Aplicación");
        opcionesTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane optionsGrid = new GridPane();
        optionsGrid.setHgap(10);
        optionsGrid.setVgap(10);
        optionsGrid.setAlignment(Pos.CENTER);
        optionsGrid.setPadding(new Insets(15, 0, 0, 0));

        String boxStyle = "-fx-background-color: #f0f0f0; " +
                          "-fx-border-color: #d0d0d0; " +
                          "-fx-border-width: 1; " +
                          "-fx-border-radius: 8; " +
                          "-fx-padding: 10px; " +
                          "-fx-pref-width: 100px; " +
                          "-fx-pref-height: 90px;";

        batteryOptionBox = createOptionBox("🔋", "Batería", boxStyle);
        optionsGrid.add(batteryOptionBox, 0, 0);

        msvFastOptionBox = createOptionBox("💬", "MSV Fast", boxStyle);
        optionsGrid.add(msvFastOptionBox, 1, 0);

        VBox translationBox = createOptionBox("🗣️", "Traducción", boxStyle);
        optionsGrid.add(translationBox, 2, 0);

        VBox languageBox = createOptionBox("🔤", "Idioma T.", boxStyle);
        optionsGrid.add(languageBox, 0, 1);

        VBox tutorialBox = createOptionBox("💡", "Tutorial", boxStyle);
        optionsGrid.add(tutorialBox, 1, 1);

        VBox helpBox = createOptionBox("❓", "Ayuda", boxStyle);
        optionsGrid.add(helpBox, 2, 1);

        optionsPane.getChildren().addAll(opcionesTitle, optionsGrid);

        // --- INICIO DE LA PANTALLA DETALLADA DE BATERÍA ---
        batteryDetailPane = new VBox(15);
        batteryDetailPane.setPadding(new Insets(20));
        batteryDetailPane.setAlignment(Pos.TOP_LEFT);

        HBox batteryDetailHeader = new HBox(10);
        batteryDetailHeader.setAlignment(Pos.CENTER_LEFT);
        batteryDetailBackBtn = new Button("<");
        batteryDetailBackBtn.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: black;");
        Label batteryDetailTitle = new Label("Batería");
        batteryDetailTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        batteryDetailHeader.getChildren().addAll(batteryDetailBackBtn, batteryDetailTitle);

        VBox percentageAndTimeContainer = new VBox(5);
        percentageAndTimeContainer.setAlignment(Pos.CENTER_LEFT);
        detailBatteryPercentageLabel = new Label("100 %");
        detailBatteryPercentageLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        timeRemainingLabel = new Label("Tiempo restante: aproximadamente -- h\ny -- min");
        timeRemainingLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: grey;");
        percentageAndTimeContainer.getChildren().addAll(detailBatteryPercentageLabel, timeRemainingLabel);

        // Barra de progreso de la batería
        StackPane progressBarContainer = new StackPane();
        progressBarContainer.setPrefHeight(15);
        progressBarContainer.setMaxWidth(360);
        progressBarContainer.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5;");

        batteryProgressBar = new Rectangle(0, 15);
        batteryProgressBar.setStyle("-fx-background-radius: 5;");
        batteryProgressBar.setFill(Color.LIMEGREEN);
        progressBarContainer.getChildren().add(batteryProgressBar);
        StackPane.setAlignment(batteryProgressBar, Pos.CENTER_LEFT);

        Label batterySaverTitle = new Label("Ahorro de batería");
        batterySaverTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 0 0 0;");

        Label batterySaverInfo = new Label("En el modo Ahorro de batería, el sistema limitará o desactivará las actividades en segundo plano, algunos efectos visuales y ciertas funciones.");
        batterySaverInfo.setWrapText(true);
        batterySaverInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Toggle de Ahorro de batería
        HBox batterySaverToggleBox = new HBox(10);
        batterySaverToggleBox.setAlignment(Pos.CENTER_LEFT);
        final double BS_RAIL_WIDTH = 40;
        final double BS_RAIL_HEIGHT = 18;
        final double BS_BUTTON_DIAMETER = 24;

        batterySaverToggleContainer = new StackPane();
        batterySaverToggleContainer.setPrefSize(BS_RAIL_WIDTH, BS_RAIL_HEIGHT);
        batterySaverToggleContainer.setStyle("-fx-background-color: gray; -fx-background-radius: " + (BS_RAIL_HEIGHT / 2) + ";");

        batterySaverToggleBtn = new Button("");
        batterySaverToggleBtn.setShape(new Circle(BS_BUTTON_DIAMETER / 2));
        batterySaverToggleBtn.setPrefSize(BS_BUTTON_DIAMETER, BS_BUTTON_DIAMETER);
        batterySaverToggleBtn.setStyle("-fx-background-color: white; -fx-background-radius: " + (BS_BUTTON_DIAMETER / 2) + ";");
        batterySaverToggleBtn.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));

        StackPane.setAlignment(batterySaverToggleBtn, Pos.CENTER_LEFT);
        double bsVerticalMarginForButton = (BS_RAIL_HEIGHT - BS_BUTTON_DIAMETER) / 2;
        StackPane.setMargin(batterySaverToggleBtn, new Insets(bsVerticalMarginForButton, 0, bsVerticalMarginForButton, 0));

        batterySaverToggleContainer.getChildren().add(batterySaverToggleBtn);

        batterySaverToggleBox.getChildren().addAll(batterySaverToggleContainer);

        Label usageTitle = new Label("Uso de la batería");
        usageTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 0 5px 0;");

        HBox lastChargeBox = new HBox();
        lastChargeBox.setAlignment(Pos.CENTER_LEFT);
        Label lastChargeLabel = new Label("Desde la última carga completa");
        lastChargeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: grey;");
        lastChargeTimeLabel = new Label("-- h y -- min");
        lastChargeTimeLabel.setStyle("-fx-font-size: 13px;");
        lastChargeBox.getChildren().addAll(lastChargeLabel, lastChargeTimeLabel);
        HBox.setHgrow(lastChargeLabel, Priority.ALWAYS);

        HBox screenUsageBox = new HBox();
        screenUsageBox.setAlignment(Pos.CENTER_LEFT);
        Label screenUsageLabel = new Label("Tiempo de uso de la pantalla");
        screenUsageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: grey;");
        screenUsageTimeLabel = new Label("-- h y -- min");
        screenUsageTimeLabel.setStyle("-fx-font-size: 13px;");
        screenUsageBox.getChildren().addAll(screenUsageLabel, screenUsageTimeLabel);
        HBox.setHgrow(screenUsageLabel, Priority.ALWAYS);

        batteryDetailPane.getChildren().addAll(
                batteryDetailHeader,
                percentageAndTimeContainer,
                progressBarContainer,
                batterySaverTitle,
                batterySaverInfo,
                batterySaverToggleBox,
                usageTitle,
                lastChargeBox,
                screenUsageBox
        );

        // --- INICIO DE LA PANTALLA DE MENSAJES RÁPIDOS (MSV Fast) ---
        msvFastPane = new VBox(10);
        msvFastPane.setPadding(new Insets(20));
        msvFastPane.setAlignment(Pos.TOP_LEFT);

        HBox msvFastHeader = new HBox(10);
        msvFastHeader.setAlignment(Pos.CENTER_LEFT);
        msvFastBackBtn = new Button("<");
        msvFastBackBtn.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: black;");
        Label msvFastTitle = new Label("Mensajes Rápidos");
        msvFastTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        msvFastHeader.getChildren().addAll(msvFastBackBtn, msvFastTitle);

        quickMessagesListView = new ListView<>();
        quickMessagesListView.setPrefHeight(250);

        addMessageBtn = new Button("Añadir");
        editMessageBtn = new Button("Editar");
        deleteMessageBtn = new Button("Eliminar");
        sendMessageBtn = new Button("Enviar");

        HBox buttonBox = new HBox(10, addMessageBtn, editMessageBtn, deleteMessageBtn, sendMessageBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        msvFastPane.getChildren().addAll(msvFastHeader, quickMessagesListView, buttonBox);
        // --- FIN DE LA PANTALLA DE MENSAJES RÁPIDOS (MSV Fast) ---


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

        notificationSimButton = new Button("100%");
        notificationSimButton.setStyle("-fx-font-size: 12px; -fx-padding: 3px 6px; -fx-background-color: transparent; -fx-text-fill: grey; -fx-border-color: lightgrey; -fx-border-radius: 3;");
        BorderPane.setAlignment(notificationSimButton, Pos.TOP_RIGHT);
        BorderPane.setMargin(notificationSimButton, new Insets(10, 10, 0, 0));
        this.setTop(notificationSimButton);

        lowBatteryMessageLabel = new Label("¡Batería baja del guante!");
        lowBatteryMessageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox notificationContent = new VBox(10, lowBatteryMessageLabel);
        notificationContent.setAlignment(Pos.CENTER);
        notificationContent.setPadding(new Insets(20));
        notificationContent.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8); -fx-background-radius: 10; -fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 10;");
        notificationContent.setMaxWidth(250);
        notificationContent.setMaxHeight(100);

        lowBatteryNotificationPane = new StackPane(notificationContent);
        lowBatteryNotificationPane.setAlignment(Pos.CENTER);
        lowBatteryNotificationPane.setManaged(false);
        lowBatteryNotificationPane.setVisible(false);
        lowBatteryNotificationPane.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);

        centerContentStack = new StackPane();
        centerContentStack.getChildren().add(mainArea);
        centerContentStack.getChildren().add(bluetoothPane);
        centerContentStack.getChildren().add(notificationScrollPane);
        centerContentStack.getChildren().add(optionsPane);
        centerContentStack.getChildren().add(batteryDetailPane);
        centerContentStack.getChildren().add(msvFastPane);
        centerContentStack.getChildren().add(lowBatteryNotificationPane);


        mainArea.setVisible(true);
        mainArea.setManaged(true);
        bluetoothPane.setVisible(false); bluetoothPane.setManaged(false);
        notificationScrollPane.setVisible(false); notificationScrollPane.setManaged(false);
        optionsPane.setVisible(false); optionsPane.setManaged(false);
        batteryDetailPane.setVisible(false); batteryDetailPane.setManaged(false);
        msvFastPane.setVisible(false); msvFastPane.setManaged(false);


        setCenter(centerContentStack);
        setBottom(menuBar);
    }

    private VBox createOptionBox(String emojiIcon, String labelText, String style) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setStyle(style);

        Label iconLabel = new Label(emojiIcon);
        iconLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #333333;");
        box.getChildren().add(iconLabel);

        Label textLabel = new Label(labelText);
        textLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666; -fx-text-alignment: center;");
        textLabel.setWrapText(true);

        box.getChildren().add(textLabel);
        return box;
    }

    public Button getNotificationSimButton() { return notificationSimButton; }
    public StackPane getLowBatteryNotificationPane() { return lowBatteryNotificationPane; }
    public Label getLowBatteryMessageLabel() { return lowBatteryMessageLabel; }
    public StackPane getCenterContentStack() { return centerContentStack; }

    public VBox getBatteryDetailPane() { return batteryDetailPane; }
    public Button getBatteryDetailBackBtn() { return batteryDetailBackBtn; }
    public Label getDetailBatteryPercentageLabel() { return detailBatteryPercentageLabel; }
    public Rectangle getBatteryProgressBar() { return batteryProgressBar; }
    public Label getTimeRemainingLabel() { return timeRemainingLabel; }
    public Button getBatterySaverToggleBtn() { return batterySaverToggleBtn; }
    public StackPane getBatterySaverToggleContainer() { return batterySaverToggleContainer; }
    public Label getLastChargeTimeLabel() { return lastChargeTimeLabel; }
    public Label getScreenUsageTimeLabel() { return screenUsageTimeLabel; }
    public VBox getBatteryOptionBox() { return batteryOptionBox; }
    public VBox getMsvFastOptionBox() { return msvFastOptionBox; }

    public TextArea getTranslatedTextArea() { return translatedTextArea; }
    public Button getPlayPauseBtn() { return playPauseBtn; }
    public Button getMuteBtn() { return muteBtn; }

    public Button getBluetoothToggleBtn() { return bluetoothToggleBtn; }
    public StackPane getBluetoothToggleContainer() { return bluetoothToggleContainer; }
    public Label getBluetoothInfoLabel() { return bluetoothInfoLabel; }

    public Label getPairedDevicesLabel() { return pairedDevicesLabel; }
    public Button getGtlsBtn() { return gtlsBtn; }
    public Label getGtlsConnectionStatusLabel() { return gtlsConnectionStatusLabel; }

    public Button getInicioBtn() { return inicioBtn; }
    public Button getBluetoothMenuBtn() { return bluetoothMenuBtn; }
    public Button getNotificacionBtn() { return notificacionBtn; }
    public Button getOpcionesBtn() { return opcionesBtn; }

    public ListView<String> getQuickMessagesListView() { return quickMessagesListView; }
    public Button getAddMessageBtn() { return addMessageBtn; }
    public Button getEditMessageBtn() { return editMessageBtn; }
    public Button getDeleteMessageBtn() { return deleteMessageBtn; }
    public Button getSendMessageBtn() { return sendMessageBtn; }
    public Button getMsvFastBackBtn() { return msvFastBackBtn; }

    private void setCenterContent(javafx.scene.Node newContent) {
        mainArea.setVisible(false); mainArea.setManaged(false);
        bluetoothPane.setVisible(false); bluetoothPane.setManaged(false);
        notificationScrollPane.setVisible(false); notificationScrollPane.setManaged(false);
        optionsPane.setVisible(false); optionsPane.setManaged(false);
        batteryDetailPane.setVisible(false); batteryDetailPane.setManaged(false);
        msvFastPane.setVisible(false); msvFastPane.setManaged(false);

        newContent.setVisible(true);
        newContent.setManaged(true);
    }

    public void showMainArea() { setCenterContent(mainArea); }
    public void showBluetoothPane() { setCenterContent(bluetoothPane); }
    public void showNotifPane() { setCenterContent(notificationScrollPane); }
    public void showOptionsPane() { setCenterContent(optionsPane); }
    public void showBatteryDetailPane() { setCenterContent(batteryDetailPane); }
    public void showMsvFastPane() { setCenterContent(msvFastPane); }

    public Optional<String> showTextInputDialog(String title, String header, String content, String initialValue) {
        TextInputDialog dialog = new TextInputDialog(initialValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }
}