package io.gituhub.eatmyvenom.chunkLoadingButGood.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.gituhub.eatmyvenom.chunkLoadingButGood.Promotable;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.server.world.ChunkHolder.LevelType;
import net.minecraft.util.math.ChunkPos;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {

    @Inject(at = @At("HEAD"), method = "setLevel")
    public void checkPromotions(long chunk, int level, ChunkHolder holder, int previousLevel, CallbackInfoReturnable<ChunkHolder> ci) {
        if(holder.getLevelType().equals(LevelType.BORDER) && shouldUpgrade(holder.getPos(), level)) {
            ((Promotable)holder).setPromotable(true);
        }
    }

    
    public boolean shouldUpgrade(ChunkPos pos, int currentLevel) {
        return 
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x + 1, pos.z)) &&
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x, pos.z + 1)) &&
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x - 1, pos.z)) &&
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x, pos.z - 1));
    }

    public int getLevelAtPos(ChunkPos pos) {
        return getLevelAtPos(pos.toLong());
    }

    public int getLevelAtPos(long pos) {
        return this.getChunkHolder(pos).getLevel();
    }
    
	@Shadow protected ChunkHolder getChunkHolder(long chunk) {
        return null;
    }
}