package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JButton;

public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("AaG-Visualizer");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 159, 562);
		contentPane.add(panel);
		
		JButton btnNext = new JButton("next");
		panel.add(btnNext);
		
		JButton btnPlay = new JButton("play");
		panel.add(btnPlay);
		
		JPanel canvas = new Canvas();
		canvas.setBackground(UIManager.getColor("CheckBox.background"));
		canvas.setBounds(161, 0, 623, 562);
		contentPane.add(canvas);
	}
}
