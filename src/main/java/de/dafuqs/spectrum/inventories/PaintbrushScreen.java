package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class PaintbrushScreen extends QuickNavigationGridScreen<PaintbrushScreenHandler> {
	
	public static final QuickNavigationGridScreen.Grid MAGENTA_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.MAGENTA.getColorVec(), "spectrum.ink.color.spectrum.magenta", (screen) -> chooseColor(InkColors.MAGENTA)),
			GridEntry.colored(InkColors.PINK.getColorVec(), "spectrum.ink.color.spectrum.pink", (screen) -> chooseColor(InkColors.PINK)),
			GridEntry.colored(InkColors.RED.getColorVec(), "spectrum.ink.color.spectrum.red", (screen) -> chooseColor(InkColors.RED)),
			GridEntry.colored(InkColors.PURPLE.getColorVec(), "spectrum.ink.color.spectrum.purple", (screen) -> chooseColor(InkColors.PURPLE)),
			GridEntry.BACK
	);
	
	public static final QuickNavigationGridScreen.Grid CYAN_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.CYAN.getColorVec(), "spectrum.ink.color.spectrum.cyan", (screen) -> chooseColor(InkColors.CYAN)),
			GridEntry.BACK,
			GridEntry.colored(InkColors.LIGHT_BLUE.getColorVec(), "spectrum.ink.color.spectrum.light_blue", (screen) -> chooseColor(InkColors.LIGHT_BLUE)),
			GridEntry.colored(InkColors.BLUE.getColorVec(), "spectrum.ink.color.spectrum.blue", (screen) -> chooseColor(InkColors.BLUE)),
			GridEntry.colored(InkColors.LIME.getColorVec(), "spectrum.ink.color.spectrum.lime", (screen) -> chooseColor(InkColors.LIME))
	);
	
	public static final QuickNavigationGridScreen.Grid YELLOW_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.YELLOW.getColorVec(), "spectrum.ink.color.spectrum.yellow", (screen) -> chooseColor(InkColors.YELLOW)),
			GridEntry.colored(InkColors.GREEN.getColorVec(), "spectrum.ink.color.spectrum.green", (screen) -> chooseColor(InkColors.GREEN)),
			GridEntry.BACK,
			GridEntry.colored(InkColors.BROWN.getColorVec(), "spectrum.ink.color.spectrum.brown", (screen) -> chooseColor(InkColors.BROWN)),
			GridEntry.colored(InkColors.ORANGE.getColorVec(), "spectrum.ink.color.spectrum.orange", (screen) -> chooseColor(InkColors.ORANGE))
	);
	
	public static final QuickNavigationGridScreen.Grid BLACK_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.BLACK.getColorVec(), "spectrum.ink.color.spectrum.black", (screen) -> chooseColor(InkColors.BLACK)),
			GridEntry.colored(InkColors.WHITE.getColorVec(), "spectrum.ink.color.spectrum.white", (screen) -> chooseColor(InkColors.WHITE)),
			GridEntry.colored(InkColors.GRAY.getColorVec(), "spectrum.ink.color.spectrum.gray", (screen) -> chooseColor(InkColors.GRAY)),
			GridEntry.BACK,
			GridEntry.colored(InkColors.LIGHT_GRAY.getColorVec(), "spectrum.ink.color.spectrum.light_gray", (screen) -> chooseColor(InkColors.LIGHT_GRAY))
	);
	
	public PaintbrushScreen(PaintbrushScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		gridStack.push(new QuickNavigationGridScreen.Grid(
				GridEntry.CLOSE,
				handler.hasAccessToWhites() ? GridEntry.colored(InkColors.BLACK.getColorVec(), "spectrum.ink.color.group_blacks", (screen) -> selectGrid(BLACK_GRID)) : GridEntry.colored(InkColors.BLACK.getColorVec(), "spectrum.ink.color.spectrum.black", (screen) -> chooseColor(InkColors.BLACK)),
				GridEntry.colored(InkColors.MAGENTA.getColorVec(), "spectrum.ink.color.group_magentas", (screen) -> selectGrid(MAGENTA_GRID)),
				GridEntry.colored(InkColors.CYAN.getColorVec(), "spectrum.ink.color.group_cyans", (screen) -> selectGrid(CYAN_GRID)),
				GridEntry.colored(InkColors.YELLOW.getColorVec(), "spectrum.ink.color.group_yellows", (screen) -> selectGrid(YELLOW_GRID))
		));
	}
	
	protected static void chooseColor(@Nullable InkColor inkColor) {
		SpectrumC2SPacketSender.sendInkColorSelectedInGUI(inkColor);
		MinecraftClient client = MinecraftClient.getInstance();
		client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.NEUTRAL, 0.6F, 1.0F);
		client.player.closeHandledScreen();
	}
	
}