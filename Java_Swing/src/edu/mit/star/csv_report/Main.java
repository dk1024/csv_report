package edu.mit.star.csv_report;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/*
 * Main.java
 * Author: dk1024
 * 
 * This program takes in information on events attended by people.
 * The data should be of the following CSV format:
 * 
 * [Name of the person],[Time spent on the event],[Location of the event]
 * 
 * Each line, or row, of the data should represent one event attended by 
 * one particular person. 
 * 
 * The data is to be entered into a provided text area entitled Input.
 * There is a Calculate button right below the Input text area.  Once the 
 * data is entered and the Calculate button is clicked, the program outputs
 * the number of events attended by each individual as well as his/her 
 * average units of time spent per event.  This output is displayed in a 
 * section entitled Report, which is right below the Calculate button.
 *
 * Warning 1: Do not click the Calculate button before entering any CSV data!
 * Warning 2: Do not enter any data that does not follow the specified format!
 * 
 */


public class Main extends JFrame implements ActionListener {
	/*
	 *  Adding the listener will allow the program to detect the events,
	 *  like the adding of the CSV data to the input text area, or
	 *  the clicking of the Calculate button, and then follow those
	 *  events up with the desired results, like printing the output.
	 */
	
	private static final long serialVersionUID = 1L;
	JTextArea input;
	JButton calculate;
	JPanel report;
	JTextArea reportOut;

	@Override
	public void addNotify() {
		/*
		 *  All the initializations occur here. 
		 */
		
		super.addNotify();
		
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		
		input = new JTextArea(15, 60);
		input.setBorder(BorderFactory.createTitledBorder("Input"));
		
		calculate = new JButton("Calculate"); 
		// Beware: It moves when you click it or even add data to the Input text area 
		//         It's a little sensitive!
		calculate.addActionListener(this);
		
		report = new JPanel();
		report.setBorder(BorderFactory.createTitledBorder("Report"));
		reportOut = new JTextArea(15, 60);
		report.add(reportOut);
		
		c.add(input);
		c.add(calculate);
		c.add(report);
	}

	public static void main(String[] args) {
		try {
				SwingUtilities.invokeAndWait(new Runnable() {
	
					@Override
					public void run() {
						Main m = new Main();
						m.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						m.pack();
						m.setVisible(true);
					}
				});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 *  This is where all the magic happens.
		 */

		StringBuffer buff = new StringBuffer();
		// Better option than String because it is mutable

		Hashtable<String, ArrayList<Double>> timeSpent = new Hashtable<String, ArrayList<Double>>();
		// The hash table should allow fast access of the data.

		String inString = input.getText();
		String[] lines = inString.split("\n");
		
		// Gather the necessary data.
		for (String line : lines) {
			String[] parts = line.split(",");
			if (!timeSpent.containsKey(parts[0])) {
				timeSpent.put(parts[0], new ArrayList<Double>());
			}
			timeSpent.get(parts[0]).add(Double.parseDouble(parts[1]));
		}

		// Prepare the report and store it in the StringBuffer.
		for (String person : timeSpent.keySet()) {
			int count = timeSpent.get(person).size();
			double sum = 0;
			for (Double i : timeSpent.get(person))
				// I am aware that this loop can get time-expensive with larger amounts of data.
				// Possible improvement: Implement the same logic as in my Python program csv_report.py.
				// I just wanted to show a different approach through this Java program. 
				sum += i;
			double average = sum / count;
			
			buff.append(person);
			buff.append(" has attended ").append(count);
			buff.append(" events at an average of ").append(average);
			buff.append("units of time per event.\n"); // ...and the report's ready!
		}

		reportOut.setText(buff.toString()); // Print the report.
	}
}
