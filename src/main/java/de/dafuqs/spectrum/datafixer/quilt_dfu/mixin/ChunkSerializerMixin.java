/*
 * Copyright 2022 The Quilt Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.dafuqs.spectrum.datafixer.quilt_dfu.mixin;

import de.dafuqs.spectrum.datafixer.quilt_dfu.impl.QuiltDataFixesInternals;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.ChunkSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {
	@ModifyVariable(
			method = "serialize",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putInt(Ljava/lang/String;I)V", ordinal = 0)
	)
	private static NbtCompound addModDataVersions(NbtCompound compound) {
		return QuiltDataFixesInternals.get().addModDataVersions(compound);
	}
}
