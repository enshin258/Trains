package sample;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.awt.*;


public class Train extends Thread {

    private static GridPane gridPane;
    String id;
    Rectangle locomotive;
    int pos_x;
    int pos_y;
    Color color;
    static Rectangle square[][];
    static Rectangle[] station;
    int trace_number;


    Train(String id, int x, int y, Color color,int trace_number)
    {
        this.id=id;
        this.pos_x=x;
        this.pos_y=y;
        this.color=color;
        this.trace_number= trace_number;
    }
    public static void setGridPane(GridPane grid)
    {
        gridPane = grid;
    }

    public static void draw_map()
    {
        square = new Rectangle[11][17];

        for(int i=0 ; i<11 ; i++)
        {
            for (int j=0 ; j<17;j++)
            {
                square[i][j] = new Rectangle(100,40);
                square[i][j].setStroke(Color.BLACK);
                square[i][j].setFill(Color.ANTIQUEWHITE);
                square[i][j].setId("x:" + i + " " + "y:" + j);

                square[i][j].setOnMouseClicked(event -> {
                    Rectangle source = (Rectangle)event.getSource();
                    System.out.println(source.getId());
                });

                gridPane.add(square[i][j],i,j);

            }
        }

        //creating possible routes


        //horizontal parts
        //lower part
        for (int i = 0; i <11 ; i++) {
            square[i][16].setFill(Color.GRAY);
            square[i][16].setStroke(Color.BLACK);
        }
        //upper part
        for (int i = 0; i < 6; i++) {
            square[i][0].setFill(Color.GRAY);
            square[i][0].setStroke(Color.BLACK);
        }
        //middle part
        for (int i = 0; i < 11; i++) {
            square[i][8].setFill(Color.GRAY);
            square[i][8].setStroke(Color.BLACK);
        }
        //vertical parts
        //left
        for (int i = 0; i < 8; i++) {
            square[0][i].setFill(Color.GRAY);
            square[0][i].setStroke(Color.BLACK);
        }
        //middle
        for (int i = 0; i < 16; i++) {
            square[5][i].setFill(Color.GRAY);
            square[5][i].setStroke(Color.BLACK);
        }
        //right
        for (int i = 8; i < 16; i++) {
            square[10][i].setFill(Color.GRAY);
            square[10][i].setStroke(Color.BLACK);
        }

        //coloring stations

        station = new Rectangle[3];

        station[0] = square[0][0];
        station[1] = square[0][16];
        station[2] = square[10][16];

        for(int i=0;i<3;i++)
        {
            station[i].setFill(Color.RED);
            station[i].setStroke(Color.DARKRED);

        }


    }


    @Override
    public void run() {
        locomotive = new Rectangle(100,40);

        locomotive.setFill(this.color);
        locomotive.setId(this.id);
        locomotive.toFront();
        locomotive.setEffect(new MotionBlur());
        locomotive.setOnMouseClicked(event -> {
            Rectangle source = (Rectangle) event.getSource();
            System.out.println("Pociag:" + id + color.toString());
            System.out.println(source.getLayoutX());
            System.out.println(source.getTranslateX());
            System.out.println(source.getY());
        });

        gridPane.add(locomotive,pos_x,pos_y);



        switch (trace_number)
        {
            case 1:
            {
                TranslateTransition transition_1 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_2 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_3 = new TranslateTransition(Duration.millis(2000),locomotive);

                transition_1.setByX(400);
                //transition_1.setCycleCount(5);
                //transition_1.setAutoReverse(true);
                //transition_1.play();
                transition_2.setByY(640);
                //transition_2.setCycleCount(5);
                //transition_2.setAutoReverse(true);

                transition_3.setByX(400);
                //transition_3.setCycleCount(5);
                //transition_3.setAutoReverse(true);

                SequentialTransition seq_1 = new SequentialTransition(locomotive,transition_1,transition_2,transition_3);
                seq_1.setCycleCount(5);
                seq_1.setAutoReverse(true);
                seq_1.play();


                break;
            }
        }







    }
}


