package com.bxzmod.randomplugin.tweak;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * 注意TweakClass所存在的包不被forge加载，需要单独存放。
 */
public class ModTweaker implements ITweaker
{
	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile)
	{
		((List<String>) Launch.blackboard.get("TweakClasses")).add("org.spongepowered.asm.launch.MixinTweaker");
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader)
	{
		//沙雕“特性”
		try
		{
			byte[] a = classLoader.getClassBytes("net.minecraft.world.World");
			if (a != null)
			{
				//this.injectResources();
				this.injectModInfo();
			}
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String getLaunchTarget()
	{
		return "";
	}

	@Override
	public String[] getLaunchArguments()
	{
		return new String[] { "nogui" };
	}

	private void injectModInfo()
	{
		File code = new File(this.getClass().getResource("/").getPath());
		File root = code.getParentFile().getParentFile().getParentFile();
		File resources = new File(root, "resources\\main");
		for (File file : resources.listFiles())
		{
			if (file.isDirectory())
				continue;
			File res = new File(code, file.getName());
			if (res.exists())
			{
				try
				{
					Files.delete(res.toPath());
					Files.copy(file.toPath(), res.toPath());
					System.err.println("复制：" + file + "到：" + res);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void injectResources()
	{
		File code = new File(this.getClass().getResource("/").getPath());
		File root = code.getParentFile().getParentFile().getParentFile();
		File resources = new File(root, "resources\\main");
		for (File file : resources.listFiles())
		{
			File res = new File(code, file.getName());
			if (res.exists())
				this.delResources(res);
			this.copyResources(file, res);
		}
	}

	private void copyResources(File from, File to)
	{
		try
		{
			System.err.println("复制：" + from.toString() + "到：" + to.toString());
			Files.copy(from.toPath(), to.toPath());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (from.isDirectory())
		{
			for (File sources : from.listFiles())
				this.copyResources(sources, new File(to, sources.getName()));
		}

	}

	private void delResources(File file)
	{
		if (file.isDirectory() && file.listFiles().length != 0)
		{
			for (File sources : file.listFiles())
				this.delResources(sources);
		}
		try
		{
			System.err.println("删除：" + file.toString());
			Files.delete(file.toPath());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
