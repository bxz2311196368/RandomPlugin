package com.bxzmod.randomplugin.capability;

import com.bxzmod.randomplugin.capability.capinterface.IMaterialOrbHolder;
import com.bxzmod.randomplugin.utils.Helper;
import com.bxzmod.randomplugin.utils.LargeItemStack;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MaterialOrbHolder
{
	public static class Storage implements Capability.IStorage<IMaterialOrbHolder>
	{

		@Nullable
		@Override
		public NBTBase writeNBT(Capability<IMaterialOrbHolder> capability, IMaterialOrbHolder instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IMaterialOrbHolder> capability, IMaterialOrbHolder instance, EnumFacing side,
				NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}

	public static class Implementation implements IMaterialOrbHolder, INBTSerializable<NBTTagCompound>
	{
		private LinkedList<LargeItemStack> itemStacks = Lists.newLinkedList();

		@Override
		public void addItems(List<LargeItemStack> itemStacks)
		{
			if (this.itemStacks.isEmpty())
				this.itemStacks.addAll(itemStacks);
			else
			{
				List<LargeItemStack> temp = Lists.newArrayList();
				for (LargeItemStack newStack : itemStacks)
				{
					boolean edit = false;
					for (LargeItemStack oldStack : this.itemStacks)
					{
						if (Helper.isItemStackSame(oldStack.getStack(), newStack.getStack()))
						{
							oldStack.increaseAmount(newStack.decreaseAmount(newStack.getAmount()));
							edit = true;
							break;
						}
					}
					if (!edit)
						temp.add(newStack);
				}
				if (!temp.isEmpty())
					this.itemStacks.addAll(temp);
			}
		}

		@Override
		public void dropStack(World world, double x, double y, double z, ItemStack itemStack, boolean dropAll)
		{
			this.reSyncData(itemStack);
			while (!this.itemStacks.isEmpty())
			{
				LargeItemStack largeItemStack = this.itemStacks.getFirst();
				ItemStack stack = largeItemStack.getStack().copy();
				stack.setCount(largeItemStack.decreaseAmount(Math.min(64, largeItemStack.getAmount())));
				EntityItem entityItem = new EntityItem(world, x, y, z, stack);
				entityItem.setNoPickupDelay();
				if (!world.isRemote)
					world.spawnEntity(entityItem);
				if (largeItemStack.getAmount() <= 0)
					this.itemStacks.removeFirst();
				if (!dropAll)
					break;
			}
			this.reSyncNBT(itemStack);
			if (this.itemStacks.isEmpty())
				itemStack.setCount(0);
		}

		@Nonnull
		@Override
		public List<String> getTooltip(ItemStack stack)
		{
			this.reSyncData(stack);
			if (this.itemStacks.isEmpty())
				return Collections.emptyList();

			List<String> toolTip = Lists.newArrayList();
			for (LargeItemStack largeItemStack : this.itemStacks)
				toolTip.add(largeItemStack.getStack().getDisplayName() + ":" + largeItemStack.getAmount());
			return toolTip;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbtTagCompound = new NBTTagCompound();
			NBTTagList nbtTagListItem = new NBTTagList();
			int[] counts = new int[this.itemStacks.size()];
			int i = 0;
			for (LargeItemStack largeItemStack : this.itemStacks)
			{
				nbtTagListItem.appendTag(largeItemStack.getStack().writeToNBT(new NBTTagCompound()));
				counts[i++] = largeItemStack.getAmount();
			}
			NBTTagIntArray nbtTagIntArray = new NBTTagIntArray(counts);
			nbtTagCompound.setTag("Item", nbtTagListItem);
			nbtTagCompound.setTag("Count", nbtTagIntArray);
			return nbtTagCompound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			if (nbt == null)
				return;
			NBTTagList nbtTagListItem = (NBTTagList) nbt.getTag("Item");
			NBTTagIntArray nbtTagIntArray = (NBTTagIntArray) nbt.getTag("Count");
			int[] counts = nbtTagIntArray.getIntArray();
			int size = nbtTagListItem.tagCount();
			this.itemStacks.clear();
			for (int i = 0; i < size; i++)
				this.itemStacks
						.addLast(new LargeItemStack(new ItemStack(nbtTagListItem.getCompoundTagAt(i)), counts[i]));
		}

		private void reSyncData(ItemStack stack)
		{
			if (stack.hasTagCompound())
				this.deserializeNBT(stack.getTagCompound());
		}

		private void reSyncNBT(ItemStack itemStack)
		{
			itemStack.setTagCompound(this.serializeNBT());
		}
	}

	public static class Provider implements ICapabilitySerializable<NBTTagCompound>
	{
		IMaterialOrbHolder holder = new Implementation();
		Capability.IStorage<IMaterialOrbHolder> storage = CapabilityLoader.MATERIAL_ORB_HOLDER.getStorage();

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability.equals(CapabilityLoader.MATERIAL_ORB_HOLDER);
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
		{
			if (capability.equals(CapabilityLoader.MATERIAL_ORB_HOLDER))
				return (T) this.holder;
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return (NBTTagCompound) this.storage
					.writeNBT(CapabilityLoader.MATERIAL_ORB_HOLDER, this.holder, EnumFacing.NORTH);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.storage.readNBT(CapabilityLoader.MATERIAL_ORB_HOLDER, holder, EnumFacing.NORTH, nbt);
		}
	}
}
