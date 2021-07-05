package com.bxzmod.randomplugin.item;

import com.bxzmod.randomplugin.Info;
import com.bxzmod.randomplugin.capability.MaterialOrbHolder;
import com.bxzmod.randomplugin.capability.capabilityinterface.IMaterialOrbHolder;
import com.bxzmod.randomplugin.creativetabs.CreativeTabsLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class MaterialOrb extends Item
{
	public MaterialOrb()
	{
		super();
		this.setUnlocalizedName("material_orb");
		this.setTextureName(Info.MODID + ":material_orb");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.modTab);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int posX, int posY, int posZ,
			int facing, float hitX, float hitY, float hitZ)
	{
		this.onItemRightClick(itemStack, world, player);
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (itemStack.getItem() != this)
			return itemStack;
		IMaterialOrbHolder holder = MaterialOrbHolder.fromNBT(itemStack);
		holder.dropStack(world, player.posX, player.posY, player.posZ, itemStack, player.isSneaking());
		if (itemStack.stackSize == 0)
			player.destroyCurrentEquippedItem();
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean sneak)
	{
		super.addInformation(itemStack, player, list, sneak);
		if (itemStack.getItem() == this)
		{
			IMaterialOrbHolder holder = MaterialOrbHolder.fromNBT(itemStack);
			list.addAll(holder.getTooltip(itemStack));
		}
	}
}
