package com.blargsworkshop;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlargsToolkit {
	
	//itemGroup.blargsCreativeTab=Blarg's Workshop
    
    protected static CreativeTabs tabBlargsWorkshop = null;
    protected static final String CREATIVE_TAB_BLARG = "blargsCreativeTab";
    
    public static CreativeTabs getBlargTab() {
    	
    	if (BlargsToolkit.tabBlargsWorkshop == null) {
    		
    		BlargsToolkit.tabBlargsWorkshop =  new CreativeTabs(CREATIVE_TAB_BLARG) {
    			@Override
    			public Item getTabIconItem() {
    				return Item.getItemFromBlock(Blocks.crafting_table);
    			}
    		};    		
    	}
    	
    	return BlargsToolkit.tabBlargsWorkshop;
    }
}
