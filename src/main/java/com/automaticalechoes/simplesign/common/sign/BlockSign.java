package com.automaticalechoes.simplesign.common.sign;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class BlockSign implements Sign {
    public static final String BLOCK_POS = "block_pos";
    public static final String BLOCK_TYPE = "block_type";
    private final BlockPos blockPos;
    private final ResourceLocation blockType;

    public BlockSign(BlockPos blockPos, ResourceLocation resourceLocation){
        this.blockPos = blockPos;
        this.blockType = resourceLocation;
    }

    public BlockSign(CompoundTag compoundTag){
        long aLong = compoundTag.getLong(BLOCK_POS);
        this.blockPos = BlockPos.of(aLong);
        this.blockType = ResourceLocation.tryParse(compoundTag.getString(BLOCK_TYPE));
    }

    public CompoundTag CreateTag(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putLong(BLOCK_POS,blockPos.asLong());
        compoundTag.putString(BLOCK_TYPE,blockType.toString());
        return compoundTag;
    }


    @Override
    public Boolean CanUse() {
        Minecraft instance = Minecraft.getInstance();
        return instance.player.position().subtract(blockPos.getCenter()).length() > 64.0D || instance.level.getBlockState(this.blockPos).is(ForgeRegistries.BLOCKS.getValue(this.blockType));
    }

    @Override
    public Color getColor() {
        return new Color(blockPos.hashCode());
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Vec3 getPointPos(){
        return blockPos.getCenter();
    }

    @Override
    public Type getMarkType() {
        return BLOCK;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) return false;
        BlockSign obj1 = (BlockSign) obj;
        return obj1.blockPos.equals(this.blockPos) && obj1.blockType.equals(this.blockType);
    }
}
