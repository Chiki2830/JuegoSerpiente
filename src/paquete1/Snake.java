package paquete1;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Snake  extends JFrame{

    int width = 640;
    int height = 480;

    Point snake;
    Point comida;

    boolean gameOver = false;

    ArrayList<Point> lista = new ArrayList<Point>();

    int longitud = 2;
    int widthPoint = 10;
    int heightPoint = 10;

    String direccion = "RIGHT";

    long frecuencia = 36;

    ImagenSnake imagenSnake;

    public Snake(){
        setTitle("Snake");
        setSize(width,height);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-width/2, dim.height/2-height/2);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       

        startGame();

        this.addKeyListener(new Teclas());
        JFrame.setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);


        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);

        setVisible(true);
        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();

    }

    public void startGame(){
        comida = new Point(200, 100);
        snake = new Point(320, 240);

        lista = new ArrayList<Point>();
        lista.add(snake);

        
       longitud = lista.size();
    }

    public void crearComida(){
        Random rnd = new Random();

    comida.x = rnd.nextInt(width-10)+5;
    comida.y = rnd.nextInt(height-10)+5;

    // asegurarse de que la comida esté dentro de los límites de la ventana
    comida.x = Math.max(5, Math.min(comida.x, width-10));
    comida.y = Math.max(5, Math.min(comida.y, height-10));

        
        if ((comida.x % 5)>0) {
            comida.x = comida.x - (comida.x % 5);
        }
        if (comida.x < 5) {
            comida.x = comida.x +10;
        }
        if (comida.x > width) {
            comida.x = comida.x -10;
        }
      
        if ((comida.y % 5)>0) {
            comida.y = comida.y - (comida.y%5);
        }
        if (comida.y > height) {
            comida.y = comida.y -10;
        }
        if (comida.y <0) {
            comida.y = comida.y +10;
        }

    }

    public static void main(String[] args) throws Exception {
       Snake s = new Snake();
    }

    public void actualizar(){
        

        lista.add(0, new Point(snake.x, snake.y));
        lista.remove((lista.size()-1));

        for (int i = 1; i<lista.size() ; i++) {
            Point punto = lista.get(i);
            if (snake.x == punto.x && snake.y == punto.y) {
                gameOver = true;
            }
        }

        if ((snake.x >(comida.x-10) && snake.x <(comida.x+10)) 
        &&(snake.y >(comida.y-10)&& snake.y <(comida.y+10))) {
            lista.add(0, new Point(snake.x, snake.y));
            crearComida();    
        }
        imagenSnake.repaint();
    }

    public class ImagenSnake extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            if(gameOver) {
                g.setColor(new Color(0,0,0));
            } else {
                g.setColor(new Color(0,0,0));
            }

            g.fillRect(0,0, width, height);
            g.setColor(new Color(73,255,0));
            
           
            if(lista.size() > 0) {
                for(int i=0;i<lista.size();i++) {
                    Point p = (Point)lista.get(i);
                    g.fillRect(p.x,p.y,widthPoint,heightPoint);
                }
            }
            

            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

            if (gameOver) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                int x = (getWidth() - metrics.stringWidth("Perdiste")) / 2;
                int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                g.drawString("Perdiste", x, y);
                x = (getWidth() - metrics.stringWidth("Record: " + (lista.size() - 1))) / 2;
                y = y + metrics.getHeight();
                g.drawString("Record: " + (lista.size() - 1), x, y);

                g.setFont(new Font("Open", Font.BOLD, 20));
                metrics = g.getFontMetrics(g.getFont());
                x = (getWidth() - metrics.stringWidth("Presiona R para jugar de nuevo")) / 2;
                y = y + metrics.getHeight();
                g.drawString("Presiona R para jugar de nuevo", x, y);
                x = (getWidth() - metrics.stringWidth("Presiona Esc para salir del juego")) / 2;
                y = y + metrics.getHeight();
                g.drawString("Presiona Esc para salir del juego", x, y);
            }

              

        }
    }

    public class Teclas extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }else if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (direccion != "DOWN") {
                    direccion = "UP";
                }
            }else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (direccion != "UP") {
                    direccion = "DOWN";
                }
            }else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (direccion != "RIGHT") {
                    direccion ="LEFT";
                }
            }else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (direccion != "LEFT") {
                    direccion = "RIGHT";
            }
                
            }else if(e.getKeyCode() == KeyEvent.VK_R) {
                gameOver = false;
                startGame();				
			}
        }
    }
    public class Momento extends Thread{
        private long last = 0;
        public Momento(){

        }
        public void run(){
            while(true){
                if ((java.lang.System.currentTimeMillis() - last ) > frecuencia) {
                    if (!gameOver) {
                    
                    if (direccion == "UP") {
                        snake.y = snake.y - heightPoint;
                        if (snake.y > height) {
                            snake.y = 0;
                        }
                        if (snake.y < 0) {
                            snake.y = height - heightPoint;
                        }
                    }else if (direccion == "DOWN") {
                        snake.y = snake.y + heightPoint;
                        if (snake.y > height) {
                            snake.y = 0;
                        }
                        if (snake.y < 0) {
                            snake.y = height - heightPoint;
                        }
                    }else if (direccion == "LEFT") {
                        snake.x = snake.x - widthPoint;
                        if (snake.x > width) {
                            snake.x = 0;
                        }
                        if (snake.x < 0) {
                            snake.x = width - widthPoint;
                        }
                    }else if (direccion == "RIGHT") {
                        snake.x = snake.x+ widthPoint;
                        if (snake.x > width) {
                            snake.x = 0;
                        }
                       
                      
                    }    
                }
                    
                    actualizar();
                    last = java.lang.System.currentTimeMillis();
                }
            }
        }
    }
}
