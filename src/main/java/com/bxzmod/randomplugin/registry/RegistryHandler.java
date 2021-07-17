package com.bxzmod.randomplugin.registry;

import com.bxzmod.randomplugin.ModInfo;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class RegistryHandler
{
	//方块注册
	public static ArrayList<Block> BLOCKS = Lists.newArrayList();
	//物品注册
	public static ArrayList<Item> ITEMS = Lists.newArrayList();
	//生物群系注册
	public static ArrayList<Biome> BIOMES = Lists.newArrayList();
	//药水效果注册
	public static ArrayList<Potion> POTIONS = Lists.newArrayList();
	//药水瓶注册
	public static ArrayList<PotionType> POTION_TYPES = Lists.newArrayList();
	//附魔注册
	public static ArrayList<Enchantment> ENCHANTMENTS = Lists.newArrayList();
	//实体注册
	public static ArrayList<EntityEntry> ENTITIES = Lists.newArrayList();
	//合成表注册
	public static ArrayList<IRecipe> RECIPES = Lists.newArrayList();
	//村民职业注册
	public static ArrayList<VillagerRegistry.VillagerProfession> PROFESSIONS = Lists.newArrayList();
	//声音事件注册
	public static ArrayList<SoundEvent> SOUND_EVENTS = Lists.newArrayList();

	@SubscribeEvent
	public static void registryBlock(RegistryEvent.Register<Block> event)
	{
		if (!BLOCKS.isEmpty())
			event.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
	}

	@SubscribeEvent
	public static void registryItem(RegistryEvent.Register<Item> event)
	{
		if (!ITEMS.isEmpty())
			event.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
	}

	@SubscribeEvent
	public static void registryPotion(RegistryEvent.Register<Potion> event)
	{
		if (!POTIONS.isEmpty())
			event.getRegistry().registerAll(POTIONS.toArray(new Potion[POTION_TYPES.size()]));
	}

	@SubscribeEvent
	public static void registryBiome(RegistryEvent.Register<Biome> event)
	{
		if (!BIOMES.isEmpty())
			event.getRegistry().registerAll(BIOMES.toArray(new Biome[BIOMES.size()]));
	}

	@SubscribeEvent
	public static void registryPotionType(RegistryEvent.Register<PotionType> event)
	{
		if (!POTION_TYPES.isEmpty())
			event.getRegistry().registerAll(POTION_TYPES.toArray(new PotionType[POTION_TYPES.size()]));
	}

	@SubscribeEvent
	public static void registryEnchantment(RegistryEvent.Register<Enchantment> event)
	{
		if (!ENCHANTMENTS.isEmpty())
			event.getRegistry().registerAll(ENCHANTMENTS.toArray(new Enchantment[ENCHANTMENTS.size()]));
	}

	@SubscribeEvent
	public static void registryEntity(RegistryEvent.Register<EntityEntry> event)
	{
		if (!ENTITIES.isEmpty())
			event.getRegistry().registerAll(ENTITIES.toArray(new EntityEntry[ENTITIES.size()]));
	}

	@SubscribeEvent
	public static void registryRecipe(RegistryEvent.Register<IRecipe> event)
	{
		if (!RECIPES.isEmpty())
			event.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
	}

	@SubscribeEvent
	public static void registryVillagerProfession(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event)
	{
		if (!PROFESSIONS.isEmpty())
			event.getRegistry()
					.registerAll(PROFESSIONS.toArray(new VillagerRegistry.VillagerProfession[PROFESSIONS.size()]));
	}

	@SubscribeEvent
	public static void registrySoundEvent(RegistryEvent.Register<SoundEvent> event)
	{
		if (!SOUND_EVENTS.isEmpty())
			event.getRegistry().registerAll(SOUND_EVENTS.toArray(new SoundEvent[SOUND_EVENTS.size()]));
	}

}
