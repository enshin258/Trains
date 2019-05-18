package sample;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.Random;


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
    private int frame=1; //used for moving
    private boolean[] moved = new boolean[9]; //check if part of animation was played


    private static GridPane gridPane; //set grid in background
    private static Rectangle[][] square; //all rectangles
    private static Rectangle[] station; //stations
    private static Button button = new Button("Start/Pause"); //button for start and pause animation

    private static boolean animation_flag = false; //on button click its changes so animation can be started/stopped
    private enum direction{left,right,up,down} //set direction of moving



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

        //button for start and stop animation
        button.setOnAction(event -> {
            animation_flag=!animation_flag;
        });
        gridPane.add(button,0,0);
    }
    void paint(int new_x,int new_y,int old_x,int old_y,direction dir)
    {

        locomotive=square[new_x][new_y];
        locomotive.setFill(this.color);
        locomotive.setId(this.id);
        square[old_x][old_y].setFill(Color.GRAY);
        square[old_x][old_y].setId("x:" + old_x + " " + "y:" + old_y);
        square[old_x][old_y].setOnMouseClicked(event -> {
            Rectangle source = (Rectangle)event.getSource();
            System.out.println(source.getId());
        });
        frame++;
    }
    void move(direction dir,int number_of_titles,long time_milis,int number_of_animation)
    {
        switch (dir)
        {
            case up://ok
            {
                int x_new = this.pos_x;
                int x_old = this.pos_x;
                int y_new = this.pos_y-frame;
                int y_old = this.pos_y-frame+1;
                System.out.println("up move number:" + frame);
                paint(x_new,y_new,x_old,y_old,dir);

                if(frame>number_of_titles)
                {
                    frame=1;
                    this.pos_y=y_new;
                    moved[number_of_animation]=true;
                    break;
                }
                try
                {
                    Thread.sleep(time_milis);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
            case down://ok
            {
                int x_new = this.pos_x;
                int x_old = this.pos_x;
                int y_new = this.pos_y+frame;
                int y_old = this.pos_y+frame-1;
                System.out.println("down move number:" + frame);
                paint(x_new,y_new,x_old,y_old,dir);

                if(frame>number_of_titles)
                {
                    this.frame=1;
                    this.pos_y=y_new;
                    moved[number_of_animation]=true;
                    break;
                }
                try
                {
                    Thread.sleep(time_milis);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
            case left://ok
            {
                int y_new = this.pos_y;
                int y_old = this.pos_y;
                int x_new = this.pos_x-this.frame;
                int x_old = this.pos_x-this.frame+1;
                System.out.println("left move number:" + this.frame);
                paint(x_new,y_new,x_old,y_old,dir);

                if(frame>number_of_titles)
                {
                    this.frame=1;
                    this.pos_x = x_new;
                    moved[number_of_animation]=true;
                    break;
                }
                try
                {
                    Thread.sleep(time_milis);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
            case right://ok
            {
                int y_new = this.pos_y;
                int y_old = this.pos_y;
                int x_new = this.pos_x+this.frame;
                int x_old = this.pos_x+this.frame-1;
                System.out.println("right move number:" + this.frame);
                paint(x_new,y_new,x_old,y_old,dir);

                if(this.frame>number_of_titles)
                {
                    this.frame=1;
                    this.pos_x = x_new;
                    moved[number_of_animation]=true;
                    break;
                }
                try
                {
                    Thread.sleep(time_milis);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    @Override
    public void run() {

        switch (trace_number)
        {
            case 1://from left top corner to bottom right
            {
                while(true)
                {
                    if(animation_flag)
                    {
                        if(!moved[1])
                        {
                            this.move(direction.right,2,milis_time,1);
                        }
                        else if(!moved[2])
                        {
                            this.move(direction.down,16,milis_time,2);
                        }
                        else if(!moved[3])
                        {
                            this.move(direction.right,4,milis_time,3);
                        }
                        else if (!moved[4])
                        {
                            this.move(direction.left,4,milis_time,4);
                        }
                        else if(!moved[5])
                        {
                            this.move(direction.up,16,milis_time,5);
                        }
                        else if(!moved[6])
                        {
                            this.move(direction.left,2,milis_time,6);
                        }
                        else
                        {
                            try
                            {
                                Thread.sleep(1000);
                                System.out.println(this.id + "Waiting on station...");
                                Random random = new Random();
                                Thread.sleep(random.nextInt(10000));
                                System.out.println(this.id + "Started again!");
                                for (int i = 0; i < moved.length; i++) {
                                    moved[i]=false;
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        try
                        {
                            Thread.sleep(1000);
                            System.out.println("Animation stopped!");
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
                {
                    while(true)
                    {
                        if(animation_flag)
                        {

                            if(!moved[1])
                            {
                                this.move(direction.right,2,milis_time,1);
                            }
                            else if(!moved[2])
                            {
                                this.move(direction.up,8,milis_time,2);
                            }
                            else if(!moved[3])
                            {
                                this.move(direction.left,5,milis_time,3);
                            }
                            else if (!moved[4])
                            {
                                this.move(direction.up,5,milis_time,4);
                            }
                            else if(!moved[5])
                            {
                                this.move(direction.down,5,milis_time,5);
                            }
                            else if(!moved[6])
                            {
                                this.move(direction.right,5,milis_time,6);
                            }
                            else if(!moved[7])
                            {
                                this.move(direction.down,8,milis_time,7);
                            }
                            else  if(!moved[8])
                            {
                                this.move(direction.left,2,milis_time,8);

                            }
                            else
                            {
                                try
                                {
                                    Thread.sleep(1000);
                                    System.out.println(this.id + "Waiting on station...");
                                    Random random = new Random();
                                    Thread.sleep(random.nextInt(10000));
                                    System.out.println(this.id + "Started again!");
                                    for (int i = 0; i < moved.length; i++) {
                                        moved[i]=false;
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            try
                            {
                                Thread.sleep(1000);
                                System.out.println("Animation stopped!");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
            case 3:
            {
                {
                    {
                        while(true)
                        {
                            if(animation_flag)
                            {

                                if(!moved[1])
                                {
                                    this.move(direction.up,5,milis_time,1);
                                }
                                else if(!moved[2])
                                {
                                    this.move(direction.left,5,milis_time,2);
                                }
                                else if(!moved[3])
                                {
                                    this.move(direction.up,8,milis_time,3);
                                }
                                else if (!moved[4])
                                {
                                    this.move(direction.left,1,milis_time,4);
                                }
                                else if(!moved[5])
                                {
                                    this.move(direction.right,1,milis_time,5);
                                }
                                else if(!moved[6])
                                {
                                    this.move(direction.down,8,milis_time,6);
                                }
                                else if(!moved[7])
                                {
                                    this.move(direction.right,5,milis_time,7);
                                }
                                else  if(!moved[8])
                                {
                                    this.move(direction.down,5,milis_time,8);

                                }
                                else
                                {
                                    try
                                    {
                                        Thread.sleep(1000);
                                        System.out.println(this.id + "Waiting on station...");
                                        Random random = new Random();
                                        Thread.sleep(random.nextInt(10000));
                                        System.out.println(this.id + "Started again!");
                                        for (int i = 0; i < moved.length; i++) {
                                            moved[i]=false;
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else {
                                try
                                {
                                    Thread.sleep(1000);
                                    System.out.println("Animation stopped!");
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
    }
}


