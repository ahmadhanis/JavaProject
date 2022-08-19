import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {

    JTextField jtf_numa, jtf_numb;
    JLabel jl_a, jl_b, jl_result;
    JButton jb_add;
    JFrame jframe;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.mainInterface();
    }

    private void mainInterface() {
        jframe = new JFrame();

        jl_a = new JLabel();
        jl_a.setText("First Number");
        jl_a.setBounds(10, 10, 100, 40);
        jframe.add(jl_a);

        jl_b = new JLabel();
        jl_b.setText("Second Number");
        jl_b.setBounds(10, 40, 100, 40);
        jframe.add(jl_b);

        jtf_numa = new JTextField();
        jtf_numa.setBounds(120, 10, 100, 30);
        jframe.add(jtf_numa);

        jtf_numb = new JTextField();
        jtf_numb.setBounds(120, 40, 100, 30);
        jframe.add(jtf_numb);

        jb_add = new JButton("+");
        jb_add.setBounds(120, 70, 100, 30);
        jframe.add(jb_add);
        jb_add.addActionListener(addListerner);

        jl_result = new JLabel();
        jl_result.setText("Result: ");
        jl_result.setBounds(120, 110, 100, 30);
        jframe.add(jl_result);

        jframe.setSize(400, 400);
        jframe.setLayout(null);
        jframe.setVisible(true);

    }

    ActionListener addListerner = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            int numa = Integer.parseInt(jtf_numa.getText());
            int numb = Integer.parseInt(jtf_numb.getText());
            int result = numa + numb;
            jl_result.setText("Result: " + result);
        }
    };
}
