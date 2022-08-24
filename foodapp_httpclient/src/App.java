import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    JTextField jtf_foodname, jtf_foodprice;
    JTextArea jta_fooddesc;
    JButton jb_add, jb_delete, jb_update, jb_search;
    JTable jt;
    JFrame frame;
    JLabel lbl_foodname, lbl_foodprice, lbl_fooddesc;
    ArrayList<Food> foodlist;
    Food food;
    String header[] = new String[] {
            "ID",
            "Food Name",
            "Food Price",
            "Food Description"
    };
    DefaultTableModel dtm = new DefaultTableModel(0, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    int row, col;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.mainInterface();
        app.loadData();
    }

    private void mainInterface() {
        frame = new JFrame();
        lbl_foodname = new JLabel();
        lbl_foodname.setText("Food Name");
        lbl_foodname.setBounds(10, 10, 100, 50);
        frame.add(lbl_foodname);

        jtf_foodname = new JTextField();
        jtf_foodname.setBounds(100, 25, 250, 25);
        frame.add(jtf_foodname);

        lbl_foodprice = new JLabel();
        lbl_foodprice.setText("Price");
        lbl_foodprice.setBounds(10, 35, 100, 50);
        frame.add(lbl_foodprice);

        jtf_foodprice = new JTextField();
        jtf_foodprice.setBounds(100, 50, 100, 25);
        frame.add(jtf_foodprice);

        lbl_fooddesc = new JLabel();
        lbl_fooddesc.setText("Description");
        lbl_fooddesc.setBounds(10, 55, 100, 50);
        frame.add(lbl_fooddesc);

        jta_fooddesc = new JTextArea();
        jta_fooddesc.setBounds(100, 75, 250, 50);
        jta_fooddesc.setBorder(new JTextField().getBorder());
        frame.add(jta_fooddesc);

        jb_add = new JButton();
        jb_add.setText("Add");
        jb_add.setBounds(10, 140, 100, 25);
        frame.add(jb_add);
        jb_add.addActionListener(addFoodListener);

        jb_delete = new JButton();
        jb_delete.setText("Delete");
        jb_delete.setBounds(120, 140, 100, 25);
        frame.add(jb_delete);
        jb_delete.addActionListener(delFoodListener);

        jb_update = new JButton();
        jb_update.setText("Update");
        jb_update.setBounds(230, 140, 100, 25);
        frame.add(jb_update);
        jb_update.addActionListener(updateFoodListener);

        jb_search = new JButton();
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
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
    }

    private void loadData() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost/foodapp/load_foods.php"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());

        JSONObject obj = new JSONObject(response.body());
        String status = obj.getString("status");

        if (status.equals("success")) {
            foodlist = new ArrayList<>();
            foodlist.clear();
            JSONObject dataobj = obj.getJSONObject("data");
            JSONArray arr = dataobj.getJSONArray("foods");
            for (int i = 0; i < arr.length(); i++) {
                int id = arr.getJSONObject(i).getInt("id");
                String foodname = arr.getJSONObject(i).getString("food_name");
                double foodprice = arr.getJSONObject(i).getDouble("food_price");
                String fooddesc = arr.getJSONObject(i).getString("food_desc");
                foodlist.add(new Food(id, foodname, foodprice, fooddesc));
            }
            dtm.setRowCount(0); // reset data model
            for (int i = 0; i < foodlist.size(); i++) {
                Object[] objs = {
                        foodlist.get(i).foodid,
                        foodlist.get(i).foodname,
                        foodlist.get(i).foodprice,
                        foodlist.get(i).fooddesc,
                };
                dtm.addRow(objs);
            }
        }
    }

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
                        deleteFood(food.foodid);
                        
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
                int result = JOptionPane.showConfirmDialog(frame, "Insert this food data " + foodname + "?", "Insert",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        insertFood(foodname, foodprice, fooddesc);
                    } catch (Exception err) {
                        System.out.println(err);
                    }
                }
            }
        }
    };

    ActionListener updateFoodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int id = food.foodid;
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
                        updatefood(id, foodname, foodprice, fooddesc);
                    } catch (Exception err) {
                        System.out.println(err);
                    }
                }

            }
        }
    };

    ActionListener searchFoodListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {

            String search = JOptionPane.showInputDialog("Enter food name");
            try {
                searchFood(search);
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    };

    protected void insertFood(String foodname, String foodprice, String fooddesc)
            throws IOException, InterruptedException {
        Map<Object, Object> data = new HashMap<>();
        data.put("name", foodname);
        data.put("price", foodprice);
        data.put("desc", fooddesc);
        data.put("ts", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost/foodapp/insert_food.php"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject obj = new JSONObject(response.body());
        String status = obj.getString("status");
        if (status.equals("success")) {
            JOptionPane.showMessageDialog(frame, "Success");
            loadData();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed");
        }

    }

    protected void deleteFood(int foodid) throws IOException, InterruptedException {
        Map<Object, Object> data = new HashMap<>();
        data.put("id", foodid);
        data.put("ts", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost/foodapp/delete_food.php"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject obj = new JSONObject(response.body());
        String status = obj.getString("status");

        if (status.equals("success")) {
            JOptionPane.showMessageDialog(frame, "Success");
            loadData();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed");
        }
    }

    protected void updatefood(int id, String foodname, String foodprice, String fooddesc)
            throws IOException, InterruptedException {
        Map<Object, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", foodname);
        data.put("price", foodprice);
        data.put("desc", fooddesc);
        data.put("ts", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost/foodapp/update_food.php"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject obj = new JSONObject(response.body());
        String status = obj.getString("status");
        if (status.equals("success")) {
            JOptionPane.showMessageDialog(frame, "Success");
            loadData();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed");
        }
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    protected void searchFood(String search) throws IOException, InterruptedException {
        System.out.println(search);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost/foodapp/search_foods.php?foodname=" + search))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        String status = obj.getString("status");

        if (status.equals("success")) {
            foodlist = new ArrayList<>();
            foodlist.clear();
            JSONObject dataobj = obj.getJSONObject("data");
            JSONArray arr = dataobj.getJSONArray("foods");
            for (int i = 0; i < arr.length(); i++) {
                int id = arr.getJSONObject(i).getInt("id");
                String foodname = arr.getJSONObject(i).getString("food_name");
                double foodprice = arr.getJSONObject(i).getDouble("food_price");
                String fooddesc = arr.getJSONObject(i).getString("food_desc");
                foodlist.add(new Food(id, foodname, foodprice, fooddesc));
            }
            dtm.setRowCount(0); // reset data model
            for (int i = 0; i < foodlist.size(); i++) {
                Object[] objs = {
                        foodlist.get(i).foodid,
                        foodlist.get(i).foodname,
                        foodlist.get(i).foodprice,
                        foodlist.get(i).fooddesc,
                };
                dtm.addRow(objs);
            }
        }
    }

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
}