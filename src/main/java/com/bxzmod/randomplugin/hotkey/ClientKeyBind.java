package com.bxzmod.randomplugin.hotkey;

import com.bxzmod.randomplugin.Info;
import com.bxzmod.randomplugin.utils.chainmining.ChainMiningManager;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;

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
		private Supplier<NBTTagCompound> pressAction;

		public ModKeyBinding(HotKeys.ModHotKey properties, Supplier<NBTTagCompound> pressAction)
		{
			super(setKeyName(properties.name), properties.keyCode, properties.category);
			this.properties = properties;
			this.pressAction = pressAction;
			KeyHandler.KEY_BINDINGS.put(this.getName(), this);
		}

		private static String setKeyName(String name)
		{
			return Info.MODID + ".key." + name + ".name";
		}

		public String getName()
		{
			return this.properties.name;
		}

		public void addPressData(NBTTagCompound compound)
		{
			compound.setBoolean(this.getName(), this.getIsKeyPressed());
		}

		public void onPress()
		{
			NBTTagCompound compound = this.pressAction.get();
			if (compound.hasNoTags())
				return;
			KeyHandler.addData(this.getName(), compound);
		}

		public boolean onTick()
		{
			if (this.prePress != this.getIsKeyPressed())
			{
				this.prePress = this.getIsKeyPressed();
				return true;
			}
			return false;
		}
	}
}
