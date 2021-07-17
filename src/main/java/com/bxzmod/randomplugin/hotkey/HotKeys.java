package com.bxzmod.randomplugin.hotkey;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import java.util.UUID;
import java.util.function.BiConsumer;

public class HotKeys
{

	public static final String KEY_CATEGORY = ModInfo.MODID + ".key." + "category";
	//	public static final ModHotKey SHAPE_MINE = new ModHotKey(ShapeMiningManager.SHAPE_MINE, Keyboard.KEY_Z,
	//					KEY_CATEGORY, ClientKeyHandler::progressShapeMine, KeyHandler::progressShapeMine);

	public static final ModHotKey CHAIN_MINE =
			new ModHotKey(ChainMiningManager.CHAIN_MINE, Keyboard.KEY_GRAVE, KEY_CATEGORY,
					ChainMiningManager::setPlayerKeyFlag, ChainMiningManager::onServerDataPacket);

	public static class ModHotKey
	{
		public final String name;
		public final int keyCode;
		public final String category;
		private final BiConsumer<UUID, Boolean> onPress;
		private final BiConsumer<UUID, NBTTagCompound> onSync;

		public ModHotKey(String name, int keyCode, String category, BiConsumer<UUID, Boolean> onPress,
				BiConsumer<UUID, NBTTagCompound> onSync)
		{
			this.name = name;
			this.keyCode = keyCode;
			this.category = category;
			this.onPress = onPress;
			this.onSync = onSync;
			KeyHandler.HOT_KEY_HASH_MAP.put(this.name, this);
		}

		public void onKeyPressPacket(UUID uuid, boolean press)
		{
			this.onPress.accept(uuid, press);
		}

		public void onServerDataPacket(UUID uuid, NBTTagCompound compound)
		{
			this.onSync.accept(uuid, compound);
		}

	}
}
