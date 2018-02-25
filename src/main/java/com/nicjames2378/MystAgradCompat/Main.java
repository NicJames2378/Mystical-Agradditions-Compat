package com.nicjames2378.MystAgradCompat;

import java.lang.reflect.Method;

import com.nicjames2378.MystAgradCompat.proxy.CommonProxy;
import com.nicjames2378.MystAgradCompat.utils.Reference;

import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class Main {

	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		proxy.PreInit(event);
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event) {
		proxy.Init(event);
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		proxy.PostInit(event);
		
		String typeInferium6 = "tier6_inferium";
		String essenceInferium = "mysticalagriculture:crafting";		
		Item itemSeeds = Item.REGISTRY.getObject(new ResourceLocation("mysticalagradditions:"+typeInferium6+"_seeds"));
		Item itemEssence = Item.REGISTRY.getObject(new ResourceLocation(essenceInferium));
		//TODO: Add config file to enable/disable this
		Block blockRoot = Block.REGISTRY.getObject(new ResourceLocation("mysticalagradditions:storage"));//Block of insanium essence			//Blocks.DIRT;
		Block blockCrop = Block.REGISTRY.getObject(new ResourceLocation("mysticalagradditions:"+typeInferium6+"_crop"));
		
		System.out.println("Found: ("+typeInferium6+"_seeds), yielding ("+essenceInferium+"_essence), to grow on ("+blockRoot+")");
		BelljarHandler.cropHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemEssence, 6)}, new ItemStack(blockRoot), blockCrop.getDefaultState());
		
		try
		{
			Class c_Types = Class.forName("com.blakebr0.mysticalagradditions.lib.CropType$Type");
			Method m_isEnabled = c_Types.getMethod("isEnabled");
			if(c_Types!=null && m_isEnabled!=null)
				for(Object type : c_Types.getEnumConstants())
					if((Boolean)m_isEnabled.invoke(type))
						addType(((IStringSerializable)type).getName(), ((com.blakebr0.mysticalagradditions.lib.CropType.Type)type).getRoot().getBlock(), ((com.blakebr0.mysticalagradditions.lib.CropType.Type)type).getRootMeta());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void addType(String type, Block root, int meta)//Block root)
	{
		Item itemSeeds = Item.REGISTRY.getObject(new ResourceLocation("mysticalagradditions:"+type+"_seeds"));
		Item itemEssence = Item.REGISTRY.getObject(new ResourceLocation("mysticalagradditions:"+type+"_essence"));
		Block blockRoot = root;
		Block blockCrop = Block.REGISTRY.getObject(new ResourceLocation("mysticalagradditions:"+type+"_crop"));
		if(itemSeeds!=null && itemEssence!=null && blockCrop!=null) {
			System.out.println("Found: ("+type+"_seeds), yielding ("+type+"_essence), to grow on ("+blockRoot+"="+meta+")");
			try {
				BelljarHandler.cropHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemEssence)}, new ItemStack(blockRoot, 1, meta), blockCrop.getDefaultState());
			} catch (Exception e) {
				e.printStackTrace();
				BelljarHandler.cropHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemEssence)}, new ItemStack(Blocks.DIRT), blockCrop.getDefaultState());
				System.out.println("Could not find root block '"+blockRoot.toString()+"'! Defaulting to dirt.");
			}
		}
	}
}