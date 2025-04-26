package org.automaticalechoes.simplesign.api.sign.target;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public record BlockTarget(BlockPos blockPos, ResourceLocation blockType, boolean hasBlockEntity, @Nullable ItemStack itemStack, List<Component> toolTipComponents) implements SignalTarget {
    public static final String BLOCK_POS = "block_pos";
    public static final String BLOCK_TYPE = "block_type";
    public static final String HAS_BLOCK_ENTITY = "has_block_entity";

    public static BlockTarget Create(BlockPos blockPos, ResourceLocation blockType, boolean hasBlockEntity, ItemStack itemStack) {
        return new BlockTarget(blockPos, blockType, hasBlockEntity, itemStack, Screen.getTooltipFromItem(Minecraft.getInstance(), itemStack));
    }

    public static BlockTarget Create(BlockPos blockPos, ResourceLocation blockType, boolean hasBlockEntity) {
        return BlockTarget.Create(blockPos, blockType, hasBlockEntity, SetupItem(blockType));
    }

    public static BlockTarget Create(BlockPos blockPos, ResourceLocation blockType) {
        return BlockTarget.Create(blockPos, blockType, false);
    }

    public static BlockTarget FromTag(CompoundTag compoundTag){
        long aLong = compoundTag.getLong(BLOCK_POS);
        BlockPos blockPos = BlockPos.of(aLong);
        ResourceLocation blockType = ResourceLocation.tryParse(compoundTag.getString(BLOCK_TYPE));
        boolean hasBlockEntity = compoundTag.getBoolean(HAS_BLOCK_ENTITY);
        return BlockTarget.Create(blockPos, blockType, hasBlockEntity);
    }

    public CompoundTag CreateTag(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(TARGET_TYPE, BLOCK);
        compoundTag.putLong(BLOCK_POS,blockPos.asLong());
        compoundTag.putString(BLOCK_TYPE,blockType.toString());
        compoundTag.putBoolean(HAS_BLOCK_ENTITY,hasBlockEntity);
        return compoundTag;
    }

    @Override
    public Boolean CanUse() {
        Minecraft instance = Minecraft.getInstance();
        return instance.player.position().subtract(blockPos.getCenter()).length() > 64.0D || instance.level.getBlockState(this.blockPos).is(BuiltInRegistries.BLOCK.get(this.blockType));
    }

    @Override
    public Color getColor() {
        return new Color(blockPos.hashCode());
    }

    public Vec3 getPointPos(){
        return blockPos.getCenter();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass()) return false;
        BlockTarget obj1 = (BlockTarget) obj;
        return obj1.blockPos.equals(this.blockPos) && obj1.blockType.equals(this.blockType);
    }

    static ItemStack SetupItem(ResourceLocation blockType) {
        Block block = BuiltInRegistries.BLOCK.get(blockType);
        return block.asItem().getDefaultInstance();
    }


}
