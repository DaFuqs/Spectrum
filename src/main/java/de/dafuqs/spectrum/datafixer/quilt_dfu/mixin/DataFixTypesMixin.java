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

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.dafuqs.spectrum.datafixer.quilt_dfu.impl.QuiltDataFixesInternals;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DataFixTypes.class)
public abstract class DataFixTypesMixin {
	@ModifyReturnValue(
			method = "update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;II)Lnet/minecraft/nbt/NbtCompound;",
			at = @At("RETURN")
	)
	private NbtCompound updateDataWithFixers(NbtCompound original) {
		return QuiltDataFixesInternals.get().updateWithAllFixers((DataFixTypes) (Object) this, original);
	}
}
