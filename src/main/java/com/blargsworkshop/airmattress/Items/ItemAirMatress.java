package com.blargsworkshop.airmattress.Items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.blargsworkshop.airmattress.AirMattressMod;
import com.blargsworkshop.airmattress.Blocks.BlockAirMattress;

public class ItemAirMatress extends Item {

    public ItemAirMatress()
    {
    	this.setMaxStackSize(1);
    	this.setCreativeTab(AirMattressMod.tabSleepingbag);
    	this.setUnlocalizedName("basicairmattress");
    	this.setTextureName("airmattressmod:airmattress");
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer player, World world, int x, int y, int z, int dimension, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (world.isRemote)
        {
            return true;
        }
        else if (dimension != 1)
        {
            return false;
        }
        else
        {
            ++y;
            BlockAirMattress blockAirMattress = (BlockAirMattress)AirMattressMod.airMattressBlock;
            int i1 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;

            if (i1 == 0)
            {
                b1 = 1;
            }

            if (i1 == 1)
            {
                b0 = -1;
            }

            if (i1 == 2)
            {
                b1 = -1;
            }

            if (i1 == 3)
            {
                b0 = 1;
            }
            
            if (player.canPlayerEdit(x, y, z, dimension, p_77648_1_) && player.canPlayerEdit(x + b0, y, z + b1, dimension, p_77648_1_))
            {
                if (world.isAirBlock(x, y, z) && world.isAirBlock(x + b0, y, z + b1) && World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && World.doesBlockHaveSolidTopSurface(world, x + b0, y - 1, z + b1))
                {
                    world.setBlock(x, y, z, blockAirMattress, i1, 3);

                    if (world.getBlock(x, y, z) == blockAirMattress)
                    {
                        world.setBlock(x + b0, y, z + b1, blockAirMattress, i1 + 8, 3);
                    }

                    --p_77648_1_.stackSize;
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
