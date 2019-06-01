package Project;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Metro {

    volatile static GridPane gridPane; //set grid in background
    volatile static Rectangle[][] square; //all rectangles
    volatile static Rectangle[] station; //stations
    static Button button;//start/pause button
    static Slider waiting_slider;//set time for waiting on station
    static boolean animation_flag=false; //check if button is pressed
    volatile static Vector<Rectangle> tunnel =  new Vector<>();
    //tracks in each stations
    //0->right track of station 1
    //1->bottom track of station 1
    //2->right track of station 2
    //3->top track of station 3
    //4->left track of station 3
    volatile static Vector<Rectangle> track_0 = new Vector<>();
    volatile static Vector<Rectangle> track_1 = new Vector<>();
    volatile static Vector<Rectangle> track_2 = new Vector<>();
    volatile static Vector<Rectangle> track_3 = new Vector<>();
    volatile static Vector<Rectangle> track_4 = new Vector<>();
    volatile static boolean[] free_track = {false,true,false,false,true};

    volatile static Semaphore semaphore = new Semaphore(1);//semaphore used in tunnel

    static void create_metro(GridPane g_grid, Button b_button, Slider w_waiting_slider)
    {
        gridPane = g_grid;
        button=b_button;
        waiting_slider=w_waiting_slider;
        button.setOnAction(event -> animation_flag=!animation_flag);
    }
    static synchronized void color_track(Vector<Rectangle> track,Color color)
    {
        Platform.runLater(()->{
            for (Rectangle r:track) {
                r.setStroke(color);
            }});

    }
    static void draw_map()
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
        }
        //upper part
        for (int i = 0; i < 6; i++) {
            square[i][0].setFill(Color.GRAY);
        }
        //middle part
        for (int i = 0; i < 11; i++) {
            square[i][8].setFill(Color.GRAY);
        }
        //vertical parts
        //left
        for (int i = 0; i < 8; i++) {
            square[0][i].setFill(Color.GRAY);
        }
        //middle
        for (int i = 0; i < 16; i++) {
            square[5][i].setFill(Color.GRAY);
        }
        //right
        for (int i = 8; i < 16; i++) {
            square[10][i].setFill(Color.GRAY);
        }

        //coloring stations

        station = new Rectangle[3];
        station[0] = square[0][0];
        station[1] = square[0][16];
        station[2] = square[10][16];

        for(int i=0;i<3;i++)
        {
            station[i].setFill(Color.RED);
        }

        //setting tunnel
        for (int i = 0; i < 17; i++) {
            square[5][i].setStroke(Color.WHITE);
            square[5][i].toFront();
            tunnel.add(square[5][i]);
        }
        //setting track
        //track 0
        track_0.add(square[1][0]);
        track_0.add(square[2][0]);
        track_0.add(square[3][0]);

        for (Rectangle r:track_0) {
            r.setStroke(Color.WHITE);
            r.toFront();
        }
        //track 1
        track_1.add(square[0][1]);
        track_1.add(square[0][2]);
        track_1.add(square[0][3]);
        for (Rectangle r:track_1) {
            r.setStroke(Color.WHITE);
            r.toFront();
        }
        //track 2
        track_2.add(square[1][16]);
        track_2.add(square[2][16]);
        track_2.add(square[3][16]);
        for (Rectangle r:track_2) {
            r.setStroke(Color.WHITE);
            r.toFront();
        }
        //track 3
        track_3.add(square[10][15]);
        track_3.add(square[10][14]);
        track_3.add(square[10][13]);
        for (Rectangle r:track_3) {
            r.setStroke(Color.WHITE);
            r.toFront();
        }
        //track 4
        track_4.add(square[7][16]);
        track_4.add(square[8][16]);
        track_4.add(square[9][16]);
        for (Rectangle r:track_4) {
            r.setStroke(Color.WHITE);
            r.toFront();
        }
        //coloring started track with trains
        color_track(track_0,Color.ORANGERED);
        color_track(track_2,Color.ORANGERED);
        color_track(track_3,Color.ORANGERED);
    }
}
