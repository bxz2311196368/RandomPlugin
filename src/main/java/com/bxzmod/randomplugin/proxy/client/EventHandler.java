package com.bxzmod.randomplugin.proxy.client;

import com.bxzmod.randomplugin.ModInfo;
import com.bxzmod.randomplugin.hotkey.KeyHandler;
import com.bxzmod.randomplugin.item.ItemLoader;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModInfo.MODID)
public class EventHandler
{
	@SubscribeEvent
	public static void onModelReg(ModelRegistryEvent event)
	{
		//TODO
		setDefaultModel(ItemLoader.materialOrb);
		setDefaultModel(ItemLoader.ring);
	}

	public static void setDefaultModel(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			KeyHandler.sendSyncData();
			KeyHandler.sendKeyChange();
		}

	}

	@SubscribeEvent
	public static void onKeyPress(InputEvent.KeyInputEvent event)
	{
		KeyHandler.handlerKeyPress();
	}

	//@SubscribeEvent
	public static void toolTipRender(RenderTooltipEvent.PostText event)
	{

	}

}
