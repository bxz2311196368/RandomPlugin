package com.bxzmod.randomplugin.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.bxzmod.randomplugin.registry.ModObjectHolder;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class Ring extends ModItemBase implements IBauble
{
	public Ring()
	{
		super("ring");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemStack)
	{
		return BaubleType.TRINKET;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player)
	{
		EntityPlayer thePlayer = (EntityPlayer) player;
		if (player.isBurning())
			player.extinguish();
		if (player.ticksExisted % 20 == 0)
		{
			this.setPlayerCapability(thePlayer);
		}
		thePlayer.capabilities.allowFlying = true;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player)
	{
		this.setPlayerCapability((EntityPlayer) player);
		setWornMark((EntityPlayer) player, true);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player)
	{
		((EntityPlayer) player).capabilities.allowFlying = false;
		player.removePotionEffect(ModObjectHolder.digSpeed);
		player.removePotionEffect(ModObjectHolder.regeneration);
		player.removePotionEffect(ModObjectHolder.resistance);
		player.removePotionEffect(ModObjectHolder.fire_resistance);
		player.removePotionEffect(ModObjectHolder.water_breathing);
		player.removePotionEffect(ModObjectHolder.saturation);
		setWornMark((EntityPlayer) player, false);
	}

	private void setPlayerCapability(EntityPlayer player)
	{
		player.capabilities.allowFlying = true;
		player.setAir(300);
		player.addPotionEffect(new PotionEffect(ModObjectHolder.digSpeed, 1200, 0, false, false));
		player.addPotionEffect(new PotionEffect(ModObjectHolder.regeneration, 1200, 5, false, false));
		player.addPotionEffect(new PotionEffect(ModObjectHolder.resistance, 1200, 3, false, false));
		player.addPotionEffect(new PotionEffect(ModObjectHolder.fire_resistance, 1200, 0, false, false));
		player.addPotionEffect(new PotionEffect(ModObjectHolder.water_breathing, 1200, 0, false, false));
		player.addPotionEffect(new PotionEffect(ModObjectHolder.saturation, 1200, 0, false, false));
	}

	public static boolean hasWorn(EntityPlayer player)
	{
		return ModPlayerData.getDataByPlayer(player).isRingWear();
	}

	private static void setWornMark(EntityPlayer player, boolean mark)
	{
		ModPlayerData.getDataByPlayer(player).setRingWear(mark);
	}

	@Override
	public boolean hasRecipe()
	{
		return true;
	}

	@Override
	public List<IRecipe> getRecipes()
	{
		return Lists.newArrayList(
				new ShapedOreRecipe(this.getRegistryName(), this, " A ", "ABA", " A ", 'A', "egg", 'B', "netherStar")
						.setRegistryName(this.getRegistryName()));
	}
}
