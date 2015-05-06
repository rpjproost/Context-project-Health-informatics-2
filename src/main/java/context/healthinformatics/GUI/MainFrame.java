package context.healthinformatics.GUI;

public class MainFrame {

	PanelState state;
	PanelState inputState = new InputPage(this);
	PanelState codeState = new CodePage(this);
	PanelState outputState = new OutputPage(this);
	
	public MainFrame() {
		state = inputState;
		load();
	}

	protected void load() {
		
	}
	
	
}
