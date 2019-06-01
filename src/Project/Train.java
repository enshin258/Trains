package Project;

import static Project.Metro.*;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;


public class Train extends Thread {

    private String id; //train id
    volatile private Rectangle locomotive; //front part (locomotice)
    volatile private Rectangle cargo_front; //middle part
    volatile private Rectangle cargo_back; //back part
    volatile private int pos_x; //actual x position
    volatile private int pos_y; //actual y position
    volatile private Vector<Rectangle> path; //trains path
    volatile private Color color; //color
    private Slider slider; //set speed of trains
    private int trace_number; //programed trace of train
    volatile private boolean ended_move=false;//indicate if train ended part of move( from station to station);



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

                //creating path for train

                path = new Vector<>();
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
                break;
            }
            case 2:
            {
                cargo_front = square[pos_x-1][pos_y];
                cargo_back = square[pos_x-2][pos_y];

                //creating path for train

                path = new Vector<>();
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

                break;
            }
            case 3:
            {
                cargo_front = square[pos_x][pos_y+1];
                cargo_back = square[pos_x][pos_y+2];

                //creating path for train

                path = new Vector<>();
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


                break;
            }
        }

        locomotive.setFill(this.color);
        cargo_front.setFill(this.color);
        cargo_back.setFill(this.color);

    }
    private synchronized void draw(Rectangle next_title)
    {
        Rectangle prev_title=cargo_back;
        cargo_back=cargo_front;
        cargo_front=locomotive;
        locomotive=next_title;
        Platform.runLater(()->
        {
            prev_title.setFill(Color.GRAY);
            locomotive.setFill(this.color);
        });


    }
    private synchronized void move ()
    {

        boolean get_permision=false;
        boolean was_in_tunnel=false;
        for (int i=0;i<path.size();i++)
        {
                //entering tunnel
                if(tunnel.contains(path.get(i)))
                {
                    if(semaphore.tryAcquire() || get_permision)
                    {
                        get_permision=true;
                        was_in_tunnel=true;
                        draw(path.get(i));
                        for (Rectangle x:tunnel) {
                            x.setStroke(Color.ORANGERED);
                        }
                    }
                    else
                    {
                        if(semaphore.availablePermits()==0)
                        {
                            try {
                                Thread.sleep(100);
                                if(i>0)
                                {
                                    i--;
                                }
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
                    draw(path.get(i));
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
                    get_permision=false;

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
    private synchronized void waitOnStation()
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
                while (true)
                {
                        if(free_track[4] && animation_flag)
                        {
                            free_track[4]=false;
                            color_track(track_4,Color.ORANGERED);
                            move();
                            if(ended_move)
                           {

                               free_track[0]=true;
                               color_track(track_0,Color.GREENYELLOW);
                               ended_move=false;
                               waitOnStation();
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
                            move();
                            if(ended_move)
                            {
                                free_track[4]=true;
                                color_track(track_4,Color.GREENYELLOW);
                                ended_move=false;
                                waitOnStation();
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
                while (true)
                {
                    if(free_track[1] && animation_flag)
                    {
                        free_track[1]=false;
                        color_track(track_1,Color.ORANGERED);
                        move();
                        if(ended_move)
                        {
                            free_track[2]=true;
                            color_track(track_2,Color.GREENYELLOW);
                            ended_move=false;
                            waitOnStation();
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
                        move();
                        if(ended_move)
                        {
                            free_track[1]=true;
                            color_track(track_1,Color.GREENYELLOW);
                            ended_move=false;
                            waitOnStation();
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
                while (true)
                {
                    if(free_track[0] && animation_flag)
                    {
                        free_track[0]=false;
                        color_track(track_0,Color.ORANGERED);
                        move();
                        if(ended_move)
                        {
                            free_track[3]=true;
                            color_track(track_3,Color.GREENYELLOW);
                            ended_move=false;
                            waitOnStation();
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
                        move();
                        if(ended_move)
                        {
                            free_track[0]=true;
                            color_track(track_0,Color.GREENYELLOW);
                            ended_move=false;
                            waitOnStation();
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


