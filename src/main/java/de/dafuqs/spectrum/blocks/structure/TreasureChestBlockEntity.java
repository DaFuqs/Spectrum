package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TreasureChestBlockEntity extends SpectrumChestBlockEntity {
	
	private final List<UUID> playersThatOpenedAlready = new ArrayList<>();
	private ResourceLocation requiredAdvancementIdentifierToOpen;
	private Vec3i controllerOffset;
	
	public TreasureChestBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_CHEST, pos, state);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		
		if (this.requiredAdvancementIdentifierToOpen != null) {
			tag.putString("RequiredAdvancement", this.requiredAdvancementIdentifierToOpen.toString());
		}
		
		if (this.controllerOffset != null) {
			tag.putInt("ControllerOffsetX", this.controllerOffset.getX());
			tag.putInt("ControllerOffsetY", this.controllerOffset.getY());
			tag.putInt("ControllerOffsetZ", this.controllerOffset.getZ());
		}
		
		if (!playersThatOpenedAlready.isEmpty()) {
			ListTag uuidList = new ListTag();
			for (UUID uuid : playersThatOpenedAlready) {
				CompoundTag nbtCompound = new CompoundTag();
				nbtCompound.putUUID("UUID", uuid);
				uuidList.add(nbtCompound);
			}
			tag.put("OpenedPlayers", uuidList);
		}
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.preservation_chest");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, ScreenBackgroundVariant.LATEGAME);
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		if (tag.contains("RequiredAdvancement", Tag.TAG_STRING)) {
			this.requiredAdvancementIdentifierToOpen = ResourceLocation.tryParse(tag.getString("RequiredAdvancement"));
		}
		
		if (tag.contains("ControllerOffsetX")) {
			this.controllerOffset = new Vec3i(tag.getInt("ControllerOffsetX"), tag.getInt("ControllerOffsetY"), tag.getInt("ControllerOffsetZ"));
		}
		
		this.playersThatOpenedAlready.clear();
		if (tag.contains("OpenedPlayers", Tag.TAG_LIST)) {
			ListTag list = tag.getList("OpenedPlayers", Tag.TAG_COMPOUND);
			for (int i = 0; i < list.size(); i++) {
				CompoundTag compound = list.getCompound(i);
				UUID uuid = compound.getUUID("UUID");
				this.playersThatOpenedAlready.add(uuid);
			}
		}
	}
	
	@Override
	public void onCloseSpectrum() {
		if (level instanceof ServerLevel serverWorld && controllerOffset != null) {
			BlockEntity blockEntity = serverWorld.getBlockEntity(Support.directionalOffset(this.worldPosition, this.controllerOffset, serverWorld.getBlockState(this.worldPosition).getValue(PreservationControllerBlock.FACING)));
			if (blockEntity instanceof PreservationControllerBlockEntity controller) {
				controller.openExit();
			}
		}
	}
	
	// Generate new loot for each player that has never opened this chest before
	@Override
	public void unpackLootTable(@Nullable Player player) {
		if (player != null && this.lootTable != null && this.getLevel() != null && !hasOpenedThisChestBefore(player)) {
			supplyInventory(player);
			rememberPlayer(player);
		}
	}
	
	public boolean hasOpenedThisChestBefore(@NotNull Player player) {
		return this.playersThatOpenedAlready.contains(player.getUUID());
	}
	
	public void rememberPlayer(@NotNull Player player) {
		this.playersThatOpenedAlready.add(player.getUUID());
		this.setChanged();
	}
	
	public void supplyInventory(@NotNull Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			LootTable lootTable = serverPlayer.serverLevel().getServer().reloadableRegistries().getLootTable(this.lootTable);
			var builder = new LootParams.Builder(serverPlayer.serverLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition));
			builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
			lootTable.fill(this, builder.create(LootContextParamSets.CHEST), lootTableSeed);
			if (player instanceof ServerPlayer) {
				CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.lootTable);
			}
		}
	}
	
	public boolean canOpen(Player player) {
		if (this.requiredAdvancementIdentifierToOpen == null) {
			return true;
		} else {
			return AdvancementHelper.hasAdvancement(player, this.requiredAdvancementIdentifierToOpen);
		}
	}
	
	@Override
	public int getContainerSize() {
		return 27;
	}
	
}
