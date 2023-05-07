import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.portable.OutputStream;

public class ClientGUI extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldStudentID;
    private JTextField textFieldDest1;
    private JTextField textFieldDest2;
    private JTextField textFieldDest3;
    private JTextField textFieldDest4;
    private JTextField textFieldDest5;
    private JButton btnSubmit;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientGUI frame = new ClientGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ClientGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Load the image in a separate thread to avoid blocking the EDT
        new Thread(new Runnable() {
            public void run() {
                try {
                    Image img = ImageIO.read(getClass().getResource("image.jpg"));
                    JLabel background = new JLabel(new ImageIcon(img));
                    contentPane.add(background);
                    contentPane.setOpaque(false);
                    contentPane.repaint();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Add the components to the content pane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridLayout(6, 1));
        setContentPane(contentPane);

        JLabel lblStudentId = new JLabel("Student ID:");
        contentPane.add(lblStudentId);

        JLabel lblDestinations = new JLabel("Destinations:");
        contentPane.add(lblDestinations);

        textFieldStudentID = new JTextField();
        textFieldStudentID.setPreferredSize(new Dimension(150, 25));
        contentPane.add(textFieldStudentID);

        textFieldDest1 = new JTextField();
        contentPane.add(textFieldDest1);

        textFieldDest2 = new JTextField();
        contentPane.add(textFieldDest2);

        textFieldDest3 = new JTextField();
        contentPane.add(textFieldDest3);

        textFieldDest4 = new JTextField();
        contentPane.add(textFieldDest4);

        textFieldDest5 = new JTextField();
        contentPane.add(textFieldDest5);

        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e -> {
            // Get student data from input fields
            String studentID = textFieldStudentID.getText();
            String[] destinations = new String[] {
                    textFieldDest1.getText(),
                    textFieldDest2.getText(),
                    textFieldDest3.getText(),
                    textFieldDest4.getText(),
                    textFieldDest5.getText()
            };

            // Create JSON object with student data
            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("student_id", studentID);
            jsonData.put("destinations", new JSONArray(Arrays.asList(destinations)));

            // Send student data to server
            // Send student data to server
try {
    URL url = new URL("http://127.0.0.1/api/students");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setDoOutput(true);

    OutputStream os = (OutputStream) conn.getOutputStream();
    os.write(new JSONObject(jsonData).toString().getBytes());
    os.flush();

    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
        throw new RuntimeException("Failed : HTTP error code : "
                + conn.getResponseCode());
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));

    String output;
    System.out.println("Output from Server .... \n");
    while ((output = br.readLine()) != null) {
        System.out.println(output);
    }

    conn.disconnect();

} catch (MalformedURLException ex) {
    ex.printStackTrace();
} catch (IOException ex) {
    ex.printStackTrace();
}
        });

        contentPane.add(btnSubmit);
    }

}
