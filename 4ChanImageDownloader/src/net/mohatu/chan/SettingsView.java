package net.mohatu.chan;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingsView implements Runnable {

	private JFrame frmSettings;
	private JTextField tfWidth;
	private JTextField tfHeight;
	private JRadioButton rdbtnAllSizes, rdbtnSpecificSize;
	private JButton btnCancel;

	/**
	 * Create the application.
	 */
	public SettingsView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSettings = new JFrame();
		frmSettings.setTitle("Settings");
		frmSettings.setBounds(100, 100, 244, 251);
		frmSettings.setDefaultCloseOperation(0);

		JPanel panel = new JPanel();
		frmSettings.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCancel.setText("Close");
				try {
					if (rdbtnSpecificSize.isSelected()) {
						MainView.setImageSize(true,
								Integer.parseInt(tfWidth.getText()),
								Integer.parseInt(tfHeight.getText()));
						System.out.println("Saved settings| W:"
								+ tfWidth.getText() + " H:"
								+ tfHeight.getText());
					} else {
						MainView.setImageSize(false, 0, 0);
						System.out.println("Saved settings| W: 0 H:0");
					}
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(frmSettings,
							"Please enter integers!");
				}
			}
		});
		btnApply.setBounds(20, 182, 95, 23);
		panel.add(btnApply);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSettings.dispose();
			}
		});
		btnCancel.setBounds(117, 182, 95, 23);
		panel.add(btnCancel);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Size Options",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(16, 16, 198, 160);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 16, 184, 133);
		panel_2.add(panel_1);
		panel_1.setLayout(null);

		rdbtnAllSizes = new JRadioButton("All sizes");
		rdbtnAllSizes.setBounds(6, 7, 109, 23);
		panel_1.add(rdbtnAllSizes);

		rdbtnSpecificSize = new JRadioButton("Specific size:");
		rdbtnSpecificSize.setBounds(6, 33, 109, 23);
		panel_1.add(rdbtnSpecificSize);

		tfWidth = new JTextField();
		tfWidth.setBounds(65, 63, 109, 20);
		panel_1.add(tfWidth);
		tfWidth.setColumns(10);

		tfHeight = new JTextField();
		tfHeight.setBounds(65, 94, 109, 20);
		panel_1.add(tfHeight);
		tfHeight.setColumns(10);

		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(6, 66, 46, 14);
		panel_1.add(lblWidth);

		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(6, 97, 46, 14);
		panel_1.add(lblHeight);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnAllSizes);
		group.add(rdbtnSpecificSize);

		rdbtnAllSizes.setSelected(true);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			SettingsView window = new SettingsView();
			rdbtnAllSizes.setSelected(true);
			window.frmSettings.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
