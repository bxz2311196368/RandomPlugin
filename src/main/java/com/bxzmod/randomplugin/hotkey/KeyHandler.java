package com.bxzmod.randomplugin.hotkey;

import com.bxzmod.randomplugin.network.NetworkLoader;
import com.bxzmod.randomplugin.network.PlayerDataPacket;
import com.bxzmod.randomplugin.network.PlayerKeyPressPacket;
import com.google.common.collect.Maps;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class KeyHandler
{
	public static final HashMap<String, HotKeys.ModHotKey> HOT_KEY_HASH_MAP = Maps.newHashMap();
	public static final HashMap<String, ClientKeyBind.ModKeyBinding> KEY_BINDINGS = Maps.newHashMap();
	public static final String NBT_UUID = "UUID";
	public static NBTTagCompound data = new NBTTagCompound();

	public static void onKeyPressPacket(NBTTagCompound compound)
	{
		UUID uuid = getUUID(compound);
		Set<String> names = compound.getKeySet();
		for (String name : names)
			if (HOT_KEY_HASH_MAP.containsKey(name))
			{
				HOT_KEY_HASH_MAP.get(name).onKeyPressPacket(uuid, compound.getBoolean(name));
			}
	}

	public static void onDataSyncPacket(NBTTagCompound compound)
	{
		UUID uuid = getUUID(compound);
		Set<String> names = compound.getKeySet();
		for (String name : names)
			if (HOT_KEY_HASH_MAP.containsKey(name))
			{
				HOT_KEY_HASH_MAP.get(name).onServerDataPacket(uuid, compound.getCompoundTag(name));
			}
	}

	public static void handlerKeyPress()
	{
		for (ClientKeyBind.ModKeyBinding key : KeyHandler.KEY_BINDINGS.values())
			if (key.isPressed())
				key.onPress();
	}

	public static void addData(String name, NBTTagCompound compound)
	{
		data.setTag(name, compound);
	}

	public static void sendSyncData()
	{
		if (data.isEmpty())
			return;
		addUUID(data);
		PlayerDataPacket packet = new PlayerDataPacket();
		packet.nbtTagCompound = data;
		NetworkLoader.instance.sendToServer(packet);
		data = new NBTTagCompound();
	}

	public static void sendKeyChange()
	{
		NBTTagCompound compound = new NBTTagCompound();
		for (ClientKeyBind.ModKeyBinding keyBinding : KEY_BINDINGS.values())
			if (keyBinding.onTick())
			{
				keyBinding.addPressData(compound);
			}
		if (compound.isEmpty())
			return;
		addUUID(compound);
		PlayerKeyPressPacket packet = new PlayerKeyPressPacket();
		packet.nbtTagCompound = compound;
		NetworkLoader.instance.sendToServer(packet);
	}

	private static void addUUID(NBTTagCompound compound)
	{
		compound.setString(NBT_UUID, Minecraft.getMinecraft().player.getGameProfile().getId().toString());
	}

	private static UUID getUUID(NBTTagCompound compound)
	{
		return UUIDTypeAdapter.fromString(compound.getString(NBT_UUID));
	}
}
