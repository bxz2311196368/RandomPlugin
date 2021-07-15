package com.bxzmod.randomplugin.item;

import com.bxzmod.randomplugin.Info;
import com.bxzmod.randomplugin.creativetabs.CreativeTabsLoader;
import com.bxzmod.randomplugin.mixin.mixininterface.IMixinBlock;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import java.util.Collections;
import java.util.List;

public class UniversalTool extends ItemTool
{
	protected UniversalTool()
	{
		super(Integer.MAX_VALUE, ToolMaterial.EMERALD, Collections.emptySet());
		this.setHarvestLevel("axe", Integer.MAX_VALUE);
		this.setHarvestLevel("shovel", Integer.MAX_VALUE);
		this.setHarvestLevel("pickaxe", Integer.MAX_VALUE);
		this.setHarvestLevel("hoe", Integer.MAX_VALUE);
		this.setHarvestLevel("sword", Integer.MAX_VALUE);
		this.setUnlocalizedName("limitless_tool");
		this.setTextureName(Info.MODID + ":limitless_tool");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabsLoader.modTab);
	}

	@Override
	public float func_150893_a(ItemStack itemStack, Block block)
	{
		if (block == null)
			return 30.0F;
		float hardness = ((IMixinBlock) block).getHardness();
		if (hardness <= 0.0F)
			return 30.0F;
		return 10.0F * hardness;
	}

	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta)
	{
		return this.func_150893_a(stack, block);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity)
	{
		return Items.shears.itemInteractionForEntity(itemstack, player, entity);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
	{
		return Items.shears.onBlockStartBreak(itemstack, x, y, z, player);
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
	{
		return true;
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int meta,
			float p_77648_8_, float p_77648_9_, float p_77648_10_)
	{
		if (!player.canPlayerEdit(x, y, z, meta, itemStack))
		{
			return false;
		} else
		{
			UseHoeEvent event = new UseHoeEvent(player, itemStack, world, x, y, z);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return false;
			}

			if (event.getResult() == Event.Result.ALLOW)
			{
				return true;
			}

			Block block = world.getBlock(x, y, z);

			if (meta != 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass
					|| block == Blocks.dirt))
			{
				Block block1 = Blocks.farmland;
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
						(double) ((float) z + 0.5F), block1.stepSound.getStepResourcePath(),
						(block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

				if (!world.isRemote)
				{
					world.setBlock(x, y, z, block1);
					world.setBlockMetadataWithNotify(x, y, z, 7, 2);
				}
				return true;
			} else
			{
				return false;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		NBTTagCompound dig = new NBTTagCompound();
		dig.setInteger("dig_range", 1);
		dig.setInteger("dig_depth", 1);
		ItemStack limitlesstoolwithnbt = new ItemStack(itemIn);
		ItemStack limitlesstoolwithnbt1 = new ItemStack(itemIn);
		limitlesstoolwithnbt.addEnchantment(Enchantment.looting, 10);
		limitlesstoolwithnbt.addEnchantment(Enchantment.fortune, 10);
		limitlesstoolwithnbt.getTagCompound().setTag("dig_parameter", dig);
		limitlesstoolwithnbt1.addEnchantment(Enchantment.looting, 10);
		limitlesstoolwithnbt1.addEnchantment(Enchantment.silkTouch, 1);
		limitlesstoolwithnbt1.getTagCompound().setTag("dig_parameter", dig);
		subItems.add(limitlesstoolwithnbt);
		subItems.add(limitlesstoolwithnbt1);
	}
}
