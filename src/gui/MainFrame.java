package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

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
		panel.setLayout(null);
		
		JButton btnNext = new JButton("next");
		btnNext.setBounds(15, 5, 60, 23);
		panel.add(btnNext);
		
		JButton btnPlay = new JButton("play");
		btnPlay.setBounds(80, 5, 60, 23);
		panel.add(btnPlay);
		
		JLabel lblWeight = new JLabel("weight:");
		lblWeight.setBounds(25, 40, 50, 20);
		panel.add(lblWeight);
		
		
		
		final Canvas canvas = new Canvas();
		canvas.setBackground(UIManager.getColor("CheckBox.background"));
		canvas.setBounds(161, 0, 623, 562);
		contentPane.add(canvas);
		
		textField = new JTextField();
		textField.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(DocumentEvent arg0) {
				
			}

			public void insertUpdate(DocumentEvent arg0) {
				try {
					canvas.updateWeight(Integer.parseInt(textField.getText()));
				} catch (NumberFormatException e) {
					
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				try {
					canvas.updateWeight(Integer.parseInt(textField.getText()));
				} catch (NumberFormatException e) {
					
				}
			}
		});
		
		textField.setBounds(85, 40, 30, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		contentPane.repaint();
	}
}
