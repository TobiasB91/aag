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
import algorithms.EulerianTest;
import algorithms.Kruskal;
import algorithms.FordFulkerson;
import algorithms.MinSpantree;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private Canvas canvas;
	public static boolean directed = false;
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
		
		JLabel lblWeight = new JLabel("weight/cap:");
		lblWeight.setBounds(15, 40, 77, 20);
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
					Integer parsedInteger = Integer.parseInt(textField.getText());
					canvas.updateWeight(parsedInteger >= 0 ? parsedInteger : Math.abs(parsedInteger));
				} catch (NumberFormatException e) {
					
				}
			}

			public void removeUpdate(DocumentEvent arg0) {
				try {
					Integer parsedInteger = Integer.parseInt(textField.getText());
					canvas.updateWeight(parsedInteger >= 0 ? parsedInteger : Math.abs(parsedInteger));
				} catch (NumberFormatException e) {
					
				}
			}
		});
		
		textField.setBounds(90, 40, 41, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		final JToggleButton tglbtnDijkstra = new JToggleButton("Dijkstra");
		tglbtnDijkstra.setBounds(10, 265, 121, 23);
		tglbtnDijkstra.setEnabled(false);
		panel.add(tglbtnDijkstra);
		
		final JToggleButton tglbtnFloydWarshall = new JToggleButton("Floyd-Warshall");
		tglbtnFloydWarshall.setBounds(10, 288, 121, 23);
		tglbtnFloydWarshall.setEnabled(false);
		panel.add(tglbtnFloydWarshall);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Directed graph");
		tglbtnNewToggleButton.setBounds(10, 231, 121, 23);
		panel.add(tglbtnNewToggleButton);
		
		final JToggleButton tglbtnEulerianTest = new JToggleButton("Eulerian test");
		tglbtnEulerianTest.setBounds(10, 355, 121, 23);
		panel.add(tglbtnEulerianTest);
		
		final JToggleButton tglbtnMinspantree = new JToggleButton("Kruskal");
		tglbtnMinspantree.setBounds(10, 401, 121, 23);
		panel.add(tglbtnMinspantree);
		
		final JToggleButton tglbtnFordfulkerson = new JToggleButton("Ford-Fulkerson");
		tglbtnFordfulkerson.setBounds(10, 311, 121, 23);
		tglbtnFordfulkerson.setEnabled(false);
		panel.add(tglbtnFordfulkerson);
		
		final JToggleButton tglbtnMinspantree_1 = new JToggleButton("MinSpantree");
		tglbtnMinspantree_1.setBounds(10, 378, 121, 23);
		panel.add(tglbtnMinspantree_1);
		

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
		
		tglbtnEulerianTest.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					currentAlgorithm = new EulerianTest(canvas.getGraph());
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	currentAlgorithm = null;
			      }
				canvas.repaint();
			}
		});
		
		tglbtnMinspantree.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					currentAlgorithm = new Kruskal(canvas.getGraph());
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	currentAlgorithm = null;
			      }
				canvas.repaint();
			}
		});
		
		tglbtnFordfulkerson.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					currentAlgorithm = new FordFulkerson(canvas.getGraph());
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	currentAlgorithm = null;
			      }
				canvas.repaint();
			}
		});
		
		tglbtnMinspantree_1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					currentAlgorithm = new MinSpantree(canvas.getGraph());
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	currentAlgorithm = null;
			      }
				canvas.repaint();
			}
		});
		
		tglbtnNewToggleButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.SELECTED) {
					directed = true;
					
					tglbtnDijkstra.setEnabled(true);
					tglbtnFloydWarshall.setEnabled(true);
					tglbtnFordfulkerson.setEnabled(true);
					
			    	tglbtnMinspantree.setEnabled(false);
			    	tglbtnMinspantree_1.setEnabled(false);
			    	tglbtnEulerianTest.setEnabled(false);
			    	
					
			      } else if(e.getStateChange()==ItemEvent.DESELECTED) {
			    	directed = false;
			    	
			    	tglbtnMinspantree.setEnabled(true);
			    	tglbtnMinspantree_1.setEnabled(true);
			    	tglbtnEulerianTest.setEnabled(true);

			    	tglbtnFordfulkerson.setEnabled(false);
			    	tglbtnDijkstra.setEnabled(false);
					tglbtnFloydWarshall.setEnabled(false);
					
			      }
				canvas.repaint();
			}
		});
		

		
		contentPane.repaint();
	}
}
