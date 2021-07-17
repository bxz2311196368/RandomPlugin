package com.bxzmod.randomplugin.tweak;

import cpw.mods.modlauncher.ArgumentHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import joptsimple.OptionSpecBuilder;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class ModTransformationService implements ITransformationService
{
	@Nonnull
	@Override
	public String name()
	{
		return "randomplugin";
	}

	@Override
	public void initialize(IEnvironment environment)
	{

	}

	@Override
	public void beginScanning(IEnvironment environment)
	{

	}

	@Override
	public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException
	{

	}

	@Nonnull
	@Override
	public List<ITransformer> transformers()
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public void arguments(BiFunction<String, String, OptionSpecBuilder> a)
	{
		try
		{
			Field field = Launcher.class.getDeclaredField("argumentHandler");
			field.setAccessible(true);
			ArgumentHandler argumentHandler = (ArgumentHandler) field.get(Launcher.INSTANCE);
			Field field1 = ArgumentHandler.class.getDeclaredField("args");
			field1.setAccessible(true);
			String[] args = (String[]) field1.get(argumentHandler);
			args = Arrays.copyOf(args, args.length + 1);
			args[args.length - 1] = "-nogui";
			field1.set(argumentHandler, args);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
