package net.steveson.createtrimmable.mixin;

import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.AllArmorMaterials;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(value = ArmorTrim.class, priority = 100)
public abstract class AdjustedArmorTrimMixin {
	@Shadow
	@Final
	private RegistryEntry<ArmorTrimMaterial> material;

	@Shadow
	@Final
	private RegistryEntry<ArmorTrimPattern> pattern;


	@Shadow
	@Final
	private Function<ArmorMaterial, Identifier> leggingsModelIdGetter;

	@Shadow
	@Final
	private  Function<ArmorMaterial, Identifier> genericModelIdGetter;



	@Shadow
	protected abstract String getMaterialAssetNameFor(ArmorMaterial armorMaterial);

	@Inject(at = @At("HEAD"), method = "getMaterialAssetNameFor", cancellable = true)
	private void getColorDarkerPaletteSuffix(ArmorMaterial armorMaterial, CallbackInfoReturnable<String> cir) {
		if (armorMaterial == AllArmorMaterials.COPPER) {
			String armorMatName = armorMaterial.getName();
			String[] armorMatNameSplit = armorMatName.split(":");

			String trimName = this.material.value().assetName();

			if (armorMatNameSplit.length == 2 && armorMatNameSplit[1].equalsIgnoreCase(trimName)) {
				cir.setReturnValue(trimName + "_darker");
			}
		}
	}


	@Unique
	private final BiFunction<Boolean, ArmorMaterial, Identifier> create$textureCardboard = Util.memoize((inner, material) -> {
		String assetPath = pattern.value().assetId().getPath();
		String colorSuffix = getMaterialAssetNameFor(material);
		return Create.asResource("trims/models/armor/card_" + assetPath + (inner ? "_leggings_" : "_") + colorSuffix);
	});

	@Inject(method = "getLeggingsModelId", at = @At("HEAD"), cancellable = true)
	private void create$swapTexturesForCardboardTrimsInner(ArmorMaterial pArmorMaterial, CallbackInfoReturnable<Identifier> cir) {
		if (pArmorMaterial == AllArmorMaterials.CARDBOARD) {
			String assetPath = pattern.value().assetId().getPath();
			boolean textureExists = MinecraftClient.getInstance().getResourceManager()
					.getResource(Create.asResource("textures/trims/models/armor/card_" + assetPath + ".png")).isPresent();

			if (textureExists) {
				cir.setReturnValue(create$textureCardboard.apply(true, pArmorMaterial));
			} else {
				cir.setReturnValue(this.leggingsModelIdGetter.apply(pArmorMaterial));
			}
		}
	}

	@Inject(method = "getGenericModelId", at = @At("HEAD"), cancellable = true)
	private void create$swapTexturesForCardboardTrimsOuter(ArmorMaterial pArmorMaterial, CallbackInfoReturnable<Identifier> cir) {
		if (pArmorMaterial == AllArmorMaterials.CARDBOARD) {
			String assetPath = pattern.value().assetId().getPath();
			boolean textureExists = MinecraftClient.getInstance().getResourceManager()
					.getResource(Create.asResource("textures/trims/models/armor/card_" + assetPath + ".png")).isPresent();

			if (textureExists) {
				cir.setReturnValue(create$textureCardboard.apply(false, pArmorMaterial));
			} else {
				cir.setReturnValue(this.genericModelIdGetter.apply(pArmorMaterial));
			}
		}
	}
}