package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class PaintbrushScreen extends QuickNavigationGridScreen<PaintbrushScreenHandler> {
	
	public static final QuickNavigationGridScreen.Grid MAGENTA_GRID = new QuickNavigationGridScreen.Grid(
			QuickNavigationGridScreen.GridEntry.of(InkColors.MAGENTA.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.MAGENTA)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.RED.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.RED)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.PINK.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.PINK)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.BROWN.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.BROWN)),
			GridEntry.BACK
			);
	
	public static final QuickNavigationGridScreen.Grid CYAN_GRID = new QuickNavigationGridScreen.Grid(
			QuickNavigationGridScreen.GridEntry.of(InkColors.CYAN.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.CYAN)),
			GridEntry.BACK,
			QuickNavigationGridScreen.GridEntry.of(InkColors.LIGHT_BLUE.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.LIGHT_BLUE)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.BLUE.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.BLUE)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.PURPLE.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.PURPLE))
	);
	
	public static final QuickNavigationGridScreen.Grid YELLOW_GRID = new QuickNavigationGridScreen.Grid(
			QuickNavigationGridScreen.GridEntry.of(InkColors.YELLOW.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.YELLOW)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.ORANGE.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.ORANGE)),
			GridEntry.BACK,
			QuickNavigationGridScreen.GridEntry.of(InkColors.LIME.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.LIME)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.GREEN.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.GREEN))
	);
	
	public static final QuickNavigationGridScreen.Grid BLACK_GRID = new QuickNavigationGridScreen.Grid(
			QuickNavigationGridScreen.GridEntry.of(InkColors.BLACK.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.BLACK)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.WHITE.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.WHITE)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.LIGHT_GRAY.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.LIGHT_GRAY)),
			GridEntry.BACK,
			QuickNavigationGridScreen.GridEntry.of(InkColors.GRAY.getColor(), null, (screen) -> PaintbrushScreen.chooseColor(InkColors.GRAY))
	);
	
	public static final QuickNavigationGridScreen.Grid MAIN_GRID = new QuickNavigationGridScreen.Grid(
			GridEntry.CLOSE,
			QuickNavigationGridScreen.GridEntry.of(InkColors.BLACK.getColor(), null, (screen) -> {
				if(((PaintbrushScreenHandler) screen.getScreenHandler()).hasAccessToWhites()) {
					screen.selectGrid(BLACK_GRID);
				} else {
					PaintbrushScreen.chooseColor(InkColors.BLACK);
				}
			}),
			QuickNavigationGridScreen.GridEntry.of(InkColors.MAGENTA.getColor(), null, (screen) -> screen.selectGrid(MAGENTA_GRID)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.CYAN.getColor(), null, (screen) -> screen.selectGrid(CYAN_GRID)),
			QuickNavigationGridScreen.GridEntry.of(InkColors.YELLOW.getColor(), null, (screen) -> screen.selectGrid(YELLOW_GRID))
	);
	
	public PaintbrushScreen(PaintbrushScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);
		gridStack.push(MAIN_GRID);
	}
	
	protected static void chooseColor(@Nullable InkColor inkColor) {
		SpectrumC2SPacketSender.sendInkColorSelectedInGUI(inkColor);
		MinecraftClient client = MinecraftClient.getInstance();
		client.world.playSound(null, client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_PAINT, SoundCategory.NEUTRAL, 0.6F, 1.0F);
		client.player.closeHandledScreen();
	}
	
}