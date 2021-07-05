package com.bxzmod.randomplugin.mixin.mixinhandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class MixinServerBlock
{
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
	{
		return Item.getItemFromBlock((Block) (Object) this);
	}

	public boolean isFlowerPot()
	{
		return false;
	}
}
