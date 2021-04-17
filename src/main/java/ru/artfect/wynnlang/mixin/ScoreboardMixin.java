package ru.artfect.wynnlang.mixin;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {
    @Inject(method = "removePlayerFromTeam", at = @At("HEAD"), cancellable = true)
    public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam, CallbackInfo ci) {
        if (playerTeam == null)
            ci.cancel();
    }
}
