package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.mixin.client.particle.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.particle.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import org.jetbrains.annotations.*;
import org.lwjgl.glfw.*;

import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public class ParticleSpawnerScreen extends HandledScreen<ParticleSpawnerScreenHandler> {
	
	/**
	 * Defines an entry that appears in the particle spawners gui to be selected as particle
	 * Theoretically the particle spawner can spawn all kinds of particle (my modifying its nbt)
	 * But we are limiting us to a few reasonable ones there
	 *
	 * @param particleType      The particle type to dynamically fetch textures from
	 * @param unlockIdentifier  The advancement identifier required to being able to select this entry
	 * @param textureIdentifier The texture shown in its gui entry
	 */
	public record ParticleSpawnerEntry(ParticleType<?> particleType, @Nullable Identifier unlockIdentifier,
									   Identifier textureIdentifier) {
	}
	
	private static final List<ParticleSpawnerEntry> PARTICLE_ENTRIES = new ArrayList<>() {{
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.WHITE_SPARKLE_RISING, null, SpectrumCommon.locate("particle/white_sparkle")));
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, SpectrumCommon.locate("collect_shimmerstone"), SpectrumCommon.locate("particle/shimmerstone_sparkle")));
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, SpectrumCommon.locate("midgame/enter_liquid_crystal"), SpectrumCommon.locate("particle/liquid_crystal_sparkle")));
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.SHOOTING_STAR, SpectrumCommon.locate("collect_star_fragment"), SpectrumCommon.locate("particle/shooting_star")));
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.VOID_FOG, null, SpectrumCommon.locate("particle/void_fog")));
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.AZURE_DIKE_RUNES, SpectrumCommon.locate("midgame/get_azure_dike_charge"), SpectrumCommon.locate("particle/azure_dike_rune_0")));
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.DRAKEBLOOD_DIKE_RUNES, SpectrumCommon.locate("impossible"), SpectrumCommon.locate("particle/void_fog"))); // TODO: unlock
		add(new ParticleSpawnerEntry(SpectrumParticleTypes.MALACHITE_DIKE_RUNES, SpectrumCommon.locate("impossible"), SpectrumCommon.locate("particle/void_fog"))); // TODO: unlock
		add(new ParticleSpawnerEntry(ParticleTypes.ANGRY_VILLAGER, null, new Identifier("particle/angry")));
		add(new ParticleSpawnerEntry(ParticleTypes.EFFECT, null, new Identifier("particle/effect_5")));
		add(new ParticleSpawnerEntry(ParticleTypes.ELECTRIC_SPARK, null, new Identifier("particle/glow")));
		add(new ParticleSpawnerEntry(ParticleTypes.END_ROD, null, new Identifier("particle/glitter_7")));
		add(new ParticleSpawnerEntry(ParticleTypes.HAPPY_VILLAGER, null, new Identifier("particle/glint")));
		add(new ParticleSpawnerEntry(ParticleTypes.HEART, null, new Identifier("particle/heart")));
		add(new ParticleSpawnerEntry(ParticleTypes.DAMAGE_INDICATOR, null, new Identifier("particle/damage")));
		add(new ParticleSpawnerEntry(ParticleTypes.LAVA, null, new Identifier("particle/lava")));
		add(new ParticleSpawnerEntry(ParticleTypes.FLAME, null, new Identifier("particle/flame")));
		add(new ParticleSpawnerEntry(ParticleTypes.FIREWORK, null, new Identifier("particle/spark_2")));
		add(new ParticleSpawnerEntry(ParticleTypes.CRIT, null, new Identifier("particle/critical_hit")));
		add(new ParticleSpawnerEntry(ParticleTypes.CLOUD, null, new Identifier("particle/generic_7")));
		add(new ParticleSpawnerEntry(ParticleTypes.NAUTILUS, null, new Identifier("particle/nautilus")));
		add(new ParticleSpawnerEntry(ParticleTypes.NOTE, null, new Identifier("particle/note")));
		add(new ParticleSpawnerEntry(ParticleTypes.BUBBLE, null, new Identifier("particle/bubble")));
		add(new ParticleSpawnerEntry(ParticleTypes.CAMPFIRE_COSY_SMOKE, null, new Identifier("particle/big_smoke_3")));
		add(new ParticleSpawnerEntry(ParticleTypes.SCULK_CHARGE_POP, null, new Identifier("particle/sculk_charge_pop_1")));
		add(new ParticleSpawnerEntry(ParticleTypes.SCULK_SOUL, null, new Identifier("particle/sculk_soul_1")));
	}};
	
	private static final Identifier GUI_TEXTURE = SpectrumCommon.locate("textures/gui/container/particle_spawner.png");
	private static final int PARTICLES_PER_PAGE = 6;
	
	int textColor = 2236962;
	boolean collisionsEnabled = false;
	int activeParticlePage = 0;
	int particleSelectionIndex = 0;
	private List<ClickableWidget> selectableWidgets;
	private TextFieldWidget amountField;
	private TextFieldWidget positionXField;
	private TextFieldWidget positionYField;
	private TextFieldWidget positionZField;
	private TextFieldWidget positionXVarianceField;
	private TextFieldWidget positionYVarianceField;
	private TextFieldWidget positionZVarianceField;
	private TextFieldWidget velocityXField;
	private TextFieldWidget velocityYField;
	private TextFieldWidget velocityZField;
	private TextFieldWidget velocityXVarianceField;
	private TextFieldWidget velocityYVarianceField;
	private TextFieldWidget velocityZVarianceField;
	private TextFieldWidget scale;
	private TextFieldWidget scaleVariance;
	private TextFieldWidget duration;
	private TextFieldWidget durationVariance;
	private TextFieldWidget gravity;
	private ButtonWidget collisionsButton;
	private ButtonWidget backButton;
	private ButtonWidget forwardButton;
	private List<ButtonWidget> particleButtons;
	
	SpriteAtlasTexture spriteAtlasTexture;
	
	private static final List<ParticleSpawnerEntry> displayedParticleEntries = new ArrayList<>();
	
	public ParticleSpawnerScreen(ParticleSpawnerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		
		this.titleX = 48;
		this.titleY = 7;
		this.backgroundHeight = 222;
		this.selectableWidgets = new ArrayList<>();
		
	}
	
	protected void init() {
		super.init();
		
		client.keyboard.setRepeatEvents(true);
		setupInputFields(handler.getBlockEntity());
		setInitialFocus(amountField);
		this.spriteAtlasTexture = ((ParticleManagerAccessor) client.particleManager).getParticleAtlasTexture();
		
		for (ParticleSpawnerEntry entry : PARTICLE_ENTRIES) {
			if (AdvancementHelper.hasAdvancement(client.player, entry.unlockIdentifier)) {
				displayedParticleEntries.add(entry);
			}
		}
	}
	
	public void removed() {
		super.removed();
		client.keyboard.setRepeatEvents(false);
	}
	
	public void handledScreenTick() {
		super.handledScreenTick();
		
		for (ClickableWidget widget : selectableWidgets) {
			if (widget instanceof TextFieldWidget) {
				((TextFieldWidget) widget).tick();
			}
		}
	}
	
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			client.player.closeHandledScreen();
		}
		
		Element focusedElement = getFocused();
		if (focusedElement instanceof TextFieldWidget) {
			if (keyCode == GLFW.GLFW_KEY_TAB) {
				int currentIndex = selectableWidgets.indexOf(focusedElement);
				((TextFieldWidget) focusedElement).setTextFieldFocused(false);
				
				if (modifiers == 1) {
					setFocused(selectableWidgets.get((selectableWidgets.size() + currentIndex - 1) % selectableWidgets.size()));
				} else {
					setFocused(selectableWidgets.get((currentIndex + 1) % selectableWidgets.size()));
				}
			}
			return focusedElement.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		
		RenderSystem.disableBlend();
		renderForeground(matrices, mouseX, mouseY, delta);
		drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
	
	public void renderForeground(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (ClickableWidget widget : selectableWidgets) {
			if (widget instanceof TextFieldWidget) {
				widget.render(matrices, mouseX, mouseY, delta);
			}
		}
	}
	
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		this.textRenderer.draw(matrices, this.title, (float) this.titleX, (float) this.titleY, 2236962);
		
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.particle_count"), 10, 50, textColor);
		this.textRenderer.draw(matrices, Text.literal("x"), 66, 64, textColor);
		this.textRenderer.draw(matrices, Text.literal("y"), 99, 64, textColor);
		this.textRenderer.draw(matrices, Text.literal("z"), 134, 64, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.offset"), 10, 78, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.variance"), 21, 97, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.velocity"), 10, 117, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.variance"), 21, 137, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.scale"), 10, 161, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.variance"), 91, 161, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.duration"), 10, 181, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.variance"), 91, 181, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.gravity"), 10, 201, textColor);
		this.textRenderer.draw(matrices, Text.translatable("block.spectrum.particle_spawner.collisions"), 90, 201, textColor);
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		int x = (this.width - this.backgroundWidth) / 2;
		int y = (this.height - this.backgroundHeight) / 2;
		
		// the background
		drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		
		// the checked button, if enabled
		if (collisionsEnabled) {
			drawTexture(matrices, x + 146, y + 197, 0, 222, 16, 16);
		}
		
		if (particleSelectionIndex / PARTICLES_PER_PAGE == activeParticlePage) {
			// particle selection outline
			drawTexture(matrices, x + 27 + (20 * (particleSelectionIndex % PARTICLES_PER_PAGE)), y + 19, 16, 222, 22, 22);
		}
		
		RenderSystem.setShaderTexture(0, spriteAtlasTexture.getId());
		int firstDisplayedEntryId = PARTICLES_PER_PAGE * activeParticlePage;
		for (int j = 0; j < PARTICLES_PER_PAGE; j++) {
			int spriteIndex = firstDisplayedEntryId + j;
			if (spriteIndex >= displayedParticleEntries.size()) {
				break;
			}
			Sprite particleSprite = spriteAtlasTexture.getSprite(displayedParticleEntries.get(spriteIndex).textureIdentifier);
			drawSprite(matrices, x + 38 + j * 20 - particleSprite.getHeight() / 2, y + 31 - particleSprite.getHeight() / 2, 0, particleSprite.getWidth(), particleSprite.getHeight(), particleSprite);
		}
	}
	
	protected void setupInputFields(ParticleSpawnerBlockEntity blockEntity) {
		client.keyboard.setRepeatEvents(true);
		int i = (this.width - this.backgroundWidth) / 2 + 3;
		int j = (this.height - this.backgroundHeight) / 2 + 3;
		
		amountField = addTextFieldWidget(i + 110, j + 47, Text.literal("Amount"), String.valueOf(blockEntity.particlesPerSecond), this::isPositiveDecimalNumberUnderThousand);
		positionXField = addTextFieldWidget(i + 61, j + 74, Text.literal("X Position"), String.valueOf(blockEntity.particleSourcePosition.getX()), this::isAbsoluteDecimalNumberThousand);
		positionYField = addTextFieldWidget(i + 96, j + 74, Text.literal("Y Position"), String.valueOf(blockEntity.particleSourcePosition.getY()), this::isAbsoluteDecimalNumberThousand);
		positionZField = addTextFieldWidget(i + 131, j + 74, Text.literal("Z Position"), String.valueOf(blockEntity.particleSourcePosition.getZ()), this::isAbsoluteDecimalNumberThousand);
		positionXVarianceField = addTextFieldWidget(i + 69, j + 94, Text.literal("X Position Variance"), String.valueOf(blockEntity.particleSourcePositionVariance.getX()), this::isAbsoluteDecimalNumberThousand);
		positionYVarianceField = addTextFieldWidget(i + 104, j + 94, Text.literal("Y Position Variance"), String.valueOf(blockEntity.particleSourcePositionVariance.getY()), this::isAbsoluteDecimalNumberThousand);
		positionZVarianceField = addTextFieldWidget(i + 140, j + 94, Text.literal("Z Position Variance"), String.valueOf(blockEntity.particleSourcePositionVariance.getZ()), this::isAbsoluteDecimalNumberThousand);
		velocityXField = addTextFieldWidget(i + 61, j + 114, Text.literal("X Velocity"), String.valueOf(blockEntity.velocity.getX()), this::isAbsoluteDecimalNumberThousand);
		velocityYField = addTextFieldWidget(i + 96, j + 114, Text.literal("Y Velocity"), String.valueOf(blockEntity.velocity.getY()), this::isAbsoluteDecimalNumberThousand);
		velocityZField = addTextFieldWidget(i + 131, j + 114, Text.literal("Z Velocity"), String.valueOf(blockEntity.velocity.getZ()), this::isAbsoluteDecimalNumberThousand);
		velocityXVarianceField = addTextFieldWidget(i + 69, j + 134, Text.literal("X Velocity Variance"), String.valueOf(blockEntity.velocityVariance.getX()), this::isAbsoluteDecimalNumberThousand);
		velocityYVarianceField = addTextFieldWidget(i + 104, j + 134, Text.literal("Y Velocity Variance"), String.valueOf(blockEntity.velocityVariance.getY()), this::isAbsoluteDecimalNumberThousand);
		velocityZVarianceField = addTextFieldWidget(i + 140, j + 134, Text.literal("Z Velocity Variance"), String.valueOf(blockEntity.velocityVariance.getZ()), this::isAbsoluteDecimalNumberThousand);
		scale = addTextFieldWidget(i + 55, j + 158, Text.literal("Scale"), String.valueOf(blockEntity.scale), this::isPositiveDecimalNumberUnderTen);
		scaleVariance = addTextFieldWidget(i + 139, j + 158, Text.literal("Scale Variance"), String.valueOf(blockEntity.scaleVariance), this::isPositiveDecimalNumberUnderTen);
		duration = addTextFieldWidget(i + 55, j + 178, Text.literal("Duration"), String.valueOf(blockEntity.lifetimeTicks), this::isPositiveWholeNumberUnderThousand);
		durationVariance = addTextFieldWidget(i + 139, j + 178, Text.literal("Duration Variance"), String.valueOf(blockEntity.lifetimeVariance), this::isPositiveWholeNumberUnderThousand);
		gravity = addTextFieldWidget(i + 55, j + 198, Text.literal("Gravity"), String.valueOf(blockEntity.gravity), this::isBetweenZeroAndOne);
		
		collisionsButton = new ButtonWidget(i + 142, j + 194, 16, 16, Text.literal("Collisions"), this::collisionButtonPressed);
		collisionsEnabled = blockEntity.collisions;
		addSelectableChild(collisionsButton);
		
		selectableWidgets = new ArrayList<>() {{
			add(amountField);
			add(positionXField);
			add(positionYField);
			add(positionZField);
			add(positionXVarianceField);
			add(positionYVarianceField);
			add(positionZVarianceField);
			add(velocityXField);
			add(velocityYField);
			add(velocityZField);
			add(velocityXVarianceField);
			add(velocityYVarianceField);
			add(velocityZVarianceField);
			add(scale);
			add(scaleVariance);
			add(duration);
			add(durationVariance);
			add(gravity);
			add(collisionsButton);
		}};
		
		backButton = new ButtonWidget(i + 11, j + 19, 12, 14, Text.literal("back"), this::navigationButtonPressed);
		addSelectableChild(backButton);
		forwardButton = new ButtonWidget(i + 147, j + 19, 12, 14, Text.literal("forward"), this::navigationButtonPressed);
		addSelectableChild(forwardButton);
		
		particleButtons = List.of(
				addParticleButton(i + 23, j + 16),
				addParticleButton(i + 23 + 20, j + 16),
				addParticleButton(i + 23 + 40, j + 16),
				addParticleButton(i + 23 + 60, j + 16),
				addParticleButton(i + 23 + 80, j + 16),
				addParticleButton(i + 23 + 100, j + 16)
		);
		
		this.particleSelectionIndex = 0;
		int particleIndex = 0;
		for (ParticleSpawnerEntry availableParticle : displayedParticleEntries) {
			if (availableParticle.textureIdentifier.equals(blockEntity.particleType)) {
				this.particleSelectionIndex = particleIndex;
				break;
			}
			particleIndex++;
		}
	}
	
	private void navigationButtonPressed(ButtonWidget buttonWidget) {
		int pageCount = displayedParticleEntries.size() / PARTICLES_PER_PAGE;
		if (buttonWidget == forwardButton) {
			activeParticlePage = (activeParticlePage + 1) % pageCount;
		} else {
			activeParticlePage = (activeParticlePage - 1 + pageCount) % pageCount;
		}
	}
	
	private @NotNull TextFieldWidget addTextFieldWidget(int x, int y, Text text, String defaultText, Predicate<String> textPredicate) {
		TextFieldWidget textFieldWidget = new TextFieldWidget(this.textRenderer, x, y, 31, 16, text);
		
		textFieldWidget.setTextPredicate(textPredicate);
		textFieldWidget.setFocusUnlocked(true);
		textFieldWidget.setEditable(true);
		textFieldWidget.setEditableColor(-1);
		textFieldWidget.setUneditableColor(-1);
		textFieldWidget.setDrawsBackground(false);
		textFieldWidget.setMaxLength(10);
		textFieldWidget.setText(defaultText);
		textFieldWidget.setChangedListener(this::onTextBoxValueChanged);
		addSelectableChild(textFieldWidget);
		
		return textFieldWidget;
	}
	
	private @NotNull ButtonWidget addParticleButton(int x, int y) {
		ButtonWidget button = new ButtonWidget(x, y, 20, 20, Text.literal("Particles"), this::particleButtonPressed);
		addSelectableChild(button);
		return button;
	}
	
	private void particleButtonPressed(ButtonWidget buttonWidget) {
		int buttonIndex = particleButtons.indexOf(buttonWidget);
		int newIndex = PARTICLES_PER_PAGE * activeParticlePage + buttonIndex;
		
		if (newIndex < displayedParticleEntries.size()) {
			particleSelectionIndex = newIndex;
			onValuesChanged();
		}
	}
	
	private void collisionButtonPressed(ButtonWidget buttonWidget) {
		collisionsEnabled = !collisionsEnabled;
		this.onValuesChanged();
	}
	
	private void onTextBoxValueChanged(@NotNull String newValue) {
		onValuesChanged();
	}
	
	protected boolean isDecimalNumber(@NotNull String text) {
		return text.matches("^(-)?\\d*+(?:\\.\\d*)?$");
	}
	
	private boolean isPositiveDecimalNumberUnderThousand(String text) {
		try {
			return Double.parseDouble(text) < 1000;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isAbsoluteDecimalNumberThousand(String text) {
		try {
			return Math.abs(Double.parseDouble(text)) < 1000;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isPositiveDecimalNumberUnderTen(String text) {
		try {
			return Double.parseDouble(text) < 10;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	protected boolean isPositiveDecimalNumber(@NotNull String text) {
		return text.matches("^\\d*+(?:\\.\\d*)?$");
	}
	
	protected boolean isPositiveWholeNumber(@NotNull String text) {
		return text.matches("^\\d*$");
	}
	
	protected boolean isPositiveWholeNumberUnderThousand(@NotNull String text) {
		try {
			return Integer.parseInt(text) < 1000;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	protected boolean isBetweenZeroAndOne(@NotNull String text) {
		try {
			float f = Float.parseFloat(text);
			return f >= 0 && f <= 1;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Send these changes to the server to distribute to all clients
	 */
	private void onValuesChanged() {
		try {
			PacketByteBuf packetByteBuf = PacketByteBufs.create();
			writeSettings(packetByteBuf);
			ClientPlayNetworking.send(SpectrumC2SPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID, packetByteBuf);
		} catch (Exception e) {
			// the text boxes currently are not able to be parsed yet.
			// wait until everything is set up
		}
	}
	
	private void writeSettings(@NotNull PacketByteBuf packetByteBuf) {
		packetByteBuf.writeString(Registry.PARTICLE_TYPE.getId(displayedParticleEntries.get(particleSelectionIndex).particleType).toString());
		packetByteBuf.writeFloat(Float.parseFloat(amountField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(positionXField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(positionYField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(positionZField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(positionXVarianceField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(positionYVarianceField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(positionZVarianceField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(velocityXField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(velocityYField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(velocityZField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(velocityXVarianceField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(velocityYVarianceField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(velocityZVarianceField.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(scale.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(scaleVariance.getText()));
		packetByteBuf.writeInt(Integer.parseInt(duration.getText()));
		packetByteBuf.writeInt(Integer.parseInt(durationVariance.getText()));
		packetByteBuf.writeFloat(Float.parseFloat(gravity.getText()));
		packetByteBuf.writeBoolean(collisionsEnabled);
	}
	
}
