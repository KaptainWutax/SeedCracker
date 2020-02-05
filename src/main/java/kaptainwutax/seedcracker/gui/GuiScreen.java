package kaptainwutax.seedcracker.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class GuiScreen extends Screen {

	protected GuiScreen(String name) {
		super(new LiteralText(name));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
	}

}
