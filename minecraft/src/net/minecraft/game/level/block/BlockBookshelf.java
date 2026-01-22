package net.minecraft.game.level.block;

import java.util.Random;

public final class BlockBookshelf extends Block {
	public BlockBookshelf(int var1, int var2) {
		super(47, 35);
	}

	public final int getBlockTexture(int var1) {
		return var1 <= 1 ? 4 : this.blockIndexInTexture;
	}

	public final int quantityDropped(Random var1) {
		return 0;
	}
}
