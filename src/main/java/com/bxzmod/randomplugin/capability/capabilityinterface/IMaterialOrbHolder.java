package com.bxzmod.randomplugin.capability.capabilityinterface;

import com.bxzmod.randomplugin.utils.LargeItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public interface IMaterialOrbHolder extends INBTSerializable<NBTTagCompound>
{
	void addItems(List<LargeItemStack> itemStacks);

	void dropStack(World world, double x, double y, double z, ItemStack itemStack, boolean dropAll);

	void insertToInventory(ItemStack holderItem, IInventory inventory);

	List<String> getTooltip(ItemStack stack);

	List<LargeItemStack> getStacks();
}
