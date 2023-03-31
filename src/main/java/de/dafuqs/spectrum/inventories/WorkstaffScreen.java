package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class WorkstaffScreen extends QuickNavigationGridScreen<WorkstaffScreenHandler> {

	private static final Grid RANGE_GRID = new Grid(
			GridEntry.EMPTY,
			GridEntry.of(InkColors.GRAY.getColor(), new Point(0, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_1x1)),
			GridEntry.of(InkColors.GRAY.getColor(), new Point(32, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_5x5)),
			GridEntry.BACK,
			GridEntry.of(InkColors.GRAY.getColor(), new Point(16, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_3x3))
	);

	private static final Grid ENCHANTMENT_GRID = new Grid(
			GridEntry.EMPTY,
			GridEntry.of(InkColors.GRAY.getColor(), new Point(48, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_SILK_TOUCH)),
			GridEntry.BACK,
			GridEntry.of(InkColors.GRAY.getColor(), new Point(64, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_RESONANCE)),
			GridEntry.of(InkColors.GRAY.getColor(), new Point(80, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_FORTUNE))
	);

	public WorkstaffScreen(WorkstaffScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);

		GridEntry rightClickGridEntry;
		ItemStack mainHandStack = playerInventory.player.getMainHandStack();
		if (WorkstaffItem.canTill(mainHandStack.getNbt())) {
			rightClickGridEntry = GridEntry.of(InkColors.GRAY.getColor(), new Point(144, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.DISABLE_RIGHT_CLICK_ACTIONS));
		} else {
			rightClickGridEntry = GridEntry.of(InkColors.GRAY.getColor(), new Point(128, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.ENABLE_RIGHT_CLICK_ACTIONS));
		}
		
		if (mainHandStack.getItem() instanceof GlassCrestWorkstaffItem) {
			
			GridEntry projectileEntry = GlassCrestWorkstaffItem.canShoot(mainHandStack.getNbt())
					? GridEntry.of(InkColors.GRAY.getColor(), new Point(176, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.DISABLE_PROJECTILES))
					: GridEntry.of(InkColors.GRAY.getColor(), new Point(160, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.ENABLE_PROJECTILES));
			
			gridStack.push(new Grid(
					GridEntry.CLOSE,
					GridEntry.of(InkColors.GRAY.getColor(), new Point(112, 38), (screen) -> screen.selectGrid(RANGE_GRID)),
					rightClickGridEntry,
					projectileEntry,
					GridEntry.of(InkColors.GRAY.getColor(), new Point(48, 38), (screen) -> screen.selectGrid(ENCHANTMENT_GRID))
			));

		} else {

			GridEntry enchantmentEntry = EnchantmentHelper.getLevel(Enchantments.FORTUNE, mainHandStack) > 0
					? GridEntry.of(InkColors.GRAY.getColor(), new Point(48, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_SILK_TOUCH))
					: GridEntry.of(InkColors.GRAY.getColor(), new Point(160, 38), (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_FORTUNE));

			gridStack.push(new Grid(
					GridEntry.CLOSE,
					GridEntry.of(InkColors.GRAY.getColor(), new Point(112, 38), (screen) -> screen.selectGrid(RANGE_GRID)),
					rightClickGridEntry,
					GridEntry.EMPTY,
					enchantmentEntry
			));

		}

	}

	protected static void select(WorkstaffItem.GUIToggle toggle) {
		SpectrumC2SPacketSender.sendWorkstaffToggle(toggle);
		MinecraftClient client = MinecraftClient.getInstance();
		client.world.playSound(client.player.getBlockPos(), SpectrumSoundEvents.PAINTBRUSH_SELECT, SoundCategory.NEUTRAL, 0.6F, 1.0F, false);
		client.player.closeHandledScreen();
	}
	
}