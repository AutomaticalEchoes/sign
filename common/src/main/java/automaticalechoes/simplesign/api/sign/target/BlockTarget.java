package automaticalechoes.simplesign.api.sign.target;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.awt.*;

public class BlockTarget implements SignalTarget {
    public static final String BLOCK_POS = "block_pos";
    public static final String BLOCK_TYPE = "block_type";

    private final BlockPos blockPos;
    private final ResourceLocation blockType;

    public BlockTarget(BlockPos blockPos, ResourceLocation resourceLocation){
        this.blockPos = blockPos;
        this.blockType = resourceLocation;
    }

    public BlockTarget(CompoundTag compoundTag){
        long aLong = compoundTag.getLong(BLOCK_POS);
        this.blockPos = BlockPos.of(aLong);
        this.blockType = ResourceLocation.tryParse(compoundTag.getString(BLOCK_TYPE));
    }

    public CompoundTag CreateTag(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(TARGET_TYPE, BLOCK);
        compoundTag.putLong(BLOCK_POS,blockPos.asLong());
        compoundTag.putString(BLOCK_TYPE,blockType.toString());
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

    public BlockPos getBlockPos() {
        return blockPos;
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

    @Override
    public ItemStack getItemStack(){
        Block block = BuiltInRegistries.BLOCK.get(this.blockType);
        return block.asItem().getDefaultInstance();
    }
}
