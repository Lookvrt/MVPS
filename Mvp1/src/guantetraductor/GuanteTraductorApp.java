package guantetraductor;

import controller.GuanteTraductorController;
import model.GuanteTraductorModel;
import view.GuanteTraductorView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuanteTraductorApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        GuanteTraductorModel model = new GuanteTraductorModel();
        GuanteTraductorView view = new GuanteTraductorView();
        GuanteTraductorController controller = new GuanteTraductorController(model, view);

        Scene scene = new Scene(view, 400, 600);
        primaryStage.setTitle("Traductor de Guante de Señas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}