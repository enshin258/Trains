package sample;

import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import org.w3c.dom.css.Rect;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;


public class Train extends Thread {

    private String id; //train id
    private Rectangle locomotive; //front part (locomotice)
    private Rectangle cargo_front; //middle part
    private Rectangle cargo_back; //back part
    private int pos_x; //actual x position
    private int pos_y; //actual y position
    private Color color; //color
    private int milis_time; //speed
    private int trace_number; //programed trace of train


    private static GridPane gridPane; //set grid in background
    private static Rectangle[][] square; //all rectangles
    private static Rectangle[] station; //stations
    private static Rectangle crossing; //crossing in middle
    private static Semaphore semaphore = new Semaphore(1);//semaphore

    //0->right track of station 1
    //1->bottom track of station 1
    //2->right track of station 2
    //3->top track of station 3
    //4->left track of station 3
    private static volatile boolean[] free_track = {false,true,false,false,true};
    private static Button button = new Button("Start/Pause"); //button for start and pause animation

    private static boolean animation_flag = false; //on button click its changes so animation can be started/stopped



    Train(String id, int x, int y, Color color,int trace_number,int milis_time)
    {
        this.id=id;
        this.pos_x=x;
        this.pos_y=y;
        this.color=color;
        this.trace_number= trace_number;
        this.milis_time=milis_time;


        locomotive = square[pos_x][pos_y];
        switch (trace_number)
        {
            case 1:
            {
                cargo_front = square[pos_x-1][pos_y];
                cargo_back = square[pos_x-2][pos_y];
                break;
            }
            case 2:
            {
                cargo_front = square[pos_x-1][pos_y];
                cargo_back = square[pos_x-2][pos_y];
                break;
            }
            case 3:
            {
                cargo_front = square[pos_x][pos_y+1];
                cargo_back = square[pos_x][pos_y+2];
                break;
            }
        }

        locomotive.setFill(this.color);
        locomotive.setId(this.id);
        locomotive.toFront();

        locomotive.setOnMouseClicked(event -> {
            System.out.println("Locomotive of: " + this.id);
        });

        cargo_front.setFill(this.color);
        cargo_front.setId(this.id);
        cargo_front.toFront();

        cargo_front.setOnMouseClicked(event -> {
            System.out.println("Front cargo of: " + this.id);
        });

        cargo_back.setFill(this.color);
        cargo_back.setId(this.id);
        cargo_back.toFront();

        cargo_back.setOnMouseClicked(event -> {
            System.out.println("Back cargo of: " + this.id);
        });

    }
    static void setGridPane(GridPane grid)
    {
        gridPane = grid;
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

        //declare crossing
        crossing = square[5][8];
        crossing.setStroke(Color.WHITE);
        crossing.toFront();

        //button for start and stop animation
        button.setOnAction(event -> {
            animation_flag=!animation_flag;
        });
        gridPane.add(button,0,0);
    }


    void move (Vector<Rectangle> path)
    {
        for (Rectangle next_title:path) {
            Rectangle prev_title = cargo_back;

            cargo_back=cargo_front;
            cargo_front=locomotive;
            locomotive=next_title;


            prev_title.setFill(Color.GRAY);
            next_title.setFill(this.color);


            try {
                Thread.sleep(milis_time);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    void changeDirection()
    {
        Rectangle temp = locomotive;
        locomotive=cargo_back;
        cargo_back=temp;
    }


    @Override
    public void run() {

        switch (trace_number)
        {
            case 1://from left top corner to bottom right
            {
                //creating path for train
                Vector<Rectangle> path = new Vector<>();

                path.add(square[1][0]);
                path.add(square[2][0]);
                path.add(square[3][0]);
                path.add(square[4][0]);
                path.add(square[5][0]);
                for (int i = 0; i < 16; i++) {
                    path.add(square[5][i+1]);
                }
                path.add(square[6][16]);
                path.add(square[7][16]);
                path.add(square[8][16]);
                path.add(square[9][16]);

                while (true)
                {
                    if(animation_flag)
                    {

                        semaphore.acquireUninterruptibly();
                        if(free_track[4])
                        {
                            move(path);
                            free_track[4]=false;
                            free_track[0]=true;
                        }
                        semaphore.release();
                        try
                        {
                            System.out.println(this.id + " is waiting on station...");
                            Thread.sleep(new Random().nextInt(1000));
                        }
                        catch ( Exception e)
                        {
                            e.printStackTrace();
                        }

                        changeDirection();
                        Collections.reverse(path);

                        semaphore.acquireUninterruptibly();
                        if(free_track[0])
                        {
                            move(path);
                            free_track[0]=false;
                            free_track[4]=true;

                        }
                        semaphore.release();

                        Collections.reverse(path);
                        try
                        {
                            System.out.println(this.id + " is waiting on station...");
                            Thread.sleep(new Random().nextInt(1000));
                        }
                        catch ( Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }
            case 2://from left bottom to left top
            {
                Vector<Rectangle> path = new Vector<>();

                path.add(square[1][16]);
                path.add(square[2][16]);
                path.add(square[3][16]);
                path.add(square[4][16]);
                path.add(square[5][16]);
                for (int i = 1; i < 9; i++) {
                    path.add(square[5][16-i]);
                }
                path.add(square[4][8]);
                path.add(square[3][8]);
                path.add(square[2][8]);
                path.add(square[1][8]);
                path.add(square[0][8]);

                path.add(square[0][7]);
                path.add(square[0][6]);
                path.add(square[0][5]);
                path.add(square[0][4]);
                path.add(square[0][3]);
                path.add(square[0][2]);
                path.add(square[0][1]);


                while (true)
                {
                    if(animation_flag)
                    {
                        semaphore.acquireUninterruptibly();
                        if(free_track[1])
                        {
                            move(path);
                            free_track[1]=false;
                            free_track[2]=true;
                        }
                        semaphore.release();
                        try
                        {
                            System.out.println(this.id + " is waiting on station...");
                            Thread.sleep(new Random().nextInt(1000));
                        }
                        catch ( Exception e)
                        {
                            e.printStackTrace();
                        }

                        changeDirection();
                        Collections.reverse(path);

                        semaphore.acquireUninterruptibly();
                        if(free_track[2])
                        {
                            move(path);
                            free_track[2]=false;
                            free_track[1]=true;

                        }
                        semaphore.release();

                        Collections.reverse(path);
                        try
                        {
                            System.out.println(this.id + " is waiting on station...");
                            Thread.sleep(new Random().nextInt(1000));
                        }
                        catch ( Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            case 3:
            {
                Vector<Rectangle> path = new Vector<>();
                path.add(square[10][15]);
                path.add(square[10][14]);
                path.add(square[10][13]);
                path.add(square[10][12]);
                path.add(square[10][11]);
                path.add(square[10][10]);
                path.add(square[10][9]);
                path.add(square[10][8]);

                path.add(square[9][8]);
                path.add(square[8][8]);
                path.add(square[7][8]);
                path.add(square[6][8]);
                path.add(square[5][8]);

                path.add(square[5][7]);
                path.add(square[5][6]);
                path.add(square[5][5]);
                path.add(square[5][4]);
                path.add(square[5][3]);
                path.add(square[5][2]);
                path.add(square[5][1]);
                path.add(square[5][0]);

                path.add(square[4][0]);
                path.add(square[3][0]);
                path.add(square[2][0]);
                path.add(square[1][0]);



                while (true)
                {
                    if(animation_flag)
                    {
                        semaphore.acquireUninterruptibly();
                        if(free_track[0])
                        {
                            move(path);
                            free_track[0]=false;
                            free_track[3]=true;
                        }
                        semaphore.release();
                        try
                        {
                            System.out.println(this.id + " is waiting on station...");
                            Thread.sleep(new Random().nextInt(1000));
                        }
                        catch ( Exception e)
                        {
                            e.printStackTrace();
                        }

                        changeDirection();
                        Collections.reverse(path);

                        semaphore.acquireUninterruptibly();
                        if(free_track[3])
                        {
                            move(path);
                            free_track[3]=false;
                            free_track[0]=true;

                        }
                        semaphore.release();

                        Collections.reverse(path);
                        try
                        {
                            System.out.println(this.id + " is waiting on station...");
                            Thread.sleep(new Random().nextInt(3000));
                        }
                        catch ( Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }
}


