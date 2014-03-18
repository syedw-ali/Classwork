package drugiee;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class JCoolerButton extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7787367931985646783L;

	
	
	public JCoolerButton(String filler){
		this.setText(filler);
		this.setBackground(new Color(236, 240, 241));
		
		Font f = new Font("Helvetica", Font.PLAIN, 16);
		
		this.setBackground(new Color(236, 240, 241));
		this.setForeground(new Color(52, 73, 94));
		Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(52, 73, 94), new Color(52, 73, 94));
		Border compound = BorderFactory.createCompoundBorder(line, padding);
		this.setBorder(compound);
		//this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(52, 73, 94), Color.yellow));
		//this.setMargin(new Insets(5, 5, 5, 5));
		this.setFont(f);
		
	}

}

