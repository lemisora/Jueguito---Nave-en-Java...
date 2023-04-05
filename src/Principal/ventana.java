package Principal;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
public class ventana extends JFrame implements Runnable{
    public static final int ancho=800,alto=600;
    private Canvas canvas;
    private Thread thread;
    private boolean ejecutar=false;
    private BufferStrategy bs;
    private Graphics g;
    private final int FPS=120;
    private double TIEMPOOBJETIVO=1000000000/FPS;
    private double delta=0;
    private int FPSPROMEDIO=FPS;
    public ventana(){
        setTitle("Nave espacial");
        setSize(ancho,alto);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        canvas=new Canvas();
        canvas.setPreferredSize(new Dimension(ancho,alto));
        canvas.setMaximumSize(new Dimension(ancho,alto));
        canvas.setMinimumSize(new Dimension(ancho,alto));
        canvas.setFocusable(true);

        add(canvas);
    }

    public static void main(String [] args){
        new ventana().start();

    }

    int x=0;
    private void actualizar(){
        x++;
    }

    private void dibujar(){
        bs=canvas.getBufferStrategy();
        if(bs==null){
            canvas.createBufferStrategy(3);
            return;
        }
        g=bs.getDrawGraphics();

        g.clearRect(0,0,ancho,alto);
        g.setColor(Color.BLACK);
        g.drawString(""+FPSPROMEDIO,100,100);


        g.dispose();
        bs.show();
    }

    @Override
    public void run(){
        long now=0;
        long lastTime=System.nanoTime();
        int fotogramas=0;
        long time=0;
        while(ejecutar){
            now=System.nanoTime();
            delta+=(now-lastTime)/TIEMPOOBJETIVO;
            lastTime=now;

            if(delta>=1){
                actualizar();
                dibujar();
                delta--;
            }
            if(time>=1000000000){
                FPSPROMEDIO=fotogramas;
                fotogramas=0;
                time=0;
            }
        }
        stop();
    }
    private void start(){
        thread=new Thread(this);
        thread.start();
        ejecutar=true;
    }
    private void stop(){
        try{
            thread.join();
            ejecutar=false;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
