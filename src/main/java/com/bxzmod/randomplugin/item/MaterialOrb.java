package com.bxzmod.randomplugin.item;

import com.bxzmod.randomplugin.capability.CapabilityLoader;
import com.bxzmod.randomplugin.capability.MaterialOrbHolder;
import com.bxzmod.randomplugin.capability.capinterface.IMaterialOrbHolder;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class MaterialOrb extends ModItemBase
{
	public MaterialOrb()
	{
		super("material_orb");
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return this.onItemRightClick(worldIn, player, hand).getType();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (stack.getItem().equals(this))
		{
			if (stack.hasCapability(CapabilityLoader.MATERIAL_ORB_HOLDER, EnumFacing.NORTH))
			{
				IMaterialOrbHolder holder = stack.getCapability(CapabilityLoader.MATERIAL_ORB_HOLDER, EnumFacing.NORTH);
				holder.dropStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack, playerIn.isSneaking());
				return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
			}
		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new MaterialOrbHolder.Provider();
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (stack.hasCapability(CapabilityLoader.MATERIAL_ORB_HOLDER, EnumFacing.NORTH))
		{
			IMaterialOrbHolder holder = stack.getCapability(CapabilityLoader.MATERIAL_ORB_HOLDER, EnumFacing.NORTH);
			tooltip.addAll(holder.getTooltip(stack));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public List<IRecipe> getRecipes()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean hasRecipe()
	{
		return false;
	}
}
