package Project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;

import java.net.URL;
import java.util.ResourceBundle;



public class Controller implements Initializable {


    @FXML
    public Pane pane;

    @FXML
    public GridPane panel;

    @FXML Button button;

    @FXML Slider blue_slider;
    @FXML Slider orange_slider;
    @FXML Slider yellow_slider;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Train.setup(panel, button);
        Train.draw_map();

        Train t1 = new Train("Blue Train",3,0, Color.BLUE,1,blue_slider,500);
        Train t2 = new Train("Orange Train",3,16, Color.ORANGE,2,orange_slider,500);
        Train t3 = new Train("Yellow Train",10,13, Color.YELLOW,3,yellow_slider,500);

        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);

        t1.start();
        t2.start();
        t3.start();



    }
}
