package a.com.port.reader;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class App {
	private final int W = 800;
	private final int H = 300;

	private JFrame initFrame() {
		System.out.println("Initializing JFrame UI");

		JFrame frame = new JFrame("COM Port Data Reader");
		frame.setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeApp();
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(null);
		frame.setContentPane(panel);
		frame.setSize(W, H);
		// frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblLabel = new JLabel("Port:");
		lblLabel.setBounds(20, 20, 100, 30);
		lblLabel.setBackground(Color.cyan);
		panel.add(lblLabel);
		JComboBox<String> cbPort = new JComboBox<String>();
		cbPort.setBounds(160, 20, 150, 30);
		panel.add(cbPort);
		cbPort.addActionListener((evt) -> {
			String val = (String) ((JComboBox<String>) evt.getSource()).getSelectedItem();
			System.out.println("You selected " + val);
			// if (val != null) {
			// JOptionPane.showMessageDialog(null, "You selected " + val);
			// }
		});

		lblLabel = new JLabel("Baud rate:");
		lblLabel.setBounds(20, 60, 100, 30);
		panel.add(lblLabel);
		JComboBox<Integer> cbBaudRate = new JComboBox<Integer>(
				new Integer[] { 300, 600, 1200, 2400, 4800, 9600, 19200, 38400, 115200, 57600 });
		cbBaudRate.setSelectedItem(9600);
		cbBaudRate.setBounds(160, 60, 150, 30);
		panel.add(cbBaudRate);

		lblLabel = new JLabel("Data bits:");
		lblLabel.setBounds(20, 100, 100, 30);
		panel.add(lblLabel);
		JComboBox<Integer> cbDataBits = new JComboBox<Integer>(new Integer[] { 5, 6, 7, 8 });
		cbDataBits.setSelectedItem(8);
		cbDataBits.setBounds(160, 100, 150, 30);
		panel.add(cbDataBits);

		lblLabel = new JLabel("Stop bits:");
		lblLabel.setBounds(20, 140, 100, 30);
		panel.add(lblLabel);
		JComboBox<Integer> cbStopBits = new JComboBox<Integer>(new Integer[] { 1, 2 });
		cbStopBits.setSelectedItem(1);
		cbStopBits.setBounds(160, 140, 150, 30);
		panel.add(cbStopBits);

		lblLabel = new JLabel("Parity:");
		lblLabel.setBounds(20, 180, 100, 30);
		panel.add(lblLabel);
		JComboBox<String> cbParity = new JComboBox<String>(new String[] { "NONE", "EVEN", "ODD", "MARK", "SPACE" });
		cbParity.setSelectedItem("NONE");
		cbParity.setBounds(160, 180, 150, 30);
		panel.add(cbParity);

		JTextArea txtArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(txtArea);
		scrollPane.setBounds(320, 20, 450, 190);
		panel.add(scrollPane);

		JButton btnOpen = new JButton("Connect");
		btnOpen.setBounds(320, 220, 120, 30);
		panel.add(btnOpen);
		btnOpen.addActionListener((event) -> {
			btnOpen.setEnabled(false);

			txtArea.setText("TODO: Connect to " + cbPort.getSelectedItem());
			txtArea.setEditable(false);

			cbPort.setEnabled(false);
			cbBaudRate.setEnabled(false);
			cbDataBits.setEnabled(false);
			cbStopBits.setEnabled(false);
			cbParity.setEnabled(false);
		});

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(460, 220, 120, 30);
		panel.add(btnClose);
		btnClose.addActionListener((event) -> {
			btnOpen.setEnabled(true);

			txtArea.setText("TODO: Closed");
			txtArea.setEditable(true);

			cbPort.setEnabled(true);
			cbBaudRate.setEnabled(true);
			cbDataBits.setEnabled(true);
			cbStopBits.setEnabled(true);
			cbParity.setEnabled(true);
		});

		cbPort.removeAllItems();
		Stream.of("COM1", "COM2", "COM3", "COM4").forEach(cbPort::addItem);

		return frame;
	}

	private void closeApp() {
		System.out.println("Exit App");
		System.exit(0);
	}

	public App() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initFrame().setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		new App();
	}

}
