package de.dafuqs.spectrum.accessor;

public interface WorldRendererAccessor {

	/**
	 * Schedules a chunk rebuild
	 * @param x		 X coordinates of the chunk.
	 * @param y		 Y coordinates of the chunk.
	 * @param z		 Z coordinates of the chunk.
	 * @param important True if important, else false.
	 */
	void scheduleChunkRenderPublic(int x, int y, int z, boolean important);

	void rebuildAllChunks();
}