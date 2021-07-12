package com.bxzmod.randomplugin.item;

import com.bxzmod.randomplugin.Info;
import com.bxzmod.randomplugin.creativetabs.CreativeTabsLoader;
import com.bxzmod.randomplugin.utils.ChromaticCraftCompatible;
import com.bxzmod.randomplugin.utils.Helper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class SyncData extends Item
{
	public SyncData()
	{
		super();
		this.setUnlocalizedName("sync_data");
		this.setTextureName(Info.MODID + ":sync_data");
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
		if (!itemStack.hasTagCompound())
		{
			this.addPlayerInfo(itemStack, player);
		} else
		{
			NBTTagCompound compound = itemStack.getTagCompound();
			if (compound.hasNoTags() || !compound.hasKey("UUID") || !compound.hasKey("player_name"))
			{
				this.addPlayerInfo(itemStack, player);
			} else
			{
				if (!world.isRemote)
				{
					EntityPlayer from = Helper.findPlayerByUUID(compound.getString("UUID"));
					if (from == null)
						return itemStack;
					if (!from.getGameProfile().getId().equals(player.getGameProfile().getId()))

						ChromaticCraftCompatible.syncPlayerChromaticCraft(from, player);
				}
			}
		}
		return itemStack;
	}

	private void addPlayerInfo(ItemStack stack, EntityPlayer player)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("UUID", player.getGameProfile().getId().toString());
		compound.setString("player_name", player.getDisplayName());
		stack.setTagCompound(compound);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean sneak)
	{
		super.addInformation(itemStack, player, list, sneak);
		if (itemStack.getItem() == this)
		{
			if (!itemStack.hasTagCompound())
				return;
			NBTTagCompound compound = itemStack.getTagCompound();
			if (compound.hasNoTags() || !compound.hasKey("UUID") || !compound.hasKey("player_name"))
				return;
			else
			{
				list.add(I18n.format("message:player_owner") + compound.getString("player_name"));
			}
		}
	}
}
