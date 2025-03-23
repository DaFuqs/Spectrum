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
			GridEntry.colored(InkColors.MAGENTA.getColorVec(), InkColors.MAGENTA.getColoredName(), (screen) -> chooseColor(InkColors.MAGENTA)),
			GridEntry.colored(InkColors.PINK.getColorVec(), InkColors.PINK.getColoredName(), (screen) -> chooseColor(InkColors.PINK)),
			GridEntry.colored(InkColors.RED.getColorVec(), InkColors.RED.getColoredName(), (screen) -> chooseColor(InkColors.RED)),
			GridEntry.colored(InkColors.PURPLE.getColorVec(), InkColors.PURPLE.getColoredName(), (screen) -> chooseColor(InkColors.PURPLE)),
			GridEntry.BACK
	);
	
	public static final QuickNavigationGridScreen.Grid CYAN_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.CYAN.getColorVec(), InkColors.CYAN.getColoredName(), (screen) -> chooseColor(InkColors.CYAN)),
			GridEntry.BACK,
			GridEntry.colored(InkColors.LIGHT_BLUE.getColorVec(), InkColors.LIGHT_BLUE.getColoredName(), (screen) -> chooseColor(InkColors.LIGHT_BLUE)),
			GridEntry.colored(InkColors.BLUE.getColorVec(), InkColors.BLUE.getColoredName(), (screen) -> chooseColor(InkColors.BLUE)),
			GridEntry.colored(InkColors.LIME.getColorVec(), InkColors.LIME.getColoredName(), (screen) -> chooseColor(InkColors.LIME))
	);
	
	public static final QuickNavigationGridScreen.Grid YELLOW_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.YELLOW.getColorVec(), InkColors.YELLOW.getColoredName(), (screen) -> chooseColor(InkColors.YELLOW)),
			GridEntry.colored(InkColors.GREEN.getColorVec(), InkColors.GREEN.getColoredName(), (screen) -> chooseColor(InkColors.GREEN)),
			GridEntry.BACK,
			GridEntry.colored(InkColors.BROWN.getColorVec(), InkColors.BROWN.getColoredName(), (screen) -> chooseColor(InkColors.BROWN)),
			GridEntry.colored(InkColors.ORANGE.getColorVec(), InkColors.ORANGE.getColoredName(), (screen) -> chooseColor(InkColors.ORANGE))
	);
	
	public static final QuickNavigationGridScreen.Grid BLACK_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.colored(InkColors.BLACK.getColorVec(), InkColors.BLACK.getColoredName(), (screen) -> chooseColor(InkColors.BLACK)),
			GridEntry.colored(InkColors.WHITE.getColorVec(), InkColors.WHITE.getColoredName(), (screen) -> chooseColor(InkColors.WHITE)),
			GridEntry.colored(InkColors.GRAY.getColorVec(), InkColors.GRAY.getColoredName(), (screen) -> chooseColor(InkColors.GRAY)),
			GridEntry.BACK,
			GridEntry.colored(InkColors.LIGHT_GRAY.getColorVec(), InkColors.LIGHT_GRAY.getColoredName(), (screen) -> chooseColor(InkColors.LIGHT_GRAY))
	);
	
	public PaintbrushScreen(PaintbrushScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		gridStack.push(new QuickNavigationGridScreen.Grid(
				GridEntry.CLOSE,
				handler.hasAccessToWhites() ? GridEntry.colored(InkColors.BLACK.getColorVec(), Text.translatable("ink.group.spectrum.blacks"), (screen) -> selectGrid(BLACK_GRID)) : GridEntry.colored(InkColors.BLACK.getColorVec(), Text.translatable("ink.spectrum.black.name"), (screen) -> chooseColor(InkColors.BLACK)),
				GridEntry.colored(InkColors.MAGENTA.getColorVec(), Text.translatable("ink.group.spectrum.magentas"), (screen) -> selectGrid(MAGENTA_GRID)),
				GridEntry.colored(InkColors.CYAN.getColorVec(), Text.translatable("ink.group.spectrum.cyans"), (screen) -> selectGrid(CYAN_GRID)),
				GridEntry.colored(InkColors.YELLOW.getColorVec(), Text.translatable("ink.group.spectrum.yellows"), (screen) -> selectGrid(YELLOW_GRID))
		));
	}
	
	protected static void chooseColor(@Nullable InkColor inkColor) {
		SpectrumC2SPacketSender.sendInkColorSelectedInGUI(inkColor);
		MinecraftClient client = MinecraftClient.getInstance();
		client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.NEUTRAL, 0.6F, 1.0F);
		client.player.closeHandledScreen();
	}
	
}