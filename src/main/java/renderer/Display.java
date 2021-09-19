package renderer;

import renderer.IO.Click;
import renderer.IO.MouseAction;
import renderer.point.PkgPoint;
import renderer.point.PointConverter;
import renderer.shapes.PkgPolygon;
import renderer.shapes.Tetrahedron;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferStrategy;

//Look this up - Runnable (interface)
public class Display extends Canvas implements Runnable{

    private Thread thread;
    public JFrame frame;
    public static String title = "3D Renderer";
    //Look this up - static and final
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 640;
    private static boolean running = false;

    private Tetrahedron tetra;

    private MouseAction mouse;

    public Display(){
        this.frame = new JFrame();

        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.mouse = new MouseAction();
        this.addMouseListener(this.mouse);
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }
    //Look this up - synchronized
    public synchronized void start(){
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }

    public synchronized void stop(){
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0/60;
        double delta = 0;
        int fps = 0;

        init();

        long now;
        while (running){
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                update();
                delta --;
                render();
                fps ++;
            }
            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                this.frame.setTitle(title + " FPS: " + fps);
                fps = 0;
            }
        }

        stop();
    }

    private void init(){
        int s = 300;
        PkgPoint c1 = new PkgPoint(s/2, -s/2, -s/2);
        PkgPoint c2 = new PkgPoint(s/2, s/2, -s/2);
        PkgPoint c3 = new PkgPoint(s/2, s/2, s/2);
        PkgPoint c4 = new PkgPoint(s/2, -s/2, s/2);
        PkgPoint c5 = new PkgPoint(-s/2, -s/2, -s/2);
        PkgPoint c6 = new PkgPoint(-s/2, s/2, -s/2);
        PkgPoint c7 = new PkgPoint(-s/2, s/2, s/2);
        PkgPoint c8 = new PkgPoint(-s/2, -s/2, s/2);

        this.tetra = new Tetrahedron(
            new PkgPolygon(Color.RED, c1, c2, c3, c4),
            new PkgPolygon(Color.BLUE, c1, c4, c8, c5),
            new PkgPolygon(Color.WHITE, c3, c4, c8, c7),
            new PkgPolygon(Color.YELLOW, c2, c3, c7, c6),
            new PkgPolygon(Color.GREEN, c1, c2, c6, c5),
            new PkgPolygon(Color.ORANGE, c5, c6, c7, c8)
            );

        this.tetra.rotate(true, 0, 0,0);
    }

    private Click prevMouseAction = Click.UNKNOWN;
    private int initX, initY;
    private double xSpeed, ySpeed;
    private double speedDecay = 0.03;

    private void update(){
        int x = this.mouse.getMouseX();
        int y = this.mouse.getMouseY();
        double sensitivity = 4;

        if(this.mouse.getMouseB() == Click.MOUSE_LEFT){
            prevMouseAction = Click.MOUSE_LEFT;
            int xDif = x - initX;
            int yDif = y - initY;
            xSpeed = xDif;
            ySpeed = yDif;
//            System.out.println(yDif + " " + xDif);
            this.tetra.rotate(true, 0, -yDif/sensitivity, -xDif/sensitivity);
        } else if(this.mouse.getMouseB() == Click.MOUSE_RIGHT){
            prevMouseAction = Click.MOUSE_RIGHT;
            int xDif = x - initX;
            xSpeed = xDif;
            this.tetra.rotate(true, -xDif/sensitivity, 0,0);
        } else if (prevMouseAction == Click.MOUSE_LEFT){
            ySpeed *= (1 - speedDecay);
            xSpeed *= (1 - speedDecay);
            this.tetra.rotate(true, 0, -ySpeed/sensitivity, -xSpeed/sensitivity);
        } else if (prevMouseAction == Click.MOUSE_RIGHT){
            xSpeed *= (1 - speedDecay);
            this.tetra.rotate(true,-xSpeed/sensitivity, 0, 0);
        }

        if(this.mouse.isScrollingUp()){
            PointConverter.zoomOut();
        } else if (this.mouse.isScrollingDown()){
            PointConverter.zoomIn();
        }

        this.mouse.resetMouseWheel();
        initX = x;
        initY = y;
    }

    private boolean movingLeft = false;
    private boolean movingUp = false;

    private int xPos = 0;
    private int yPos = 0;

    private int figureHeight = HEIGHT/8;
    private int figureWidth = WIDTH/8;

    private void render(){
        //Look this up
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            //Look this up
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, WIDTH, HEIGHT);

//        calculateMovement(g);
        tetra.render(g);

        g.dispose();
        bs.show();
    }

    private void calculateMovement(Graphics g){
        int speed = 1;
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, figureWidth, figureHeight);
        if(xPos >= WIDTH - figureWidth){
            //Crashed in right wall
            movingLeft = true;
        }else if(xPos <= 0){
            //Crashed in left wall
            movingLeft = false;
        }else if(yPos >= HEIGHT - figureHeight){
            //Crashed in lower wall
            movingUp = true;
        }else if(yPos <= 0){
            //Crashed in upper wall
            movingUp = false;
        }
        if(movingLeft){
            xPos = xPos - speed;
        }else{
            xPos = xPos + speed;
        }
        if(movingUp) {
            yPos = yPos - speed;
        } else {
            yPos = yPos + speed;
        }

    }


}
