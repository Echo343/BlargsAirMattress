package com.blargsworkshop.airmattress.Blocks;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import com.blargsworkshop.airmattress.AirMattressMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAirMattress extends BlockDirectional {

	public BlockAirMattress() {
		super(Material.cloth);
        this.setBlockBounds();
    	this.setHardness(0.01f);
    	this.setStepSound(Block.soundTypeCloth);
    	this.setBlockName("genericAirMattress");
    	this.setBlockTextureName("airmattressmod:airmattress");
	}

    public static final int[][] field_149981_a = new int[][] {{0, 1}, { -1, 0}, {0, -1}, {1, 0}};
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149980_b;
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149982_M;
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149983_N;

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            int i1 = world.getBlockMetadata(x, y, z);

            if (!isBlockHeadOfBed(i1))
            {
                int j1 = getDirection(i1);
                x += field_149981_a[j1][0];
                z += field_149981_a[j1][1];

                if (world.getBlock(x, y, z) != this)
                {
                    return true;
                }

                i1 = world.getBlockMetadata(x, y, z);
            }

            if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell)
            {
                if (func_149976_c(i1))
                {
                    EntityPlayer entityplayer1 = null;
                    Iterator iterator = world.playerEntities.iterator();

                    while (iterator.hasNext())
                    {
                        EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();

                        if (entityplayer2.isPlayerSleeping())
                        {
                            ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;

                            if (chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z)
                            {
                                entityplayer1 = entityplayer2;
                            }
                        }
                    }

                    if (entityplayer1 != null)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                        return true;
                    }

                    func_149979_a(world, x, y, z, false);
                }

                EntityPlayer.EnumStatus enumstatus = player.sleepInBedAt(x, y, z);

                if (enumstatus == EntityPlayer.EnumStatus.OK)
                {
                    func_149979_a(world, x, y, z, true);
                    return true;
                }
                else
                {
                    if (enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
                    }
                    else if (enumstatus == EntityPlayer.EnumStatus.NOT_SAFE)
                    {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
                    }

                    return true;
                }
            }
            else
            {
                double d2 = (double)x + 0.5D;
                double d0 = (double)y + 0.5D;
                double d1 = (double)z + 0.5D;
                world.setBlockToAir(x, y, z);
                int k1 = getDirection(i1);
                x += field_149981_a[k1][0];
                z += field_149981_a[k1][1];

                if (world.getBlock(x, y, z) == this)
                {
                    world.setBlockToAir(x, y, z);
                    d2 = (d2 + (double)x + 0.5D) / 2.0D;
                    d0 = (d0 + (double)y + 0.5D) / 2.0D;
                    d1 = (d1 + (double)z + 0.5D) / 2.0D;
                }

                world.newExplosion((Entity)null, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 5.0F, true, true);
                return true;
            }
        }
    }
    
    @Override
    public boolean isBed(IBlockAccess world, int x, int y, int z, net.minecraft.entity.EntityLivingBase player) 
    {
    	return true;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 0)
        {
            return Blocks.planks.getBlockTextureFromSide(side);
        }
        else
        {
            int k = getDirection(meta);
            int l = Direction.bedDirection[k][side];
            int i1 = isBlockHeadOfBed(meta) ? 1 : 0;
            return (i1 != 1 || l != 2) && (i1 != 0 || l != 3) ? (l != 5 && l != 4 ? this.field_149983_N[i1] : this.field_149982_M[i1]) : this.field_149980_b[i1];
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        this.field_149983_N = new IIcon[] {register.registerIcon(this.getTextureName() + "_feet_top"), register.registerIcon(this.getTextureName() + "_head_top")};
        this.field_149980_b = new IIcon[] {register.registerIcon(this.getTextureName() + "_feet_end"), register.registerIcon(this.getTextureName() + "_head_end")};
        this.field_149982_M = new IIcon[] {register.registerIcon(this.getTextureName() + "_feet_side"), register.registerIcon(this.getTextureName() + "_head_side")};
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 14;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        this.setBlockBounds();
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_)
    {
        int l = world.getBlockMetadata(x, y, z);
        int i1 = getDirection(l);

        if (isBlockHeadOfBed(l))
        {
            if (world.getBlock(x - field_149981_a[i1][0], y, z - field_149981_a[i1][1]) != this)
            {
                world.setBlockToAir(x, y, z);
            }
        }
        else if (world.getBlock(x + field_149981_a[i1][0], y, z + field_149981_a[i1][1]) != this)
        {
            world.setBlockToAir(x, y, z);

            if (!world.isRemote)
            {
                this.dropBlockAsItem(world, x, y, z, l, 0);
            }
        }
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        /**
         * Returns whether or not this bed block is the head of the bed.
         */
        return isBlockHeadOfBed(p_149650_1_) ? Item.getItemById(0) : AirMattressMod.airMattressItem;
    }

    private void setBlockBounds()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
    }

    /**
     * Returns whether or not this bed block is the head of the bed.
     */
    public static boolean isBlockHeadOfBed(int p_149975_0_)
    {
        return (p_149975_0_ & 8) != 0;
    }

    public static boolean func_149976_c(int p_149976_0_)
    {
        return (p_149976_0_ & 4) != 0;
    }

    public static void func_149979_a(World world, int x, int y, int z, boolean p_149979_4_)
    {
        int l = world.getBlockMetadata(x, y, z);

        if (p_149979_4_)
        {
            l |= 4;
        }
        else
        {
            l &= -5;
        }

        world.setBlockMetadataWithNotify(x, y, z, l, 4);
    }

    public static ChunkCoordinates func_149977_a(World world, int x, int y, int z, int p_149977_4_)
    {
        int i1 = world.getBlockMetadata(x, y, z);
        int j1 = BlockDirectional.getDirection(i1);

        for (int k1 = 0; k1 <= 1; ++k1)
        {
            int l1 = x - field_149981_a[j1][0] * k1 - 1;
            int i2 = z - field_149981_a[j1][1] * k1 - 1;
            int j2 = l1 + 2;
            int k2 = i2 + 2;

            for (int l2 = l1; l2 <= j2; ++l2)
            {
                for (int i3 = i2; i3 <= k2; ++i3)
                {
                    if (World.doesBlockHaveSolidTopSurface(world, l2, y - 1, i3) && !world.getBlock(l2, y, i3).getMaterial().isOpaque() && !world.getBlock(l2, y + 1, i3).getMaterial().isOpaque())
                    {
                        if (p_149977_4_ <= 0)
                        {
                            return new ChunkCoordinates(l2, y, i3);
                        }

                        --p_149977_4_;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {
        if (!isBlockHeadOfBed(p_149690_5_))
        {
            super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_, 0);
        }
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int getMobilityFlag()
    {
        return 1;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return AirMattressMod.airMattressItem;
    }

    /**
     * Called when the block is attempted to be harvested
     */
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && isBlockHeadOfBed(meta))
        {
            int i1 = getDirection(meta);
            x -= field_149981_a[i1][0];
            z -= field_149981_a[i1][1];

            if (world.getBlock(x, y, z) == this)
            {
                world.setBlockToAir(x, y, z);
            }
        }
    }


}
