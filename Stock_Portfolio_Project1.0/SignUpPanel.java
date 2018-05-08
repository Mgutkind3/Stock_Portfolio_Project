package csi480;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class SignUpPanel extends JPanel {
	private JTextField textfield_user;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldRepeat;
				
	public SignUpPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton btnBack = new JButton("Back");
		GridBagConstraints gbc_btnBack = new GridBagConstraints();
		gbc_btnBack.insets = new Insets(0, 0, 5, 5);
		gbc_btnBack.gridx = 1;
		gbc_btnBack.gridy = 0;
		add(btnBack, gbc_btnBack);
		
		JLabel lbl_signup = new JLabel("Create New Account");
		GridBagConstraints gbc_lbl_signup = new GridBagConstraints();
		gbc_lbl_signup.insets = new Insets(0, 0, 5, 0);
		gbc_lbl_signup.gridwidth = 11;
		gbc_lbl_signup.gridx = 0;
		gbc_lbl_signup.gridy = 1;
		add(lbl_signup, gbc_lbl_signup);
		
		JLabel lbl_warning = new JLabel("");
		GridBagConstraints gbc_lbl_warning = new GridBagConstraints();
		gbc_lbl_warning.gridwidth = 3;
		gbc_lbl_warning.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_warning.gridx = 3;
		gbc_lbl_warning.gridy = 2;
		add(lbl_warning, gbc_lbl_warning);
		
		JLabel lbl_user = new JLabel("Username");
		lbl_user.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbl_user = new GridBagConstraints();
		gbc_lbl_user.anchor = GridBagConstraints.WEST;
		gbc_lbl_user.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_user.gridx = 3;
		gbc_lbl_user.gridy = 3;
		add(lbl_user, gbc_lbl_user);
		
		textfield_user = new JTextField();
		GridBagConstraints gbc_textfield_user = new GridBagConstraints();
		gbc_textfield_user.gridwidth = 2;
		gbc_textfield_user.insets = new Insets(0, 0, 5, 5);
		gbc_textfield_user.fill = GridBagConstraints.HORIZONTAL;
		gbc_textfield_user.gridx = 4;
		gbc_textfield_user.gridy = 3;
		add(textfield_user, gbc_textfield_user);
		textfield_user.setColumns(10);
		
		JLabel lbl_password = new JLabel("Password");
		lbl_password.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbl_password = new GridBagConstraints();
		gbc_lbl_password.anchor = GridBagConstraints.WEST;
		gbc_lbl_password.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_password.gridx = 3;
		gbc_lbl_password.gridy = 4;
		add(lbl_password, gbc_lbl_password);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 2;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 4;
		gbc_passwordField.gridy = 4;
		add(passwordField, gbc_passwordField);
		
		JLabel lbl_passwordRepeat = new JLabel("Repeat password");
		lbl_passwordRepeat.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbl_passwordRepeat = new GridBagConstraints();
		gbc_lbl_passwordRepeat.anchor = GridBagConstraints.WEST;
		gbc_lbl_passwordRepeat.insets = new Insets(0, 0, 5, 5);
		gbc_lbl_passwordRepeat.gridx = 3;
		gbc_lbl_passwordRepeat.gridy = 5;
		add(lbl_passwordRepeat, gbc_lbl_passwordRepeat);
		
		passwordFieldRepeat = new JPasswordField();
		GridBagConstraints gbc_passwordFieldRepeat = new GridBagConstraints();
		gbc_passwordFieldRepeat.gridwidth = 2;
		gbc_passwordFieldRepeat.insets = new Insets(0, 0, 5, 5);
		gbc_passwordFieldRepeat.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordFieldRepeat.gridx = 4;
		gbc_passwordFieldRepeat.gridy = 5;
		add(passwordFieldRepeat, gbc_passwordFieldRepeat);
		
		JButton btn_clear = new JButton("Clear");
		GridBagConstraints gbc_btn_clear = new GridBagConstraints();
		gbc_btn_clear.insets = new Insets(0, 0, 0, 5);
		gbc_btn_clear.gridx = 4;
		gbc_btn_clear.gridy = 6;
		add(btn_clear, gbc_btn_clear);
		
		JButton btn_submit = new JButton("Submit");
		GridBagConstraints gbc_btn_submit = new GridBagConstraints();
		gbc_btn_submit.insets = new Insets(0, 0, 0, 5);
		gbc_btn_submit.gridx = 5;
		gbc_btn_submit.gridy = 6;
		add(btn_submit, gbc_btn_submit);

		
		btn_submit.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		        String username;
		
		        char pass[];
		        char passRepeat[];
		        username = textfield_user.getText().toString().trim();
		       
		        pass = passwordField.getPassword();
		        passRepeat = passwordFieldRepeat.getPassword();
		        String password = new String (pass);
		        String passwordRepeat = new String (passRepeat);
		        System.out.println(password);
		        System.out.println(passwordRepeat);
		        Boolean passconfirm = null;
		        if(username.equals("")) {
		        	lbl_warning.setText("Please add a username");
		        }
		        if (password.equals(passwordRepeat)) {
		        	passconfirm = true;
		        } else {
		        	lbl_warning.setText("Your passwords do not match!");
		        }
		        
		        if(passconfirm  = true || username !=null) {
		        	MongoConnect signup = new MongoConnect();
		        	Boolean userExists;
		        	userExists = false;
		        	
		        	userExists = MongoConnect.checkUsernameExist(username);
		        	
		        	if(Boolean.TRUE.equals(userExists)) {
		        	lbl_warning.setText("The username you chose already exists");
		        	
		        
		        	} else {
		        	try {
						MongoConnect.createUser(username, password);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	}
		        	
		        }

		      }
		    });
		
		
		btnBack.addActionListener(new ActionListener()
	    {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame main = new MainFrame();
				main.toSignIn();

				
			}
	      
	    });
		
		btn_clear.addActionListener(new ActionListener()
	    {

			@Override
			public void actionPerformed(ActionEvent e) {
				textfield_user.setText("");
				passwordField.setText("");
				passwordFieldRepeat.setText("");

				
			}
	      
	    });
	
	}
}