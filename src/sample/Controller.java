package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


    //testing pausing animation with button



    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Train.setGridPane(panel);
        Train.draw_map();


        Train t1 = new Train("Pociag niebieski",1,0, Color.BLUE,1);
        Train t2 = new Train("Pociag pomaranczowy",1,16, Color.ORANGE,2);
        Train t3 = new Train("Pociag zolty",10,15, Color.YELLOW,3);

        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();





    }
}
