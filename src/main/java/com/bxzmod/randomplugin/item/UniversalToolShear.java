package com.bxzmod.randomplugin.item;

import com.bxzmod.randomplugin.Info;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class UniversalToolShear extends ItemShears
{
	public UniversalToolShear()
	{
		this.setHarvestLevel("axe", Integer.MAX_VALUE);
		this.setHarvestLevel("shovel", Integer.MAX_VALUE);
		this.setHarvestLevel("pickaxe", Integer.MAX_VALUE);
		this.setHarvestLevel("hoe", Integer.MAX_VALUE);
		this.setHarvestLevel("sword", Integer.MAX_VALUE);
		this.setUnlocalizedName("limitless_tool");
		this.setTextureName(Info.MODID + ":limitless_tool");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	@Override
	public float func_150893_a(ItemStack itemStack, Block block)
	{
		return ItemLoader.limitless_tool.func_150893_a(itemStack, block);
	}

	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta)
	{
		return ItemLoader.limitless_tool.getDigSpeed(stack, block, meta);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity)
	{
		return ItemLoader.limitless_tool.itemInteractionForEntity(itemstack, player, entity);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
	{
		return ItemLoader.limitless_tool.onBlockStartBreak(itemstack, x, y, z, player);
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
	{
		return true;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int meta,
			float p_77648_8_, float p_77648_9_, float p_77648_10_)
	{
		return ItemLoader.limitless_tool
				.onItemUse(itemStack, player, world, x, y, z, meta, p_77648_8_, p_77648_9_, p_77648_10_);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
	{
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		p_77624_3_.add(Items.shears.getItemStackDisplayName(new ItemStack(Items.shears)));
	}
}
