package io.gituhub.eatmyvenom.chunkLoadingButGood.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.gituhub.eatmyvenom.chunkLoadingButGood.Promotable;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.util.math.ChunkPos;

@Mixin(ChunkHolder.class)
public class ChunkHolderMixin implements Promotable {
    
    boolean promotable;

    @Shadow
    int level;

    @Shadow
    ChunkPos pos;

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ChunkHolder;level:I", opcode = Opcodes.GETFIELD))
    private int upgradeLevel() {
        if(this.promotable) {
            return 33;
        } else {
            return this.level;
        }

    }

    @Override
    public boolean isPromotable() {
        return promotable;
    }

    @Override
    public void setPromotable(boolean promote) {
        this.promotable = promote;
    }
    
}