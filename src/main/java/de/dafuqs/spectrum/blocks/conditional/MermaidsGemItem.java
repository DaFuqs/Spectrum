package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MermaidsGemItem extends ItemNameBlockItem implements RevelationAware {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	
	public MermaidsGemItem(Block block, Properties settings) {
		super(block, settings);
		RevelationAware.register(this);
	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		return Map.of();
	}
	
	@Override
	public Tuple<Item, Item> getItemCloak() {
		return new Tuple<>(this, Items.KELP);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockstate = level.getBlockState(pos);
		if(blockstate.is(Blocks.CAULDRON)) {
			if(!level.isClientSide()) {
				level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MAX_FILL_LEVEL));
				context.getItemInHand().shrink(1);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		
		return super.useOn(context);
	}
	
	@Override
	public boolean overrideStackedOnOther(@NotNull ItemStack gemStack, @NotNull Slot slot, @NotNull ClickAction clickType, @NotNull Player player) {
		if (clickType != ClickAction.SECONDARY) {
			return false;
		}
		
		ItemStack slotStack = slot.getItem();
		IFluidHandlerItem fluidHandler = slotStack.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler != null) {
			if(fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE) > 0) {
				slot.set(fluidHandler.getContainer());
				gemStack.shrink(1);
			}
		}
		
		return true;
	}
	
}
