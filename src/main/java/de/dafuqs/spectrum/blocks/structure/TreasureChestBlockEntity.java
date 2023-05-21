package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.blocks.chests.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.loot.*;
import net.minecraft.loot.context.*;
import net.minecraft.nbt.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TreasureChestBlockEntity extends SpectrumChestBlockEntity {
	
	private final List<UUID> playersThatOpenedAlready = new ArrayList<>();
	private Identifier requiredAdvancementIdentifierToOpen;
	private Vec3i controllerOffset;
	
	public TreasureChestBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.TREASURE_CHEST, pos, state);
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		
		if (this.requiredAdvancementIdentifierToOpen != null) {
			tag.putString("RequiredAdvancement", this.requiredAdvancementIdentifierToOpen.toString());
		}
		
		if (this.controllerOffset != null) {
			tag.putInt("ControllerOffsetX", this.controllerOffset.getX());
			tag.putInt("ControllerOffsetY", this.controllerOffset.getY());
			tag.putInt("ControllerOffsetZ", this.controllerOffset.getZ());
		}
		
		if (!playersThatOpenedAlready.isEmpty()) {
			NbtList uuidList = new NbtList();
			for (UUID uuid : playersThatOpenedAlready) {
				NbtCompound nbtCompound = new NbtCompound();
				nbtCompound.putUuid("UUID", uuid);
				uuidList.add(nbtCompound);
			}
			tag.put("OpenedPlayers", uuidList);
		}
	}
	
	@Override
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.treasure_chest");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return GenericSpectrumContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this, ProgressionStage.LATEGAME);
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		
		if (tag.contains("RequiredAdvancement", NbtElement.STRING_TYPE)) {
			this.requiredAdvancementIdentifierToOpen = Identifier.tryParse(tag.getString("RequiredAdvancement"));
		}
		
		if (tag.contains("ControllerOffsetX")) {
			this.controllerOffset = new Vec3i(tag.getInt("ControllerOffsetX"), tag.getInt("ControllerOffsetY"), tag.getInt("ControllerOffsetZ"));
		}
		
		this.playersThatOpenedAlready.clear();
		if (tag.contains("OpenedPlayers", NbtElement.LIST_TYPE)) {
			NbtList list = tag.getList("OpenedPlayers", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < list.size(); i++) {
				NbtCompound compound = list.getCompound(i);
				UUID uuid = compound.getUuid("UUID");
				this.playersThatOpenedAlready.add(uuid);
			}
		}
	}
	
	@Override
	public void onClose() {
		if (!world.isClient && controllerOffset != null) {
			BlockEntity blockEntity = world.getBlockEntity(Support.directionalOffset(this.pos, this.controllerOffset, world.getBlockState(this.pos).get(PreservationControllerBlock.FACING)));
			if (blockEntity instanceof PreservationControllerBlockEntity controller) {
				controller.openExit();
			}
		}
	}
	
	// Generate new loot for each player that has never opened this chest before
	@Override
	public void checkLootInteraction(@Nullable PlayerEntity player) {
		if (player != null && this.lootTableId != null && this.world != null && !hasOpenedThisChestBefore(player)) {
			supplyInventory(player);
			rememberPlayer(player);
		}
	}
	
	public boolean hasOpenedThisChestBefore(@NotNull PlayerEntity player) {
		return this.playersThatOpenedAlready.contains(player.getUuid());
	}
	
	public void rememberPlayer(@NotNull PlayerEntity player) {
		this.playersThatOpenedAlready.add(player.getUuid());
		this.markDirty();
	}
	
	public void supplyInventory(@NotNull PlayerEntity player) {
		LootTable lootTable = this.world.getServer().getLootManager().getTable(this.lootTableId);
		LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world)).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos)).random(this.lootTableSeed);
		builder.luck(player.getLuck()).parameter(LootContextParameters.THIS_ENTITY, player);
		lootTable.supplyInventory(this, builder.build(LootContextTypes.CHEST));
		
		if (player instanceof ServerPlayerEntity) {
			Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger((ServerPlayerEntity) player, this.lootTableId);
		}
	}
	
	public boolean canOpen(PlayerEntity player) {
		if (this.requiredAdvancementIdentifierToOpen == null) {
			return true;
		} else {
			return AdvancementHelper.hasAdvancement(player, this.requiredAdvancementIdentifierToOpen);
		}
	}
	
	@Override
	public int size() {
		return 27;
	}
	
}
