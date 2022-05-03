package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.CMYKColor;
import de.dafuqs.spectrum.energy.color.PigmentColors;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArtistsPaletteEnergyStorage extends IndividualAndTotalCappedElementalPigmentEnergyStorage {
	
	public ArtistsPaletteEnergyStorage(long maxEnergyTotal, long maxEnergyPerColor) {
		super(maxEnergyTotal, maxEnergyPerColor);
	}
	
	public ArtistsPaletteEnergyStorage(long maxEnergyTotal, long maxEnergyPerColor, long cyan, long magenta, long yellow, long black, long white) {
		super(maxEnergyTotal, maxEnergyPerColor, cyan, magenta, yellow, black, white);
	}
	
	public long addEnergy(CMYKColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		long leftoverEnergy = super.addEnergy(color, amount);
		if(leftoverEnergy != amount) {
			SpectrumAdvancementCriteria.PIGMENT_PALETTE_USE.trigger(serverPlayerEntity, stack, this, amount - leftoverEnergy);
		}
		return leftoverEnergy;
	}
	
	public boolean requestEnergy(CMYKColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		boolean success = super.requestEnergy(color, amount);
		if(success) {
			SpectrumAdvancementCriteria.PIGMENT_PALETTE_USE.trigger(serverPlayerEntity, stack, this, -amount);
		}
		return success;
	}
	
	public long drainEnergy(CMYKColor color, long amount, ItemStack stack, ServerPlayerEntity serverPlayerEntity) {
		long drainedAmount = super.drainEnergy(color, amount);
		if(drainedAmount != 0) {
			SpectrumAdvancementCriteria.PIGMENT_PALETTE_USE.trigger(serverPlayerEntity, stack, this, -drainedAmount);
		}
		return drainedAmount;
	}
	
	public static @Nullable ArtistsPaletteEnergyStorage fromNbt(@NotNull NbtCompound compound) {
		if(compound.contains("MaxEnergyTotal", NbtElement.LONG_TYPE)) {
			long maxEnergyTotal = compound.getLong("MaxEnergyTotal");
			long maxEnergyPerColor = compound.getLong("MaxEnergyPerColor");
			long cyan = compound.getLong("Cyan");
			long magenta = compound.getLong("Magenta");
			long yellow = compound.getLong("Yellow");
			long black = compound.getLong("Black");
			long white = compound.getLong("White");
			return new ArtistsPaletteEnergyStorage(maxEnergyTotal, maxEnergyPerColor, cyan, magenta, yellow, black, white);
		}
		return null;
	}
	
	@Environment(EnvType.CLIENT)
	public void addTooltip(World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.cyan", this.storedEnergy.get(PigmentColors.CYAN)));
		tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.magenta", this.storedEnergy.get(PigmentColors.MAGENTA)));
		tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.yellow", this.storedEnergy.get(PigmentColors.YELLOW)));
		
		PlayerEntity player = MinecraftClient.getInstance().player;
		if(player != null) {
			if(Support.hasAdvancement(player, new Identifier(SpectrumCommon.MOD_ID, "create_onyx_shard"))) {
				tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.black", this.storedEnergy.get(PigmentColors.BLACK)));
			}
			if(Support.hasAdvancement(player, new Identifier(SpectrumCommon.MOD_ID, "midgame/collect_moonstone_shard"))) {
				tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip.stored_energy.white", this.storedEnergy.get(PigmentColors.WHITE)));
			}
		}
	}
}
