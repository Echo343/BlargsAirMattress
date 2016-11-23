package com.blargsworkshop.airmattress;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

import com.blargsworkshop.BlargsToolkit;
import com.blargsworkshop.airmattress.Blocks.BlockAirMattress;
import com.blargsworkshop.airmattress.Items.ItemAirMatress;
import com.blargsworkshop.airmattress.events.EventHandlers;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = AirMattressMod.MODID, name = AirMattressMod.NAME, version = AirMattressMod.VERSION)
public class AirMattressMod
{
    public static final String MODID = "blarg_airmattressmod";
    public static final String NAME = "AirMattress";
    public static final String VERSION = "1.3";
    
    private static final int DEBUG_LVL = 0;
    private static final boolean DEBUG_CHAT = true;
    
    @Instance
    public static AirMattressMod instance = new AirMattressMod();
    
	//Proxies
    @SidedProxy(clientSide = "com.blargsworkshop.airmattress.ClientProxy", serverSide = "com.blargsworkshop.airmattress.ServerProxy")
	public static CommonProxy proxy;
	
	//Items
    public static Item airMattressItem;
    
    private static void initItems() {
    	airMattressItem = new ItemAirMatress();
    	GameRegistry.registerItem(airMattressItem, "modItemAirMatressItem");
    }
    
    //Blocks
    public static Block airMattressBlock;
    
    private static void initBlocks() {
    	airMattressBlock = new BlockAirMattress();
    	GameRegistry.registerBlock(airMattressBlock, "modBlockAirMatressItem");
    }
    
    //Creative Mode Tabs
    public static CreativeTabs tabSleepingbag;
    
    private static void initCreativeTabs() {
    	tabSleepingbag = BlargsToolkit.getBlargTab();
    	if (tabSleepingbag == null) {
    		System.out.println("Error - Creative Tab for Blarg's Workbench didn't init correctly!");
    	}
    }
    
    private static void initRegisters() {
    	
    }
    
    private static void initRecipies() {
    	GameRegistry.addShapedRecipe(new ItemStack(airMattressItem),
        		"csc",
        		"scs",
        		"   ",
        		'c', new ItemStack(Blocks.wool), 
        		's', new ItemStack(Items.string));
    }
    
    @EventHandler
    public static void preInit(FMLPreInitializationEvent e) {
    	proxy.preInit(e);
    	initCreativeTabs();
    	initItems();
    	initBlocks();
    	EventHandlers evh = new EventHandlers();
    	MinecraftForge.EVENT_BUS.register(evh);
    	FMLCommonHandler.instance().bus().register(evh);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e)
    {
    	proxy.init(e);
    	initRegisters();
    	initRecipies();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	proxy.postInit(e);
    }
    
    public static void debug(String message, int debugLvl, EntityPlayer player) {
    	if (debugLvl <= AirMattressMod.DEBUG_LVL) {
    		if (AirMattressMod.DEBUG_CHAT && player != null) {
    			player.addChatMessage(new ChatComponentText(message));
    		}
    		System.out.println(message);
    	}
    }
}
