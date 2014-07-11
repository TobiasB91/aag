package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import algorithms.Algorithm;
import algorithms.Dijkstra;
import algorithms.FloydWarshall;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private Canvas canvas;
	public static Algorithm currentAlgorithm = null;

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
		
		JLabel lblWeight = new JLabel("weight:");
		lblWeight.setBounds(25, 40, 50, 20);
		panel.add(lblWeight);
		
		canvas = new Canvas();
		canvas.setBackground(UIManager.getColor("CheckBox.background"));
		canvas.setBounds(161, 0, 623, 562);
		contentPane.add(canvas);
		
		JButton btnNext = new JButton("next");
		btnNext.setBounds(15, 5, 60, 23);
		panel.add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentAlgorithm != null) {
					currentAlgorithm.nextStep();
					canvas.repaint();
				}
			}
		});
		
		JButton btnPlay = new JButton("play");
		btnPlay.setBounds(80, 5, 60, 23);
		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentAlgorithm != null) {
					currentAlgorithm.play();
					canvas.repaint();
				}
			}
		});
		panel.add(btnPlay);
		
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
		
		JToggleButton tglbtnDijkstra = new JToggleButton("Dijkstra");
		tglbtnDijkstra.setBounds(10, 265, 121, 23);
		panel.add(tglbtnDijkstra);
		
		JToggleButton tglbtnFloydWarshall = new JToggleButton("Floyd-Warshall");
		tglbtnFloydWarshall.setBounds(10, 288, 121, 23);
		panel.add(tglbtnFloydWarshall);
		

		tglbtnDijkstra.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED && canvas.getMarkedVertex() != null) {
					currentAlgorithm = new Dijkstra(canvas.getGraph(), canvas.getMarkedVertex());
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	currentAlgorithm = null;
			      }
				canvas.repaint();
			}
		});
		
		tglbtnFloydWarshall.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED && canvas.getMarkedVertex() != null) {
					currentAlgorithm = new FloydWarshall(canvas.getGraph());
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	currentAlgorithm = null;
			      }
				canvas.repaint();
			}
		});
		

		
		contentPane.repaint();
	}
}
