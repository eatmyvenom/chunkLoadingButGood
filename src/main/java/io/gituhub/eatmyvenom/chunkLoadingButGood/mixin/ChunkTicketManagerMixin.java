package io.gituhub.eatmyvenom.chunkLoadingButGood.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.collection.SortedArraySet;
import net.minecraft.util.math.ChunkPos;

@Mixin(ChunkTicketManager.class)
public class ChunkTicketManagerMixin {

    private final static ChunkTicketType<ChunkPos> UNKNOWN_TICKET = ChunkTicketType.field_14032;

    @ModifyVariable(at = @At("HEAD"), method = "addTicketWithLevel", index = 3)
    private <T> int addTicketWithLevel(ChunkTicketType<T> type, ChunkPos pos, int level, T argument) {
        if(type.equals(UNKNOWN_TICKET) && shouldUpgrade(pos, level)) {
            return level-=4;
        }
        return level;
    }

    public int getLevelAtPos(long pos) {
        return !this.getTicketSet(pos).isEmpty() ? (this.getTicketSet(pos).first()).getLevel() : ThreadedAnvilChunkStorage.MAX_LEVEL + 10;
    }

    public int getLevelAtPos(ChunkPos pos) {
        return getLevelAtPos(pos.toLong());
    }

    public boolean shouldUpgrade(ChunkPos pos, int currentLevel) {
        return 
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x + 1, pos.z)) &&
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x, pos.z + 1)) &&
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x - 1, pos.z)) &&
            currentLevel >= getLevelAtPos(new ChunkPos(pos.x, pos.z - 1));
    }

    @Shadow
    private static int getLevel(SortedArraySet<ChunkTicket<?>> sortedArraySet) {
        return 0;
    }

    @Shadow
    private SortedArraySet<ChunkTicket<?>> getTicketSet(long position) {
        return null;
    }
}