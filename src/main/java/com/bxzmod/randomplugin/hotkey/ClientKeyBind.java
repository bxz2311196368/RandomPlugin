package com.bxzmod.randomplugin.hotkey;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.function.Supplier;

public class ClientKeyBind
{
	public static final KeyBinding CHAIN_MINE =
			new ModKeyBinding(HotKeys.CHAIN_MINE, ChainMiningManager::onClientPress);

	//public static final KeyBinding SHAPE_MINE = new ModKeyBinding(HotKeys.SHAPE_MINE);

	public static void registerKey()
	{
		for (KeyBinding keyBinding : KeyHandler.KEY_BINDINGS.values())
			ClientRegistry.registerKeyBinding(keyBinding);
	}

	public static class ModKeyBinding extends KeyBinding
	{
		private final HotKeys.ModHotKey properties;
		private boolean prePress = false;
		private final Supplier<NBTTagCompound> pressAction;

		public ModKeyBinding(HotKeys.ModHotKey properties, Supplier<NBTTagCompound> pressAction)
		{
			super(setKeyName(properties.name), properties.keyCode, properties.category);
			this.properties = properties;
			this.pressAction = pressAction;
			KeyHandler.KEY_BINDINGS.put(this.getName(), this);
		}

		private static String setKeyName(String name)
		{
			return ModInfo.MODID + ".key." + name + ".name";
		}

		public String getName()
		{
			return this.properties.name;
		}

		public void addPressData(NBTTagCompound compound)
		{
			compound.setBoolean(this.getName(), this.isKeyDown());
		}

		public void onPress()
		{
			NBTTagCompound compound = this.pressAction.get();
			if (compound.isEmpty())
				return;
			KeyHandler.addData(this.getName(), compound);
		}

		public boolean onTick()
		{
			if (this.prePress != this.isKeyDown())
			{
				this.prePress = this.isKeyDown();
				return true;
			}
			return false;
		}
	}
}
