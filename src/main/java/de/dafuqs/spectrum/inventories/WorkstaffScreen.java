package de.dafuqs.spectrum.inventories;

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

@Environment(EnvType.CLIENT)
public class WorkstaffScreen extends QuickNavigationGridScreen<WorkstaffScreenHandler> {

	private static final Grid RANGE_GRID = new Grid(
			GridEntry.EMPTY,
			GridEntry.text(Text.literal("1x1"), "item.spectrum.workstaff.gui.1x1", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_1x1)),
			GridEntry.text(Text.literal("5x5"), "item.spectrum.workstaff.gui.5x5", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_5x5)),
			GridEntry.BACK,
			GridEntry.text(Text.literal("3x3"), "item.spectrum.workstaff.gui.3x3", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_3x3))
	);

	private static final Grid ENCHANTMENT_GRID = new Grid(
			GridEntry.EMPTY,
			GridEntry.item(Items.FEATHER, "item.spectrum.workstaff.gui.silk_touch", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_SILK_TOUCH)),
			GridEntry.BACK,
			GridEntry.item(SpectrumItems.RESONANCE_SHARD, "item.spectrum.workstaff.gui.resonance", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_RESONANCE)),
			GridEntry.item(SpectrumBlocks.FOUR_LEAF_CLOVER.asItem(), "item.spectrum.workstaff.gui.fortune", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_FORTUNE))
	);

	public WorkstaffScreen(WorkstaffScreenHandler handler, PlayerInventory playerInventory, Text title) {
		super(handler, playerInventory, title);

		GridEntry rightClickGridEntry;
		ItemStack mainHandStack = playerInventory.player.getMainHandStack();
		if (WorkstaffItem.canTill(mainHandStack.getNbt())) {
			rightClickGridEntry = GridEntry.item(Items.WOODEN_HOE, "item.spectrum.workstaff.gui.disable_right_click_actions", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.DISABLE_RIGHT_CLICK_ACTIONS));
		} else {
			rightClickGridEntry = GridEntry.item(SpectrumItems.MULTITOOL, "item.spectrum.workstaff.gui.enable_right_click_actions", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.ENABLE_RIGHT_CLICK_ACTIONS));
		}
		
		if (mainHandStack.getItem() instanceof GlassCrestWorkstaffItem) {
			GridEntry projectileEntry = GlassCrestWorkstaffItem.canShoot(mainHandStack.getNbt())
					? GridEntry.item(Items.SPECTRAL_ARROW, "item.spectrum.workstaff.gui.disable_projectiles", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.DISABLE_PROJECTILES))
					: GridEntry.item(Items.ARROW, "item.spectrum.workstaff.gui.enable_projectiles", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.ENABLE_PROJECTILES));
			
			gridStack.push(new Grid(
					GridEntry.CLOSE,
					GridEntry.item(Items.STONE, "item.spectrum.workstaff.gui.range_group", (screen) -> selectGrid(RANGE_GRID)),
					rightClickGridEntry,
					projectileEntry,
					GridEntry.item(Items.ENCHANTED_BOOK, "item.spectrum.workstaff.gui.enchantment_group", (screen) -> screen.selectGrid(ENCHANTMENT_GRID))
			));
		} else {
			GridEntry enchantmentEntry = EnchantmentHelper.getLevel(Enchantments.FORTUNE, mainHandStack) > 0
					? GridEntry.item(Items.FEATHER, "item.spectrum.workstaff.gui.silk_touch", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_SILK_TOUCH))
					: GridEntry.item(SpectrumBlocks.FOUR_LEAF_CLOVER.asItem(), "item.spectrum.workstaff.gui.fortune", (screen) -> WorkstaffScreen.select(WorkstaffItem.GUIToggle.SELECT_FORTUNE));

			gridStack.push(new Grid(
					GridEntry.CLOSE,
					GridEntry.item(Items.STONE, "item.spectrum.workstaff.gui.range_group", (screen) -> screen.selectGrid(RANGE_GRID)),
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