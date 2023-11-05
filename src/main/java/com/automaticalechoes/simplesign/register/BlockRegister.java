package com.automaticalechoes.simplesign.register;

import com.automaticalechoes.simplesign.SimpleSign;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleSign.MODID);

    public static final RegistryObject<Block> MARK_BLOCK  = BLOCKS.register("mark_block",() -> new Block(BlockBehaviour
            .Properties.of().noOcclusion().sound(SoundType.WOOD)));
}
