import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class ShapeDraw extends JFrame {
 
	JButton addButton;
	JButton changeButton;
	JButton removeButton;
	JPanel rightPane;
	JList shapeSelect;
	
	
    public ShapeDraw()
    {
		Container pane = getContentPane();
		pane.setLayout (new GridLayout (0,2));
		
		JPanel leftPane = new JPanel ();
		leftPane.setLayout(new GridLayout(2,0));
		
		String listData[] = {
				"Line","Rectangle","Oval"
		};
		
		shapeSelect = new JList(listData);
		shapeSelect.setPreferredSize(new Dimension(350,150));
		leftPane.add(shapeSelect, BorderLayout.NORTH);
		
		JPanel buttonsPane = new JPanel();
		addButton = new JButton("Add");
		changeButton = new JButton("Change");
		removeButton = new JButton("Remove");
		
		addButton.addActionListener(new AddButtonActionListener());
		
		buttonsPane.add(addButton);
		buttonsPane.add(changeButton);
		buttonsPane.add(removeButton);
		
		leftPane.add(buttonsPane, BorderLayout.SOUTH);
		rightPane = new JPanel();
		rightPane.setPreferredSize(new Dimension(500,500));
		rightPane.setVisible(true);
		rightPane.setBackground(Color.green);
		
		
		pane.add(leftPane);
		pane.add(rightPane);
		
    }
  
    public class AddButtonActionListener implements ActionListener{
    	JDialog addDialogWin = new JDialog();
		String[] colors = { "None", "Black", "Blue", "Green", "Red", "Yellow" };
		
		JLabel strokeColorLabel = new JLabel("Stroke Color:");
		JLabel fillColorLabel = new JLabel("Fill Color:");
		JLabel position1XLabel = new JLabel("Position1 X:");
		JLabel position1YLabel = new JLabel("Position1 Y:");
		JLabel position2XLabel = new JLabel("Position2 X");
		JLabel position2YLabel = new JLabel("Position2 Y");
		JComboBox strokeColorCombo = new JComboBox(colors);
		JComboBox fillColorCombo = new JComboBox(colors);
		JTextField position1XTextField = new JTextField();
		JTextField position1YTextField = new JTextField();
		JTextField position2XTextField = new JTextField();
		JTextField position2YTextField = new JTextField();
		JButton okButton_line = new JButton("Ok");
		JButton cancelButton_line = new JButton("Cancel");
		
    	public void actionPerformed (ActionEvent e){
    		
    		
    		if(e.equals(okButton_line)){
    			// draw shape
    		} else if(e.equals(cancelButton_line)){
    			addDialogWin.dispose();
    		} else {
    			buildDialog();
    		}
    		
    		
    		
    	}
    	
    	public void buildDialog(){
    		addDialogWin.setPreferredSize(new Dimension(300,300));
    		
    		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    		pack();
    		tabbedPane.setVisible(true);
    		
    		// Add new line panel
    		JComponent addLinePane = new JPanel();
    		
    		addLinePane.setLayout(new GridLayout(7,2));
    		
    		addLinePane.add(strokeColorLabel);
    		addLinePane.add(strokeColorCombo);
    		addLinePane.add(fillColorLabel);
    		addLinePane.add(fillColorCombo);
    		addLinePane.add(position1XLabel);
    		addLinePane.add(position1XTextField);
    		addLinePane.add(position1YLabel);
    		addLinePane.add(position1YTextField);
    		addLinePane.add(position2XLabel);
    		addLinePane.add(position2XTextField);
    		addLinePane.add(position2YLabel);
    		addLinePane.add(position2YTextField);
    		addLinePane.add(okButton_line);
    		addLinePane.add(cancelButton_line);
    		
    		tabbedPane.addTab("Line", addLinePane);
    		// OK NOW NEED TO FIGURE OUT HOW TO FIND WHAT THE USE SELECTED ONCE THEY CLICK OK
    		okButton_line.addActionListener(this);
    		
    		// Add new Rectangle panel
    		JComponent addRectPane = new JPanel();
    		
    		tabbedPane.addTab("Rectangle", addRectPane);
    		
    		//getContentPane().add(tabbedPane);
    		addDialogWin.getContentPane().add(tabbedPane);
    		addDialogWin.pack();
    		addDialogWin.setVisible(true);
    	}

    }
    
    
    public static void main(String args[])
    {
    	ShapeDraw frame = new ShapeDraw();
		frame.setTitle("Shape Draw");
		frame.pack();
		frame.setVisible(true);
    }
}