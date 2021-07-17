package com.bxzmod.randomplugin.advancement;

import com.bxzmod.randomplugin.ModInfo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javafx.util.Pair;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Map;

public class AdvancementLoader
{
	public AdvancementLoader(MinecraftServer server)
	{
		AdvancementList advancementList = AdvancementManager.ADVANCEMENT_LIST;
		advancementList.roots.addAll(this.getModRootAdvancement());
		advancementList.nonRoots.addAll(this.getModNonRootAdvancement());
	}

	private Collection<Advancement> getModRootAdvancement()
	{
		Collection<Advancement> root = Sets.newHashSet(
				this.createRootAdvancement(new ResourceLocation(ModInfo.MODID + "advancement.root"), null, null,
						this.createCriterion(new Pair("imp", new Criterion(new ImpossibleTrigger.Instance()))),
						this.createRequirements(new String[][] { { "imp" } })));
		return root;
	}

	private Collection<Advancement> getModNonRootAdvancement()
	{
		Collection<Advancement> nonRoot = Sets.newHashSet(new Advancement[] {});
		return nonRoot;
	}

	private Advancement createRootAdvancement(ResourceLocation id, DisplayInfo info, AdvancementRewards rewardsIn,
			Map<String, Criterion> criteriaIn, String[][] requirementsIn)
	{
		return new Advancement(id, null, info, rewardsIn, criteriaIn, requirementsIn);
	}

	/**
	 * @param list 每个字符串数组都是一项必须条件（与逻辑），其中单个字符串数组内部每个字符串代表的条件是仅需完成其中之一（“或”逻辑）。
	 * @return
	 */
	private String[][] createRequirements(String[]... list)
	{
		return list;
	}

	private Map<String, Criterion> createCriterion(Pair<String, Criterion>... criterions)
	{
		Map<String, Criterion> cache = Maps.newHashMap();
		for (Pair<String, Criterion> c : criterions)
		{
			cache.put(c.getKey(), c.getValue());
		}
		return cache;
	}
}
