//

//

package noppes.npcs.client.gui.swing;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import noppes.npcs.client.gui.util.IJTextAreaListener;

public class GuiJTextArea extends JDialog implements WindowListener {
	public IJTextAreaListener listener;
	private JTextArea area;

	public GuiJTextArea(String text) {
		setDefaultCloseOperation(2);
		this.setSize(Display.getWidth() - 40, Display.getHeight() - 40);
		this.setLocation(Display.getX() + 20, Display.getY() + 20);
		JScrollPane scroll = new JScrollPane(area = new JTextArea(text));
		scroll.setVerticalScrollBarPolicy(22);
		this.add(scroll);
		addWindowListener(this);
		setVisible(true);
	}

	public GuiJTextArea setListener(IJTextAreaListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if (listener == null) {
			return;
		}
		Minecraft.getMinecraft().addScheduledTask(() -> listener.saveText(area.getText()));
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
