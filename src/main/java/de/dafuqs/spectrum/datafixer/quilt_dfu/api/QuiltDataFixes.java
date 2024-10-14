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

package de.dafuqs.spectrum.datafixer.quilt_dfu.api;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import de.dafuqs.spectrum.datafixer.quilt_dfu.impl.QuiltDataFixesInternals;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Provides methods to register custom {@link DataFixer}s.
 */
public final class QuiltDataFixes {
	/**
	 * A "base" version {@code 0} schema, for use by all mods.
	 * <p>
	 * This schema <em>must</em> be the first one added!
	 *
	 * @see DataFixerBuilder#addSchema(int, BiFunction)
	 */
	public static final BiFunction<Integer, Schema, Schema> BASE_SCHEMA = (version, parent) -> {
		checkArgument(version == 0, "version must be 0");
		checkArgument(parent == null, "parent must be null");
		return QuiltDataFixesInternals.get().createBaseSchema();
	};

	/**
	 * Registers a new data fixer.
	 *
	 * @param modId          the mod identifier
	 * @param currentVersion the current version of the mod's data
	 * @param dataFixer      the data fixer
	 */
	public static void registerFixer(@NotNull String modId, @Range(from = 0, to = Integer.MAX_VALUE) int currentVersion, @NotNull DataFixer dataFixer) {
		requireNonNull(modId, "modId cannot be null");
		//noinspection ConstantConditions
		checkArgument(currentVersion >= 0, "currentVersion must be positive");
		requireNonNull(dataFixer, "dataFixer cannot be null");

		if (isFrozen()) {
			throw new IllegalStateException("Can't register data fixer after registry is frozen");
		}

		QuiltDataFixesInternals.get().registerFixer(modId, currentVersion, dataFixer);
	}

	/**
	 * Checks if the data fixer registry is frozen.
	 *
	 * @return {@code true} if frozen, or {@code false} otherwise.
	 */
	@Contract(pure = true)
	public static boolean isFrozen() {
		return QuiltDataFixesInternals.get().isFrozen();
	}
}
