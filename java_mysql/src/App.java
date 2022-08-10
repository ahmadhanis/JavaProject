import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.sql.*;
import java.util.ArrayList;

public class App {

    JTextField jtf_foodname, jtf_foodprice;
    JTextArea jta_fooddesc;
    JTable jt;
    JFrame frame;
    ArrayList<Food> foodlist;
    Food food;
    String header[] = new String[] { "ID", "Food Name", "Food Price", "Food Description" };
    DefaultTableModel dtm = new DefaultTableModel(0, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    static Connection con = null;
    ResultSet rs;
    int row, col;

    public static void main(String[] args) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_db", "root", "");

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        App app = new App();
        app.mainInterface();
        app.loadData();
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
        jb_add.addActionListener(addFoodListener);

        JButton jb_delete = new JButton();
        jb_delete.setText("Delete");
        jb_delete.setBounds(120, 140, 100, 25);
        frame.add(jb_delete);
        jb_delete.addActionListener(delFoodListener);

        JButton jb_update = new JButton();
        jb_update.setText("Update");
        jb_update.setBounds(230, 140, 100, 25);
        frame.add(jb_update);
        jb_update.addActionListener(updateFoodListener);

        JButton jb_search = new JButton();
        jb_search.setText("Search");
        jb_search.setBounds(340, 140, 100, 25);
        frame.add(jb_search);
        jb_search.addActionListener(searchFoodListener);

        jt = new JTable();
        jt.setModel(dtm);
        dtm.setColumnIdentifiers(header);
        JScrollPane sp = new JScrollPane(jt);
        sp.setBounds(10, 170, 430, 600);
        frame.add(sp);
        jt.addMouseListener(mouseListener);

        frame.setSize(480, 800);
        frame.setLayout(null);// using no layout managers
        frame.setVisible(true);// making the frame visible
    }

    private void loadData() {
        foodlist = new ArrayList<>();
        try {

            Statement stmt = con.createStatement();
            rs = stmt.executeQuery("select * from tbl_foods");
            foodlist.clear();
            while (rs.next()) {
                foodlist.add(new Food(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4)));
            }
            dtm.setRowCount(0);// reset data model
            for (int i = 0; i < foodlist.size(); i++) {
                Object[] objs = { foodlist.get(i).foodid, foodlist.get(i).foodname, foodlist.get(i).foodprice,
                        foodlist.get(i).fooddesc, };
                dtm.addRow(objs);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    ActionListener searchFoodListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            String search = JOptionPane.showInputDialog("Enter food name");
            System.out.println(search);

            foodlist = new ArrayList<>();
            try {

                Statement stmt = con.createStatement();
                rs = stmt.executeQuery("select * from tbl_foods where food_name LIKE '%" + search + "%'");
                foodlist.clear();
                while (rs.next()) {
                    foodlist.add(new Food(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getString(4)));
                }
                dtm.setRowCount(0);// reset data model
                for (int i = 0; i < foodlist.size(); i++) {
                    Object[] objs = { foodlist.get(i).foodid, foodlist.get(i).foodname, foodlist.get(i).foodprice,
                            foodlist.get(i).fooddesc, };
                    dtm.addRow(objs);
                }

            } catch (Exception err) {
                System.out.println(err);
            }
        }

    };

    MouseInputAdapter mouseListener = new MouseInputAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = jt.rowAtPoint(evt.getPoint());
            int col = jt.columnAtPoint(evt.getPoint());
            if (row >= 0 && col >= 0) {
                jtf_foodname.setText(jt.getValueAt(row, 1).toString());
                jtf_foodprice.setText(jt.getValueAt(row, 2).toString());
                jta_fooddesc.setText(jt.getValueAt(row, 3).toString());
                food = new Food(Integer.parseInt(jt.getValueAt(row, 0).toString()), jt.getValueAt(row, 1).toString(),
                        Double.parseDouble(jt.getValueAt(row, 0).toString()), jt.getValueAt(row, 0).toString());
            }
        }
    };

    ActionListener updateFoodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String foodname = jtf_foodname.getText().toString();
            String foodprice = jtf_foodprice.getText().toString();
            String fooddesc = jta_fooddesc.getText().toString();
            if (food == null) {
                System.out.println("Null");
            } else {

                int result = JOptionPane.showConfirmDialog(frame, "Update " + food.foodname + "?", "Swing Tester",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        System.out.println("Food " + food.foodname);
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate("update tbl_foods set food_name = '" + foodname + "', food_price = "
                                + foodprice + ", food_desc='" + fooddesc + "' where id =" + food.foodid + "");
                        loadData();
                    } catch (Exception err) {
                        System.out.println(err);
                    }
                }

            }
        }
    };
    ActionListener addFoodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String foodname = jtf_foodname.getText().toString();
            String foodprice = jtf_foodprice.getText().toString();
            String fooddesc = jta_fooddesc.getText().toString();
            if (foodname.isEmpty() || foodprice.isEmpty() || fooddesc.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter food info");
                jtf_foodname.requestFocus();
            } else {
                try {
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate("insert into tbl_foods (`food_name`, `food_price`, `food_desc`) VALUES ('"
                            + foodname + "','" + foodprice + "','" + fooddesc + "')");
                    loadData();
                } catch (Exception err) {
                    System.out.println(err);
                }
            }
        }
    };

    ActionListener delFoodListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (food == null) {
                System.out.println("Null");
            } else {

                int result = JOptionPane.showConfirmDialog(frame, "Delete " + food.foodname + "?", "Swing Tester",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        System.out.println("Food " + food.foodname);
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate("delete from tbl_foods where id = '" + food.foodid + "'");
                        loadData();
                    } catch (Exception err) {
                        System.out.println(err);
                    }
                }

            }

        }
    };

}
