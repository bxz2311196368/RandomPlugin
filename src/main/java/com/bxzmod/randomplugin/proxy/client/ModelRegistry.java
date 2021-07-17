package com.bxzmod.randomplugin.proxy.client;

import com.bxzmod.randomplugin.ModInfo;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModInfo.MODID)
public class ModelRegistry
{
	public static HashMap<ItemStack, ModelResourceLocation> itemModels = Maps.newHashMap();
	public static HashMap<Block, IStateMapper> blockModels = Maps.newHashMap();

	@SubscribeEvent
	public static void handlerModelRegistry(ModelRegistryEvent event)
	{
		if (!blockModels.isEmpty())
			for (Block block : blockModels.keySet())
				ModelLoader.setCustomStateMapper(block, blockModels.get(block));
		if (!itemModels.isEmpty())
			for (ItemStack stack : itemModels.keySet())
				ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), itemModels.get(stack));
	}
}
