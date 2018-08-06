package brains.lesson_1.hello;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Scanner;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {

    InputStream in;
    OutputStream out;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private static final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Alwayson top");
    private final JTextField tfLogin = new JTextField("Alex");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final JList<String> userList = new JList<>();

    private static String channel = "chat.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });

        print();

    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUsers = new JScrollPane(userList);
        String[] users = {"user1_with_an_exceptionally_long_nickname", "user2", "user3", "user4", "user5", "user6", "user7", "user8", "user9", "user10"};
        userList.setListData(users);
        scrollUsers.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this);
        btnLogin.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addKeyListener(keyLis);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);

        add(panelTop, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        add(scrollUsers, BorderLayout.EAST);
        setVisible(true);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = e.getClass().getCanonicalName() + ": " + e.getMessage() +
                    "\n\t at " + ste[0];
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
//            setAlwaysOnTop(!isAlwaysOnTop());
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else {
     //       throw new RuntimeException("Unknown source: " + src);
        }
        if (src == btnSend){
            send();
        }
    }


    public static void writ(String name, String txt) throws IOException {
        PrintStream File = new PrintStream(new FileOutputStream(name, true));
        File.write(txt.getBytes());
        File.flush();
        File.close();
    }

    public static String read (String name) throws IOException {
        String msg="";
        Scanner txt1 = new Scanner(new FileInputStream(name));
        while (txt1.hasNext()) {
            msg += txt1.nextLine()+"\n";
        }
        return msg;
    }

    public static void print (){
        log.setText("");
        try {
            log.insert(read(channel), 0);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Writing failed!");
        }
    }

    public void send () {
        try {
            writ("chat.txt",  tfLogin.getText() + ": " + tfMessage.getText()+"\n");
        } catch (FileNotFoundException a) {
            System.out.println("File not found!");
        } catch (IOException a) {
            System.out.println("Writing failed!");
        }
        print();
        tfMessage.setText("");
    }

    KeyListener keyLis = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) { }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){send();}
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    };
}

