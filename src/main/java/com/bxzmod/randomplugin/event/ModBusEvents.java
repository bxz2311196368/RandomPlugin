package com.bxzmod.randomplugin.event;

import com.bxzmod.randomplugin.Holders;
import com.bxzmod.randomplugin.ModInfo;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents
{
	public ModBusEvents()
	{
		//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		//		// Register the enqueueIMC method for modloading
		//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		//		// Register the processIMC method for modloading
		//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		//		// Register the doClientStuff method for modloading
		//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
	}

	@SubscribeEvent
	public static void construct(FMLConstructModEvent event)
	{

	}

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event)
	{
		// some preinit code
		//LOGGER.info("HELLO FROM PREINIT");
		//LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
	}

	@SubscribeEvent
	public static void doClientStuff(final FMLClientSetupEvent event)
	{
		// do something that can only be done on the client
		//LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
	}

	@SubscribeEvent
	public static void enqueueIMC(final InterModEnqueueEvent event)
	{
		// some example code to dispatch IMC to another mod
		//		InterModComms.sendTo("randomplugin", "helloworld", () -> {
		//			LOGGER.info("Hello world from the MDK");
		//			return "Hello world";
		//		});
	}

	@SubscribeEvent
	public static void processIMC(final InterModProcessEvent event)
	{
		// some example code to receive and process InterModComms from other mods
		//LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
	}

	@SubscribeEvent
	public static void LoadComplete(FMLLoadCompleteEvent event)
	{
		Holders.bucket.maxStackSize = 64;
	}
}
