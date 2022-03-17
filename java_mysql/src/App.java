import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
//import java.util.ArrayList;
import java.sql.*;
import java.util.ArrayList;

public class App {

    JTextField jtf_foodname, jtf_foodprice;
    JTextArea jta_fooddesc;
    JFrame frame;
    ArrayList<Food> foodlist;
    String header[] = new String[] { "Food Name", "Food Price", "Food Description" };

    int row, col;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.loadData();
        app.mainInterface();
    }

    private void loadData() {
        foodlist = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/food_db", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from tbl_foods");
            foodlist.clear();
            while (rs.next()){
                foodlist.add(new Food(rs.getInt(1),rs.getString(2),rs.getFloat(3),rs.getString(4)));
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getFloat(3)+ "  " + rs.getString(4));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void mainInterface() {
        frame = new JFrame();
        JLabel lbl_foodname = new JLabel();
        lbl_foodname.setText("Food Name");
        lbl_foodname.setBounds(10, 10, 100, 50);
        frame.add(lbl_foodname);

        jtf_foodname = new JTextField();
        jtf_foodname.setBounds(100, 25, 250, 25);
        frame.add(jtf_foodname);

        JLabel lbl_foodprice = new JLabel();
        lbl_foodprice.setText("Price");
        lbl_foodprice.setBounds(10, 35, 100, 50);
        frame.add(lbl_foodprice);

        jtf_foodprice = new JTextField();
        jtf_foodprice.setBounds(100, 50, 100, 25);
        frame.add(jtf_foodprice);

        JLabel lbl_fooddesc = new JLabel();
        lbl_fooddesc.setText("Description");
        lbl_fooddesc.setBounds(10, 55, 100, 50);
        frame.add(lbl_fooddesc);

        jta_fooddesc = new JTextArea();
        jta_fooddesc.setBounds(100, 75, 250, 50);
        jta_fooddesc.setBorder(new JTextField().getBorder());
        frame.add(jta_fooddesc);

        JButton jb_add = new JButton();
        jb_add.setText("Add");
        jb_add.setBounds(10, 140, 100, 25);
        frame.add(jb_add);

        JButton jb_delete = new JButton();
        jb_delete.setText("Delete");
        jb_delete.setBounds(120, 140, 100, 25);
        frame.add(jb_delete);

        JButton jb_update = new JButton();
        jb_update.setText("Update");
        jb_update.setBounds(230, 140, 100, 25);
        frame.add(jb_update);

        JButton jb_search = new JButton();
        jb_search.setText("Search");
        jb_search.setBounds(340, 140, 100, 25);
        frame.add(jb_search);

        // JTable jt = new JTable();
        // jt.setModel(dtm);
        // JScrollPane sp = new JScrollPane(jt);
        // sp.setBounds(10, 170, 430, 600);
        // frame.add(sp);

        jb_add.addActionListener(addFoodListener);
        frame.setSize(480, 800);
        frame.setLayout(null);// using no layout managers
        frame.setVisible(true);// making the frame visible
    }

    ActionListener addFoodListener=new ActionListener(){@Override public void actionPerformed(ActionEvent e){String foodname=jtf_foodname.getText().toString();String foodprice=jtf_foodprice.getText().toString();String fooddesc=jta_fooddesc.getText().toString();if(foodname.isEmpty()||foodprice.isEmpty()||fooddesc.isEmpty()){JOptionPane.showMessageDialog(frame,"Please enter food info");jtf_foodname.requestFocus();}else{

    }}};

    // private void loadData() {
    // try {
    // File file = new File("data.txt"); // create file
    // file.createNewFile();// if not exit
    // FileReader f = new FileReader("data.txt");
    // StringBuffer sb = new StringBuffer();
    // while (f.ready()) {
    // char c = (char) f.read();
    // if (c == '-') {
    // String foodarray[] = sb.toString().split(",");
    // // System.out.println(foodarray[2]);
    // foodlist.add(new Food(foodarray[0], Double.parseDouble(foodarray[1]),
    // foodarray[2]));
    // sb = new StringBuffer();
    // } else {
    // sb.append(c);
    // }
    // }
    // f.close();
    // } catch (IOException e) {
    // return;
    // }
    // dtm.setRowCount(0);// reset data model
    // for (int i = 0; i < foodlist.size(); i++) {
    // Object[] objs = { foodlist.get(i).foodname, foodlist.get(i).foodprice,
    // foodlist.get(i).fooddesc, };
    // dtm.addRow(objs);
    // }

    // }

    // private void writeData() {
    // try (FileWriter f = new FileWriter("data.txt")) {
    // StringBuilder sb = new StringBuilder();
    // for (int i = 0; i < foodlist.size(); i++) {
    // sb.append(foodlist.get(i).foodname + "," + foodlist.get(i).foodprice + "," +
    // foodlist.get(i).fooddesc
    // + "-");
    // }
    // f.write(sb.toString());
    // f.close();
    // } catch (IOException e) {
    // return;
    // }
    // dtm.setRowCount(0);// reset data model
    // for (int i = 0; i < foodlist.size(); i++) {
    // Object[] objs = { foodlist.get(i).foodname, foodlist.get(i).foodprice,
    // foodlist.get(i).fooddesc };
    // dtm.addRow(objs);
    // }
    // }
}
