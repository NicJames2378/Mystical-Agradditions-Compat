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
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {
		Item itemEssence = Item.field_150901_e.func_82594_a(new ResourceLocation("mysticalagradditions","crafting"));
		if(itemEssence!=null)
			for(int i=6; i<=6; i++)
			{
				Item itemSeeds = Item.field_150901_e.func_82594_a(new ResourceLocation("mysticalagradditions","tier"+i+"_inferium_seeds"));
				Block blockCrop = Block.field_149771_c.func_82594_a(new ResourceLocation("mysticalagradditions","tier"+i+"_inferium_crop"));
				if(itemSeeds!=null&&blockCrop!=null)
					BelljarHandler.cropHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemEssence,i)}, new ItemStack(Blocks.field_150346_d), blockCrop.func_176223_P());
			}	
		try
		{
			Class c_Types = Class.forName("com.blakebr0.mysticalagradditions.lib.CropType$Type");
			Method m_isEnabled = c_Types.getMethod("isEnabled");
			if(c_Types!=null && m_isEnabled!=null)
				for(Object type : c_Types.getEnumConstants())
					if((Boolean)m_isEnabled.invoke(type))
						addType(((IStringSerializable)type).func_176610_l(), ((com.blakebr0.mysticalagradditions.lib.CropType.Type)type).getRoot().func_177230_c(), ((com.blakebr0.mysticalagradditions.lib.CropType.Type)type).getRootMeta());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void addType(String type, Block root, int meta)//Block root)
	{
		Item itemSeeds = Item.field_150901_e.func_82594_a(new ResourceLocation("mysticalagradditions:"+type+"_seeds"));
		Item itemEssence = Item.field_150901_e.func_82594_a(new ResourceLocation("mysticalagradditions:"+type+"_essence"));
		Block blockRoot = root;//Block.getBlockFromName("mysticalagradditions:special="+root);		
		Block blockCrop = Block.field_149771_c.func_82594_a(new ResourceLocation("mysticalagradditions:"+type+"_crop"));
		if(itemSeeds!=null && itemEssence!=null && blockCrop!=null) {
			System.out.println("njmac: "+blockRoot.toString()+", "+blockCrop.toString()+"="+meta);
			try {
				BelljarHandler.cropHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemEssence)}, new ItemStack(blockRoot, 1, meta), blockCrop.func_176223_P());
			} catch (Exception e) {
				e.printStackTrace();
				BelljarHandler.cropHandler.register(new ItemStack(itemSeeds), new ItemStack[]{new ItemStack(itemEssence)}, new ItemStack(Blocks.field_150346_d), blockCrop.func_176223_P());
				System.out.println("Could not find root block '"+blockRoot.toString()+"'! Defaulting to dirt.");
			}
		}
	}
}
