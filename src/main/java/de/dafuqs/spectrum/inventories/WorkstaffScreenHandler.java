package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.items.tools.WorkstaffItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WorkstaffScreenHandler extends QuickNavigationGridScreenHandler {
	
	private final PlayerEntity player;
	private final ItemStack workstaffStack;
	
	public WorkstaffScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ItemStack.EMPTY);
	}
	
	public WorkstaffScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack workstaffStack) {
		super(SpectrumScreenHandlerTypes.WORKSTAFF, syncId);
		this.workstaffStack = workstaffStack;
		this.player = playerInventory.player;
	}

    public boolean canUse(PlayerEntity player) {
        for (ItemStack itemStack : player.getHandItems()) {
            if (itemStack == workstaffStack) {
                return true;
            }
        }
        return false;
    }

    public void onWorkstaffToggleSelectionPacket(@Nullable WorkstaffItem.GUIToggle toggle) {
        WorkstaffItem.applyToggle(player, workstaffStack, toggle);
    }

}
