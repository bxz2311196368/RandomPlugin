package com.bxzmod.randomplugin.mixin.mixinhandler;

import com.bxzmod.randomplugin.mixin.mixininterface.IMixinEntityTracker;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.EntityTracker;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityTracker.class)
public abstract class MixinEntityTracker implements IMixinEntityTracker
{
	@Shadow
	public abstract void updateTrackedEntities();

	@Override
	public void safeUpdateTrackedEntities()
	{
		try
		{
			this.updateTrackedEntities();
		} catch (Exception e)
		{
			FMLCommonHandler.instance().getMinecraftServerInstance()
					.addChatMessage(new ChatComponentText("exception from entityTracker:updateTrackedEntities"));
		}
	}
}
