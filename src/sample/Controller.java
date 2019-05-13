package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public GridPane panel;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Train.setGridPane(panel);
        Train.draw_map();
        Train t1 = new Train("Pociag niebieski",1,0, Color.BLUE);
        t1.run();

    }
}
