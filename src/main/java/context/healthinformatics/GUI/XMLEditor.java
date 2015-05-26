package context.healthinformatics.GUI;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class XMLEditor extends InterfaceHelper {

	public XMLEditor(){
		
	}
	
	public JPanel loadPanel(){
		JPanel jp = createPanel(Color.decode("#00000"), 900, 700);
		//jp.setLayout(new BorderLayout(10,10));
		//jp.setBorder(new EmptyBorder(100, 10, 10, 10) );
		//jp.setMargin(new Insets(insetTop, insetLeft, insetBottom, insetRight));
		return jp;
	}
}
