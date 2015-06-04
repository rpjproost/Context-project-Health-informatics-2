package context.healthinformatics.interfacecomponents;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.derby.impl.sql.execute.CreateConstraintConstantAction;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.gui.MainFrame;

public class IntermediateResults extends InterfaceHelper {

	private MainFrame mf;

	public IntermediateResults(MainFrame mf) {
		this.mf = mf;
	}

	public JPanel loadPanel(){
		JPanel panel = createEmptyWithGridBagLayoutPanel(MainFrame.CODETABCOLOR);
		JTextArea intermediateResult = createTextField(mf.getScreenWidth() / 2,
				mf.getStatePanelSize() / 2 - 200);
		intermediateResult.setEditable(false);
		intermediateResult.setText("test");
		panel.add(intermediateResult, setGrids(0,0));
		return panel;
	}
}
