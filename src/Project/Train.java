package Project;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;


public class Train extends Thread {



    private String id; //train id
    volatile private Rectangle locomotive; //front part (locomotice)
    volatile private Rectangle cargo_front; //middle part
    volatile private Rectangle cargo_back; //back part
    volatile private int pos_x; //actual x position
    volatile private int pos_y; //actual y position
    volatile private Color color; //color
    private Slider slider; //set speed of trains
    private int trace_number; //programed trace of train
    volatile private boolean ended_move=false;//indicate if train ended part of move( from station to station);


    volatile private static GridPane gridPane; //set grid in background
    volatile private static Rectangle[][] square; //all rectangles
    volatile private static Rectangle[] station; //stations
    private static Button button;//start/pause button
    private static Slider waiting_slider;//set time for waiting on station
    private static boolean animation_flag=false; //check if button is pressed
    volatile private static Vector<Rectangle> tunnel =  new Vector<>();
    //tracks
    volatile private static Vector<Rectangle> track_0 = new Vector<>();
    volatile private static Vector<Rectangle> track_1 = new Vector<>();
    volatile private static Vector<Rectangle> track_2 = new Vector<>();
    volatile private static Vector<Rectangle> track_3 = new Vector<>();
    volatile private static Vector<Rectangle> track_4 = new Vector<>();

    volatile private static Semaphore semaphore = new Semaphore(1);//semaphore


    //0->right track of station 1
    //1->bottom track of station 1
    //2->right track of station 2
    //3->top track of station 3
    //4->left track of station 3
    volatile private static boolean[] free_track = {false,true,false,false,true};

    Train(String id, int x, int y, Color color,int trace_number,Slider slider)
    {
        this.id=id;
        this.pos_x=x;
        this.pos_y=y;
        this.color=color;
        this.trace_number= trace_number;
        this.slider=slider;

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
//        locomotive.setId(this.id);
//
//        locomotive.setOnMouseClicked(event -> {
//            System.out.println(this.id);
//        });

        cargo_front.setFill(this.color);
//        cargo_front.setId(this.id);
//
//        cargo_front.setOnMouseClicked(event -> {
//            System.out.println(this.id);
//        });

        cargo_back.setFill(this.color);
//        cargo_back.setId(this.id);
//
//        cargo_back.setOnMouseClicked(event -> {
//            System.out.println(this.id);
//        });

    }
    static void setup(GridPane g_grid,Button b_button,Slider w_waiting_slider)
    {
        gridPane = g_grid;
        button=b_button;
        waiting_slider=w_waiting_slider;
        button.setOnAction(event -> animation_flag=!animation_flag);
    }
    static private synchronized void color_track(Vector<Rectangle> track,Color color)
    {
        for (Rectangle r:track) {
            r.setStroke(color);
        }
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
    private synchronized void draw(Rectangle next_title)
    {
        Rectangle prev_title=cargo_back;
        cargo_back=cargo_front;
        cargo_front=locomotive;
        locomotive=next_title;
        prev_title.setFill(Color.GRAY);
        locomotive.setFill(this.color);
    }
    private synchronized void cleanup(Vector<Rectangle> path,int z)
    {
        for(int j=z;j>=0;j--)
        {
            path.get(j).setFill(Color.GRAY);
        }
    }

    private synchronized void move (Vector<Rectangle> path)
    {

        boolean get_permision=false;
        boolean was_in_tunnel=false;
        boolean was_waited=false;
        for (int i=0;i<path.size();i++)
        {
                //entering tunnel
                if(tunnel.contains(path.get(i)))
                {
                    //System.out.println(this.id + " want get permission");
                    if(semaphore.tryAcquire() || get_permision)
                    {
                        get_permision=true;
                        was_in_tunnel=true;
                        //System.out.println(this.id + " get permission");
                        if(was_waited)
                        {

                            draw(path.get(i-1));
                            draw(path.get(i));
                            cleanup(path,i-3);
                            was_waited=false;
                        }
                        else
                        {
                            draw(path.get(i));
                            cleanup(path,i-3);
                        }
                        for (Rectangle x:tunnel) {
                            x.setStroke(Color.ORANGERED);
                        }
                    }
                    else
                    {
                        while (semaphore.availablePermits()==0)
                        {
                            try {

                                //System.out.println(this.id + " wait for permission");
                                was_waited=true;
                                Thread.sleep(100);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //leaving tunnel
                else if(!tunnel.contains(locomotive) && !tunnel.contains(cargo_front)&& !tunnel.contains(cargo_back) && was_in_tunnel)
                {
                    //System.out.println(this.id + " left perrmision and tunnel");
                    draw(path.get(i));
                    cleanup(path,i-3);
                    for (Rectangle x:tunnel
                    ) {
                        x.setStroke(Color.GREENYELLOW);
                    }

                    was_in_tunnel=false;
                    get_permision=false;
                    semaphore.release();

                }
                //other route
                else
                {
                    draw(path.get(i));
                    cleanup(path,i-3);
                    //System.out.println(this.id + " just drive");
                    get_permision=false;
                    was_waited=false;

                }
                if(path.lastElement()==path.get(i))
                {
                    ended_move=true;
                }
                else
                {
                    ended_move=false;
                }
                //setting speed of animation
                try
                {
                    long max = new Double(this.slider.getMax()).longValue();
                    long value = new Double(this.slider.getValue()).longValue();
                    Thread.sleep(max-value);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }


    }
    private synchronized void changeDirection()
    {
        Rectangle temp = locomotive;
        locomotive=cargo_back;
        cargo_back=temp;
    }
    private synchronized void wait_on_station()
    {
        try {
            System.out.println(this.id + " is waiting on station...");
            int time= (int)waiting_slider.getValue();
            Thread.sleep(new Random().nextInt(time));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void run() {

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
                        if(free_track[4] && animation_flag)
                        {
                            free_track[4]=false;
                            color_track(track_4,Color.ORANGERED);
                            move(path);
                            if(ended_move)
                           {

                               free_track[0]=true;
                               color_track(track_0,Color.GREENYELLOW);
                               ended_move=false;
                               wait_on_station();
                               Collections.reverse(path);
                               changeDirection();
                            }
                        }
                        else
                        {
                            try {
                                Thread.currentThread().wait(100);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        if(free_track[0] && animation_flag)
                        {
                            free_track[0]=false;
                            color_track(track_0,Color.ORANGERED);
                            move(path);
                            if(ended_move)
                            {
                                free_track[4]=true;
                                color_track(track_4,Color.GREENYELLOW);
                                ended_move=false;
                                wait_on_station();
                                Collections.reverse(path);
                                changeDirection();
                            }
                        }
                        else
                        {
                            try {
                                Thread.currentThread().wait(100);
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
                    if(free_track[1] && animation_flag)
                    {
                        free_track[1]=false;
                        color_track(track_1,Color.ORANGERED);
                        move(path);
                        if(ended_move)
                        {
                            free_track[2]=true;
                            color_track(track_2,Color.GREENYELLOW);
                            ended_move=false;
                            wait_on_station();
                            Collections.reverse(path);
                            changeDirection();

                        }
                    }
                    else
                    {
                        try {
                            Thread.currentThread().wait(100);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    if(free_track[2] && animation_flag)
                    {
                        free_track[2]=false;
                        color_track(track_2,Color.ORANGERED);
                        move(path);
                        if(ended_move)
                        {
                            free_track[1]=true;
                            color_track(track_1,Color.GREENYELLOW);
                            ended_move=false;
                            wait_on_station();
                            Collections.reverse(path);
                            changeDirection();
                        }
                    }
                    else
                    {
                        try {
                            Thread.currentThread().wait(100);
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
                    if(free_track[0] && animation_flag)
                    {
                        free_track[0]=false;
                        color_track(track_0,Color.ORANGERED);
                        move(path);
                        if(ended_move)
                        {
                            free_track[3]=true;
                            color_track(track_3,Color.GREENYELLOW);
                            ended_move=false;
                            wait_on_station();
                            Collections.reverse(path);
                            changeDirection();
                        }

                    }
                    else
                    {
                        try {
                            Thread.currentThread().wait(100);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                    if(free_track[3] && animation_flag)
                    {
                        free_track[3]=false;
                        color_track(track_3,Color.ORANGERED);
                        move(path);
                        if(ended_move)
                        {
                            free_track[0]=true;
                            color_track(track_0,Color.GREENYELLOW);
                            ended_move=false;
                            wait_on_station();
                            Collections.reverse(path);
                            changeDirection();
                        }
                    }
                    else
                    {
                        try {
                            Thread.currentThread().wait(100);
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


