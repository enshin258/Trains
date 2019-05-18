package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Pane pane;

    @FXML
    public GridPane panel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Train.setGridPane(panel);
        Train.draw_map();

        Train t1 = new Train("Blue Train",3,0, Color.BLUE,1,300);
        Train t2 = new Train("Orange Train",3,16, Color.ORANGE,2,200);
        Train t3 = new Train("Yellow Train",10,13, Color.YELLOW,3,100);

        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();

    }
}
