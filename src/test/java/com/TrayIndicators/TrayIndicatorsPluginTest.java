package com.TrayIndicators;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TrayIndicatorsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TrayIndicatorsPlugin.class);
		RuneLite.main(args);
	}
}