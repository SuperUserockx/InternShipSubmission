import java.util.*;
import java.io.*;
import java.awt.FlowLayout;  
import javax.swing.JButton;  
import javax.swing.JFrame;  
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Jpanel;
import jdk.jfr.Label;  

static class data{
    static int numFloor = 0;
    static int floor = 0;
    static int state = 0;
    static int statePrv = 1;
    static boolean isMoving = false;
    static List<Integer> up;
    static List<Integer> down;
    static boolean prvMove = false;
}

class ProcessThread extends Thread {

    void check(){
        if(!data.up.isEmpty()){
            if(data.floor == data.up.get(0)){
                data.up.remove(0);
                data.state = 0;
                data.statePrv = 1;
                data.isMoving = false;
            }
        }
        if(!data.down.isEmpty()){
            if(data.floor == data.down.get(0)){
                data.down.remove(0);
                data.state = 0;
                data.statePrv = -1;
                data.isMoving = false;
            }
        }
    }

    void update(){
        if(!data.up.isEmpty() && data.isMoving==false){
            if(data.floor<data.up.get(0))
                data.state = 1;
            else if(data.floor>data.up.get(0))
                data.state = -1;
        }
        else if(!data.down.isEmpty() && data.isMoving==false){
            if(data.floor<data.down.get(0))
                data.state = 1;
            else if(data.floor>data.down.get(0))
                data.state = -1;

        }        
        else if(data.isMoving == false)
            data.state = 0;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame("Lift Status");  
        JPanel panel = new JPanel();  
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        frame.setSize(400, 100);  
        frame.setLocationRelativeTo(null);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setVisible(true);  
        panel.setLayout(new FlowLayout());  
        panel.add(label1);
        panel.add(label2);  
        panel.add(label3);  
        frame.add(panel); 
        while(true) {
            frame.remove(label1);
            frame.remove(label2);
            frame.remove(label3);
            Collections.sort(data.up);
            Collections.sort(data.down,Collections.reverseOrder());
            check();
            if(data.state == -1){
                data.isMoving = true;
                data.prvMove = true;
                if(data.floor>0){
                    label1.setText("Currently on floor " + data.floor);
                    label2.setText("Going Down");
                    label3.setText("");
                data.floor--;
                }
                if(!data.up.isEmpty()){
                    if(data.floor == data.up.get(0))
                        data.isMoving = false;
                }
                if(!data.down.isEmpty()){
                    if(data.floor == data.down.get(0))
                        data.isMoving = false;
                }
            }
            else if(data.state == 1){
                data.isMoving = true;
                data.prvMove = true;
                if(data.floor<data.numFloor){
                    label1.setText("Currently on floor " + data.floor);
                    label2.setText("Going up");
                    label3.setText("");
                    data.floor++;
                }
                if(!data.up.isEmpty()){
                    if(data.floor == data.up.get(0))
                        data.isMoving = false;
                }
                if(!data.down.isEmpty()){
                    if(data.floor == data.down.get(0))
                        data.isMoving = false;
                }
            }
            else{
                 data.isMoving = false;
                label1.setText("Currently on floor " + data.floor);
                label2.setText("Lift Open");
                if(data.statePrv == 1){
                    label3.setText("Going Up");
                }
                else if(data.statePrv == -1){
                    label3.setText("Going Down");
                }
                else if(data.floor == data.numFloor){
                    label3.setText("Going Down");
                }
                else if(data.floor == 0){
                    label3.setText("Going Up");
                }
                if(data.prvMove == true){
                    try { this.sleep(1000); } catch(InterruptedException ie) {}
                    data.prvMove = false;
                }
            }
            update();
            try { this.sleep(1000); } catch(InterruptedException ie) {}
          }
    }
}


public class liftControl {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProcessThread t = new ProcessThread();
        data.up = new ArrayList<Integer>();
        data.down = new ArrayList<Integer>();
        System.out.print("Input the total number of floors : ");
        data.numFloor = Integer.parseInt(sc.nextLine());
        t.start();
        while(true) {
                System.out.print("Enter user's floor number : ");
                int f = -1;
                try{
                    f = Integer.parseInt(sc.nextLine());
                }catch(Exception e){}
                if(f >= 0  && f <= data.numFloor){
                    System.out.print("Select opton u for UP d for Down : ");
                    String dir = sc.nextLine();
                    if(dir.equals("u")){
                        
                        if(f>(data.numFloor-1)){
                            System.out.println("Invald input");
                        }
                        else
                            data.up.add(f);
                    }
                    else if(dir.equals("d")){
                        if(f<1){
                            System.out.println("Invald input");
                        }
                        else
                            data.down.add(f);
                    }
                    else{
                        System.out.println("Invalid input");
                    }
                }
                else{
                    System.out.println("Invalid input");
                }
            System.out.println();
        }
    }
}

