package sample;

import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;



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
    static Button button = new Button("Start/Pause");
    static boolean flague = false;
    static SequentialTransition seq_1;
    static SequentialTransition seq_2;
    static SequentialTransition seq_3;





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



        //button for testing

        button.setOnAction(event -> {
            try {
                flague=!flague;
                if(flague)
                {
                    seq_1.play();
                    seq_2.play();
                    seq_3.play();
                }
                else
                {
                    seq_1.pause();
                    seq_2.pause();
                    seq_3.pause();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        });

        gridPane.add(button,0,0);
    }






    @Override
    public void run() {

        locomotive = new Rectangle(100,40);

        locomotive.setFill(this.color);
        locomotive.setId(this.id);
        locomotive.toFront();
        locomotive.setEffect(new DropShadow());

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
            case 1://from left top corner to bottom right
            {
                TranslateTransition transition_1 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_2 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_3 = new TranslateTransition(Duration.millis(2000),locomotive);

                transition_1.setByX(400);

                transition_2.setByY(640);

                transition_3.setByX(400);

                seq_1 = new SequentialTransition(locomotive,transition_1,transition_2,transition_3);





                seq_1.setCycleCount(100);

                seq_1.setAutoReverse(true);
                //seq_1.play();


                break;
            }
            case 2://from left bottom to left top
            {
                TranslateTransition transition_1 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_2 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_3 = new TranslateTransition(Duration.millis(2000),locomotive);

                transition_1.setByX(400);

                transition_2.setByY(-640);

                transition_3.setByX(-400);

                seq_2 = new SequentialTransition(locomotive,transition_1,transition_2,transition_3);

                seq_2.setCycleCount(100);

                seq_2.setAutoReverse(true);
                break;
            }
            case 3:
            {
                TranslateTransition transition_1 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_2 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_3 = new TranslateTransition(Duration.millis(2000),locomotive);
                TranslateTransition transition_4 = new TranslateTransition(Duration.millis(2000),locomotive);

                transition_1.setByY(-280);

                transition_2.setByX(-500);

                transition_3.setByY(-320);

                transition_4.setByX(-400);

                seq_3 = new SequentialTransition(locomotive,transition_1,transition_2,transition_3,transition_4);
                seq_3.setCycleCount(100);

                seq_3.setAutoReverse(true);

                break;
            }
        }
    }
}


