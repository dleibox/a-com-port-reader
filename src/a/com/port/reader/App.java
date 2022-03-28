package a.com.port.reader;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class App {
	private final int W = 800;
	private final int H = 310;
	private final int BUFFER_TIMEOUT = 800;

	private SerialPort serialPort;
	private JComboBox<String> cbPort;
	private JComboBox<Integer> cbBaudRate;
	private JComboBox<Integer> cbDataBits;
	private JComboBox<Integer> cbStopBits;
	private JComboBox<String> cbParity;
	private JTextArea txtArea;

	private JFrame initFrame() {
		System.out.println("Initializing UI");

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
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblLabel = new JLabel("Port:");
		lblLabel.setBounds(20, 20, 100, 30);
		lblLabel.setBackground(Color.cyan);
		panel.add(lblLabel);
		cbPort = new JComboBox<String>();
		cbPort.setBounds(160, 20, 150, 30);
		panel.add(cbPort);

		lblLabel = new JLabel("Baud rate:");
		lblLabel.setBounds(20, 60, 100, 30);
		panel.add(lblLabel);
		cbBaudRate = new JComboBox<Integer>(new Integer[] { 300, 600, 1200, 2400, 4800, 9600, 19200, 38400, 115200 });
		cbBaudRate.setSelectedItem(9600);
		cbBaudRate.setBounds(160, 60, 150, 30);
		panel.add(cbBaudRate);

		lblLabel = new JLabel("Data bits:");
		lblLabel.setBounds(20, 100, 100, 30);
		panel.add(lblLabel);
		cbDataBits = new JComboBox<Integer>(new Integer[] { 5, 6, 7, 8 });
		cbDataBits.setSelectedItem(8);
		cbDataBits.setBounds(160, 100, 150, 30);
		panel.add(cbDataBits);

		lblLabel = new JLabel("Stop bits:");
		lblLabel.setBounds(20, 140, 100, 30);
		panel.add(lblLabel);
		cbStopBits = new JComboBox<Integer>(new Integer[] { SerialPort.ONE_STOP_BIT,
				SerialPort.ONE_POINT_FIVE_STOP_BITS, SerialPort.TWO_STOP_BITS });

		cbStopBits.setSelectedItem(1);
		cbStopBits.setBounds(160, 140, 150, 30);
		panel.add(cbStopBits);

		lblLabel = new JLabel("Parity:");
		lblLabel.setBounds(20, 180, 100, 30);
		panel.add(lblLabel);
		cbParity = new JComboBox<String>(new String[] { "NONE", "ODD", "EVEN", "MARK", "SPACE" });
		cbParity.setSelectedItem("NONE");
		cbParity.setBounds(160, 180, 150, 30);
		panel.add(cbParity);

		txtArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(txtArea);
		scrollPane.setBounds(320, 20, 450, 232);
		panel.add(scrollPane);

		JButton btnOpen = new JButton("Connect");
		btnOpen.setBounds(20, 220, 120, 30);
		panel.add(btnOpen);
		btnOpen.addActionListener((event) -> {
			if (openCOM()) {
				btnOpen.setEnabled(false);

				txtArea.setText("Connected successfully: " + cbPort.getSelectedItem());
				txtArea.setEditable(false);

				cbPort.setEnabled(false);
				cbBaudRate.setEnabled(false);
				cbDataBits.setEnabled(false);
				cbStopBits.setEnabled(false);
				cbParity.setEnabled(false);
			}
		});

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(160, 220, 120, 30);
		panel.add(btnClose);
		btnClose.addActionListener((event) -> {
			txtArea.setEditable(true);

			cbPort.setEnabled(true);
			cbBaudRate.setEnabled(true);
			cbDataBits.setEnabled(true);
			cbStopBits.setEnabled(true);
			cbParity.setEnabled(true);

			if (closeCOM()) {
				btnOpen.setEnabled(true);
			}
		});

		return frame;
	}

	private void closeApp() {
		System.out.println("Exit App");
		closeCOM();
		System.exit(0);
	}

	private void initCOM() {

		SerialPort[] ports = SerialPort.getCommPorts();
		cbPort.removeAllItems();
		Stream.of(ports).map(p -> p.getSystemPortName()).forEach(cbPort::addItem);
	}

	private boolean openCOM() {
		if (serialPort != null && serialPort.isOpen()) {
			JOptionPane.showMessageDialog(null, "COM port has been connected!");
			return false;
		}

		serialPort = SerialPort.getCommPort((String) cbPort.getSelectedItem());
		System.out.println("Connected to " + serialPort.getDescriptivePortName());

		boolean openedSuccessfully = serialPort.openPort();
		System.out.println("\nOpening " + serialPort.getSystemPortName() + ": " + serialPort.getDescriptivePortName()
				+ " - " + serialPort.getPortDescription() + ": " + openedSuccessfully);
		if (!openedSuccessfully) {
			System.out.println("Error code was " + serialPort.getLastErrorCode() + " at Line "
					+ serialPort.getLastErrorLocation());
			JOptionPane.showMessageDialog(null, "Error code: " + serialPort.getLastErrorCode());
			return false;
		}

		final List<byte[]> dataList = new ArrayList<byte[]>();

		System.out.println("\nNow waiting asynchronously for all possible listening events...");
		serialPort.addDataListener(new SerialPortDataListener() {
			// @Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_PARITY_ERROR | SerialPort.LISTENING_EVENT_DATA_WRITTEN
						| SerialPort.LISTENING_EVENT_BREAK_INTERRUPT | SerialPort.LISTENING_EVENT_CARRIER_DETECT
						| SerialPort.LISTENING_EVENT_CTS | SerialPort.LISTENING_EVENT_DSR
						| SerialPort.LISTENING_EVENT_RING_INDICATOR | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED
						| SerialPort.LISTENING_EVENT_FRAMING_ERROR | SerialPort.LISTENING_EVENT_FIRMWARE_OVERRUN_ERROR
						| SerialPort.LISTENING_EVENT_SOFTWARE_OVERRUN_ERROR | SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			// @Override
			public void serialEvent(SerialPortEvent event) {
				if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
					byte[] buffer = new byte[event.getSerialPort().bytesAvailable()];
					event.getSerialPort().readBytes(buffer, buffer.length);

					if (dataList.size() == 0) {
						(new Thread(() -> {
							try {
								Thread.sleep(BUFFER_TIMEOUT);

								byte[] dataArr = dataList.stream().collect(() -> new ByteArrayOutputStream(),
										(b, e) -> b.write(e, 0, e.length), (a, b) -> {
										}).toByteArray();

								String data = new String(dataArr);
								System.out.println("\n***\n" + data);
								txtArea.setText(data);

								dataList.clear();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						})).start();
					}
					dataList.add(buffer);
				} else if (event.getEventType() == SerialPort.LISTENING_EVENT_PORT_DISCONNECTED)
					System.out.println("Received event type: LISTENING_EVENT_PORT_DISCONNECTED");
				else
					System.out.println("Received event type: " + event.getEventType());
			}
		});

		int parity = SerialPort.NO_PARITY;
		String parityStr = (String) cbParity.getSelectedItem();
		if ("ODD".equals(parityStr)) {
			parity = SerialPort.ODD_PARITY;
		} else if ("EVEN".equals(parityStr)) {
			parity = SerialPort.EVEN_PARITY;
		} else if ("MARK".equals(parityStr)) {
			parity = SerialPort.MARK_PARITY;
		} else if ("SPACE".equals(parityStr)) {
			parity = SerialPort.SPACE_PARITY;
		}

		serialPort.setComPortParameters((int) cbBaudRate.getSelectedItem(), (int) cbDataBits.getSelectedItem(),
				(int) cbStopBits.getSelectedItem(), parity);

		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

		return true;
	}

	private boolean closeCOM() {
		if (serialPort != null && serialPort.isOpen()) {
			serialPort.removeDataListener();
			serialPort.closePort();
			System.out.println(serialPort.getDescriptivePortName() + " has been disconnected");
//			serialPort = null;
		}
		return true;
	}

	public App() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initFrame().setVisible(true);
				initCOM();
			}
		});
	}

	public static void main(String[] args) {
		new App();
	}

}
