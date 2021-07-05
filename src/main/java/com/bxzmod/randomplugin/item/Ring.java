package com.bxzmod.randomplugin.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.bxzmod.randomplugin.Info;
import com.bxzmod.randomplugin.creativetabs.CreativeTabsLoader;
import com.bxzmod.randomplugin.utils.ModPlayerData;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;

public class Ring extends Item implements IBauble
{

	public Ring()
	{
		super();
		MinecraftForge.EVENT_BUS.register(this);
		this.setUnlocalizedName("ring");
		this.setTextureName(Info.MODID + ":ring");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.modTab);
	}

	@Override
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1)
	{

		return true;
	}

	@Override
	public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1)
	{

		return true;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0)
	{

		return BaubleType.RING;
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
		player.removePotionEffect(Potion.digSpeed.getId());
		player.removePotionEffect(Potion.regeneration.getId());
		player.removePotionEffect(Potion.resistance.getId());
		player.removePotionEffect(Potion.fireResistance.getId());
		player.removePotionEffect(Potion.waterBreathing.getId());
		player.removePotionEffect(Potion.field_76443_y.getId());
		setWornMark((EntityPlayer) player, false);
	}

	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase e)
	{
		EntityPlayer player = (EntityPlayer) e;
		if (player.isBurning())
			player.extinguish();
		if (itemStack.getItemDamage() == 0 && player.ticksExisted % 20 == 0)
		{
			this.setPlayerCapability(player);
		}
		player.capabilities.allowFlying = true;

		if (!ModPlayerData.ModPlayerClientData.ringMark && FMLCommonHandler.instance().getSide().isClient())
		{
			if (FMLClientHandler.instance().getServer() == null)
				return;
			ModPlayerData.addRingMark((EntityPlayer) e);
			ModPlayerData.ModPlayerClientData.ringMark = true;
		}

	}

	private void setPlayerCapability(EntityPlayer player)
	{
		player.capabilities.allowFlying = true;
		player.setAir(300);
		player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(), 1200, 0, true));
		player.addPotionEffect(new PotionEffect(Potion.regeneration.getId(), 1200, 5, true));
		player.addPotionEffect(new PotionEffect(Potion.resistance.getId(), 1200, 3, true));
		player.addPotionEffect(new PotionEffect(Potion.fireResistance.getId(), 1200, 0, true));
		player.addPotionEffect(new PotionEffect(Potion.waterBreathing.getId(), 1200, 0, true));
		player.addPotionEffect(new PotionEffect(Potion.field_76443_y.getId(), 1200, 0, true));
	}

	public static boolean hasWorn(EntityPlayer player)
	{
		return ModPlayerData.getDataByPlayer(player).isRingWear();
	}

	private static void setWornMark(EntityPlayer player, boolean mark)
	{
		ModPlayerData.getDataByPlayer(player).setRingWear(mark);
	}

}
