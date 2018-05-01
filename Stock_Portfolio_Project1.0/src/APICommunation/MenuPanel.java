package APICommunation;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.parser.ParseException;

import javax.swing.JButton;

public class MenuPanel extends JPanel {
	private JPasswordField passfield;
	private JTextField textfield_user;
	public MenuPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lbl_signin = new JLabel("Sign In");
		GridBagConstraints gbc_lbl_signin = new GridBagConstraints();
		gbc_lbl_signin.insets = new Insets(0, 0, 5, 0);
		gbc_lbl_signin.gridwidth = 10;
		gbc_lbl_signin.gridx = 0;
		gbc_lbl_signin.gridy = 0;
		add(lbl_signin, gbc_lbl_signin);
		
		JLabel lbl_warning = new JLabel("");
		GridBagConstraints gbc_lbl_warning = new GridBagConstraints();
		gbc_lbl_warning.gridwidth = 3;
		gbc_lbl_warning.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_warning.gridx = 3;
		gbc_lbl_warning.gridy = 1;
		add(lbl_warning, gbc_lbl_warning);
		
		JLabel lbl_user = new JLabel("Username:");
		GridBagConstraints gbc_lbl_user = new GridBagConstraints();
		gbc_lbl_user.anchor = GridBagConstraints.EAST;
		gbc_lbl_user.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_user.gridx = 3;
		gbc_lbl_user.gridy = 2;
		add(lbl_user, gbc_lbl_user);
		
		textfield_user = new JTextField();
		GridBagConstraints gbc_textfield_user = new GridBagConstraints();
		gbc_textfield_user.gridwidth = 2;
		gbc_textfield_user.insets = new Insets(0, 0, 5, 5);
		gbc_textfield_user.fill = GridBagConstraints.HORIZONTAL;
		gbc_textfield_user.gridx = 4;
		gbc_textfield_user.gridy = 2;
		add(textfield_user, gbc_textfield_user);
		textfield_user.setColumns(10);
		
		JLabel lbl_pass = new JLabel("Password:");
		GridBagConstraints gbc_lbl_pass = new GridBagConstraints();
		gbc_lbl_pass.anchor = GridBagConstraints.EAST;
		gbc_lbl_pass.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_pass.gridx = 3;
		gbc_lbl_pass.gridy = 3;
		add(lbl_pass, gbc_lbl_pass);
		
		passfield = new JPasswordField();
		GridBagConstraints gbc_passfield = new GridBagConstraints();
		gbc_passfield.gridwidth = 2;
		gbc_passfield.insets = new Insets(0, 0, 5, 5);
		gbc_passfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_passfield.gridx = 4;
		gbc_passfield.gridy = 3;
		add(passfield, gbc_passfield);
		
		JButton btn_signup = new JButton("Create Account");
		GridBagConstraints gbc_btn_signup = new GridBagConstraints();
		gbc_btn_signup.insets = new Insets(0, 0, 0, 5);
		gbc_btn_signup.gridx = 4;
		gbc_btn_signup.gridy = 4;
		add(btn_signup, gbc_btn_signup);
		
		JButton btn_signin = new JButton("Sign In");
		GridBagConstraints gbc_btn_signin = new GridBagConstraints();
		gbc_btn_signin.insets = new Insets(0, 0, 0, 5);
		gbc_btn_signin.gridx = 5;
		gbc_btn_signin.gridy = 4;
		add(btn_signin, gbc_btn_signin);
		
		
		btn_signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username;
				//three means nothing has happened
				int result;
				username = textfield_user.getText().toString().trim();
			    char pass[];
			    pass = passfield.getPassword();
			    String password = new String (pass);
			    
				try {
					result = MongoConnect.login(username, password);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					result = 3;
				}
				
				System.out.println(result);
				if(result == 2 ) {
					MainFrame main = new MainFrame();
					main.toSummary();
				
				} else if (result == 0) {
					lbl_warning.setText("Password incorrect");
				} else if (result == 1) {
					lbl_warning.setText("Username or password incorrect");
				} else {
					lbl_warning.setText("Something went wrong, please try again later");
				}
				
			}
		});
		
		btn_signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame();
				mainFrame.toSignup();
			}
		});
		
	}
}
