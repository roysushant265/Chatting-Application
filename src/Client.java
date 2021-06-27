import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {

    JPanel p1;
    JTextField t1;
    JButton b1;
    static JPanel a1;
    static JFrame f1 = new JFrame();

    static Box vertical = Box.createVerticalBox();// Creates a Box that displays its components from top to bottom


    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;

    Boolean typing;

    Client(){
        f1.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        f1.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);
        l1.setBounds(5, 17, 30, 30);
        p1.add(l1);

        l1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent ae){
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/2.png"));
        Image i5 = i4.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l2 = new JLabel(i6);
        l2.setBounds(40, 5, 60, 60);
        p1.add(l2);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel l5 = new JLabel(i9);
        l5.setBounds(290, 20, 30, 30);
        p1.add(l5);

        ImageIcon i11 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i12 = i11.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i13 = new ImageIcon(i12);
        JLabel l6 = new JLabel(i13);
        l6.setBounds(350, 20, 35, 30);
        p1.add(l6);

        ImageIcon i14 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i15 = i14.getImage().getScaledInstance(13, 25, Image.SCALE_DEFAULT);
        ImageIcon i16 = new ImageIcon(i15);
        JLabel l7 = new JLabel(i16);
        l7.setBounds(410, 20, 13, 25);
        p1.add(l7);


        JLabel l3 = new JLabel("Bill Gates");
        l3.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        l3.setForeground(Color.WHITE);
        l3.setBounds(110, 15, 100, 18);
        p1.add(l3);


        JLabel l4 = new JLabel("Active Now");
        l4.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        l4.setForeground(Color.WHITE);
        l4.setBounds(110, 35, 100, 20);
        p1.add(l4);

        Timer t = new Timer(1, new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                if(!typing){
                    l4.setText("Active Now");
                }
            }
        });

        t.setInitialDelay(2000);

        a1 = new JPanel();
//        a1.setBounds(5, 75, 440, 570);
        a1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
//        f1.add(a1);

        JScrollPane sp = new JScrollPane(a1);
        sp.setBorder(BorderFactory.createEmptyBorder());// to remove the default border that we get from adding scroll bar
        sp.setBounds(5, 75, 440, 570);
        ScrollBarUI ui = new BasicScrollBarUI(){ // basic scrollBarUi is child class of scrollBarUi
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(new Color(7,94,84));
                button.setForeground(Color.white);
                this.thumbColor = new Color(7,94,84); // to change color of thumb(long bar in the scroll pane which we slide)
                return button;
            }
            protected JButton createIncreaseButton(int orientation) {
                JButton button = super.createDecreaseButton(orientation);
                button.setBackground(new Color(7,94,84));
                button.setForeground(Color.white);
                this.thumbColor = new Color(7,94,84);
                return button;
            }
        };
        sp.getVerticalScrollBar().setUI(ui);// to add ui to scroll bar
        f1.add(sp);

        t1 = new JTextField();
        t1.setBounds(5, 655, 310, 40);
        t1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f1.add(t1);

        t1.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                l4.setText("typing...");

                t.stop();

                typing = true;
            }

            public void keyReleased(KeyEvent ke){
                typing = false;

                if(!t.isRunning()){
                    t.start();
                }
            }
        });

        b1 = new JButton("Send");
        b1.setBounds(320, 655, 123, 40);
        b1.setBackground(new Color(7, 94, 84));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        b1.addActionListener(this);
        f1.add(b1);

        f1.getContentPane().setBackground(Color.WHITE);
        f1.setLayout(null);
        f1.setSize(450, 700);
        f1.setLocation(800, 0);
        f1.setUndecorated(true);
        f1.setVisible(true);

    }
    public void sendTextToFile(String message)throws FileNotFoundException {
        try(FileWriter fw = new FileWriter("chat.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);){
                bw.write("Bill Gates: "+message);
                bw.newLine();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae){
        try{
            String out = t1.getText();
            sendTextToFile(out);
            JPanel p2 = formatLabel(out);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout()); // new panel created to print messages in right
            right.add(p2, BorderLayout.LINE_END);// append the panel of text at the end of border(right side)
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));// for space between vertical structures

            a1.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);
            t1.setText("");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static JPanel formatLabel(String out){
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS)); // label added in panel adds along y axis

        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">"+out+"</p></html>"); // beak the text in new line
        l1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        l1.setBackground(new Color(37, 211, 102));
        l1.setOpaque(true);// set the whole color of label
        l1.setBorder(new EmptyBorder(15,15,15,50));// For Padding

        Calendar cal = Calendar.getInstance(); // to display time under the message
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel l2 = new JLabel();
        l2.setText(sdf.format(cal.getTime()));// to get the time

        p3.add(l1); // add the text label in the panel
        p3.add(l2); // add time label vertically down from text label
        return p3; // return the panel
    }

    public static void main(String[] args){
        new Client().f1.setVisible(true);

        try{

            s = new Socket("127.0.0.1", 6001);// here both IP address and same port is required
            din  = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());

            String msginput = "";

            while(true){
                a1.setLayout(new BorderLayout());
                msginput = din.readUTF();
                JPanel p2 = formatLabel(msginput);
                JPanel left = new JPanel(new BorderLayout());
                left.add(p2, BorderLayout.LINE_START);// add to the left

                vertical.add(left);// add the text panel in the box vertically
                vertical.add(Box.createVerticalStrut(15));// to give space between vertical messages
                a1.add(vertical, BorderLayout.PAGE_START);// then the vertical box is added to the main panel
                f1.validate();
            }

        }catch(Exception e){}
    }
}
