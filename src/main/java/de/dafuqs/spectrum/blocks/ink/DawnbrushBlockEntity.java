package de.dafuqs.spectrum.blocks.ink;

import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class DawnbrushBlockEntity extends InkGeneratorBlockEntity {
	
	public DawnbrushBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.DAWNBRUSH.get(), blockPos, blockState, 3);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.dawnbrush");
	}
	
	@Override
	protected boolean tickLogic(Level level) {
		return false;
	}
	
}
