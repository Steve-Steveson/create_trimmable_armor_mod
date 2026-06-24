package net.steveson.createtrimmable.mixin;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorTrim.class)
public class AdjustedArmorTrimMixin {


	@Inject(at = @At("HEAD"), method = "getMaterialAssetNameFor")
	private void getColorDarkerPaletteSuffix(ArmorMaterial armorMaterial, CallbackInfoReturnable<String> cir) {
		// This code is injected into the start of MinecraftServer.loadLevel()V
	}


}