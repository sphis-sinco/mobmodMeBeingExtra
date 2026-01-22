package net.minecraft.client;

final class EnumOSMappingHelper {
	static final int[] osValues = new int[EnumOS.values().length];

	static {
		try {
			osValues[EnumOS.a.ordinal()] = 1;
		} catch (NoSuchFieldError var3) {
		}

		try {
			osValues[EnumOS.b.ordinal()] = 2;
		} catch (NoSuchFieldError var2) {
		}

		try {
			osValues[EnumOS.c.ordinal()] = 3;
		} catch (NoSuchFieldError var1) {
		}

		try {
			osValues[EnumOS.d.ordinal()] = 4;
		} catch (NoSuchFieldError var0) {
		}
	}
}
