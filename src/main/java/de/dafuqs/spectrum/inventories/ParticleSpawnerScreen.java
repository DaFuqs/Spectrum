package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.particle_spawner.ParticleSpawnerBlockEntity;
import de.dafuqs.spectrum.networking.SpectrumC2SPacketSender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class ParticleSpawnerScreen extends HandledScreen<ParticleSpawnerScreenHandler> {
	
	private static final Identifier GUI_TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/particle_spawner.png");
	// when adding new particles do not forget adding them both in
	// - the texture file (for the gui)
	// - the available particle list
	private static final Identifier TEXTURE_PARTICLES = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/container/particle_spawner_particles.png");
	private static final int PARTICLES_PER_PAGE = 6;
	private static final List<Identifier> AVAILABLE_PARTICLES = new ArrayList<>() {{
		add(new Identifier(SpectrumCommon.MOD_ID, "particle/sparklestone_sparkle"));
		add(new Identifier(SpectrumCommon.MOD_ID, "particle/shooting_star"));
		add(new Identifier(SpectrumCommon.MOD_ID, "particle/liquid_crystal_sparkle"));
		add(new Identifier(SpectrumCommon.MOD_ID, "particle/void_fog"));
		add(new Identifier("particle/effect_5"));
		add(new Identifier("particle/glint"));
		add(new Identifier("particle/heart"));
		add(new Identifier("particle/damage"));
		add(new Identifier("particle/lava"));
		add(new Identifier("particle/flame"));
		add(new Identifier("particle/spark_2"));
		add(new Identifier("particle/critical_hit"));
		add(new Identifier("particle/nautilus"));
		add(new Identifier("particle/bubble"));
		add(new Identifier("particle/big_smoke_3"));
	}};
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
		
		this.textRenderer.draw(matrices, new LiteralText("Particles / Second"), 10, 50, textColor);
		this.textRenderer.draw(matrices, new LiteralText("x"), 66, 64, textColor);
		this.textRenderer.draw(matrices, new LiteralText("y"), 99, 64, textColor);
		this.textRenderer.draw(matrices, new LiteralText("z"), 134, 64, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Offset"), 10, 78, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Variance"), 21, 97, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Velocity"), 10, 117, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Variance"), 21, 137, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Scale"), 10, 161, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Variance"), 91, 161, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Duration"), 10, 181, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Variance"), 91, 181, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Gravity"), 10, 201, textColor);
		this.textRenderer.draw(matrices, new LiteralText("Collisions"), 90, 201, textColor);
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
		
		RenderSystem.setShaderTexture(0, TEXTURE_PARTICLES);
		drawTexture(matrices, x + 28, y + 21, 0, activeParticlePage * 20, 20 * PARTICLES_PER_PAGE, 20);
	}
	
	protected void setupInputFields(ParticleSpawnerBlockEntity blockEntity) {
		client.keyboard.setRepeatEvents(true);
		int i = (this.width - this.backgroundWidth) / 2 + 3;
		int j = (this.height - this.backgroundHeight) / 2 + 3;
		
		amountField = addTextFieldWidget(i + 110, j + 47, new LiteralText("Amount"), String.valueOf(blockEntity.particlesPerSecond), this::isPositiveDecimalNumberUnderThousand);
		positionXField = addTextFieldWidget(i + 61, j + 74, new LiteralText("X Position"), String.valueOf(blockEntity.particleSourcePosition.getX()), this::isAbsoluteDecimalNumberThousand);
		positionYField = addTextFieldWidget(i + 96, j + 74, new LiteralText("Y Position"), String.valueOf(blockEntity.particleSourcePosition.getY()), this::isAbsoluteDecimalNumberThousand);
		positionZField = addTextFieldWidget(i + 131, j + 74, new LiteralText("Z Position"), String.valueOf(blockEntity.particleSourcePosition.getZ()), this::isAbsoluteDecimalNumberThousand);
		positionXVarianceField = addTextFieldWidget(i + 69, j + 94, new LiteralText("X Position Variance"), String.valueOf(blockEntity.particleSourcePositionVariance.getX()), this::isAbsoluteDecimalNumberThousand);
		positionYVarianceField = addTextFieldWidget(i + 104, j + 94, new LiteralText("Y Position Variance"), String.valueOf(blockEntity.particleSourcePositionVariance.getY()), this::isAbsoluteDecimalNumberThousand);
		positionZVarianceField = addTextFieldWidget(i + 140, j + 94, new LiteralText("Z Position Variance"), String.valueOf(blockEntity.particleSourcePositionVariance.getZ()), this::isAbsoluteDecimalNumberThousand);
		velocityXField = addTextFieldWidget(i + 61, j + 114, new LiteralText("X Velocity"), String.valueOf(blockEntity.velocity.getX()), this::isAbsoluteDecimalNumberThousand);
		velocityYField = addTextFieldWidget(i + 96, j + 114, new LiteralText("Y Velocity"), String.valueOf(blockEntity.velocity.getY()), this::isAbsoluteDecimalNumberThousand);
		velocityZField = addTextFieldWidget(i + 131, j + 114, new LiteralText("Z Velocity"), String.valueOf(blockEntity.velocity.getZ()), this::isAbsoluteDecimalNumberThousand);
		velocityXVarianceField = addTextFieldWidget(i + 69, j + 134, new LiteralText("X Velocity Variance"), String.valueOf(blockEntity.velocityVariance.getX()), this::isAbsoluteDecimalNumberThousand);
		velocityYVarianceField = addTextFieldWidget(i + 104, j + 134, new LiteralText("Y Velocity Variance"), String.valueOf(blockEntity.velocityVariance.getY()), this::isAbsoluteDecimalNumberThousand);
		velocityZVarianceField = addTextFieldWidget(i + 140, j + 134, new LiteralText("Z Velocity Variance"), String.valueOf(blockEntity.velocityVariance.getZ()), this::isAbsoluteDecimalNumberThousand);
		scale = addTextFieldWidget(i + 55, j + 158, new LiteralText("Scale"), String.valueOf(blockEntity.scale), this::isPositiveDecimalNumberUnderTen);
		scaleVariance = addTextFieldWidget(i + 139, j + 158, new LiteralText("Scale Variance"), String.valueOf(blockEntity.scaleVariance), this::isPositiveDecimalNumberUnderTen);
		duration = addTextFieldWidget(i + 55, j + 178, new LiteralText("Duration"), String.valueOf(blockEntity.lifetimeTicks), this::isPositiveWholeNumberUnderThousand);
		durationVariance = addTextFieldWidget(i + 139, j + 178, new LiteralText("Duration Variance"), String.valueOf(blockEntity.lifetimeVariance), this::isPositiveWholeNumberUnderThousand);
		gravity = addTextFieldWidget(i + 55, j + 198, new LiteralText("Gravity"), String.valueOf(blockEntity.gravity), this::isBetweenZeroAndOne);
		
		collisionsButton = new ButtonWidget(i + 142, j + 194, 16, 16, new LiteralText("Collisions"), this::collisionButtonPressed);
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
		
		backButton = new ButtonWidget(i + 11, j + 19, 12, 14, new LiteralText("back"), this::navigationButtonPressed);
		addSelectableChild(backButton);
		forwardButton = new ButtonWidget(i + 147, j + 19, 12, 14, new LiteralText("forward"), this::navigationButtonPressed);
		addSelectableChild(forwardButton);
		
		particleButtons = new ArrayList<>();
		particleButtons.add(addParticleButton(i + 23, j + 16));
		particleButtons.add(addParticleButton(i + 23 + 20, j + 16));
		particleButtons.add(addParticleButton(i + 23 + 40, j + 16));
		particleButtons.add(addParticleButton(i + 23 + 60, j + 16));
		particleButtons.add(addParticleButton(i + 23 + 80, j + 16));
		particleButtons.add(addParticleButton(i + 23 + 100, j + 16));
		
		int selectionIndex = AVAILABLE_PARTICLES.indexOf(blockEntity.particleSpriteIdentifier);
		if (selectionIndex != -1) {
			particleSelectionIndex = selectionIndex;
		}
	}
	
	private void navigationButtonPressed(ButtonWidget buttonWidget) {
		if (buttonWidget == forwardButton) {
			activeParticlePage = (activeParticlePage + 1) % ((AVAILABLE_PARTICLES.size() / PARTICLES_PER_PAGE) + 1);
		} else {
			activeParticlePage = (activeParticlePage - 1) % ((AVAILABLE_PARTICLES.size() / PARTICLES_PER_PAGE) + 1);
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
		ButtonWidget button = new ButtonWidget(x, y, 20, 20, new LiteralText("asdfsd.asdfsd"), this::particleButtonPressed);
		addSelectableChild(button);
		return button;
	}
	
	private void particleButtonPressed(ButtonWidget buttonWidget) {
		int buttonIndex = particleButtons.indexOf(buttonWidget);
		int newIndex = PARTICLES_PER_PAGE * activeParticlePage + buttonIndex;
		
		if (newIndex < AVAILABLE_PARTICLES.size() - 1) {
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
			ClientPlayNetworking.send(SpectrumC2SPacketSender.CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID, packetByteBuf);
		} catch (Exception e) {
			// the text boxes currently are not parseable as-is.
			// wait until the player finished setting everything up
		}
	}
	
	@Contract("_ -> param1")
	private @NotNull PacketByteBuf writeSettings(@NotNull PacketByteBuf packetByteBuf) {
		packetByteBuf.writeString(AVAILABLE_PARTICLES.get(particleSelectionIndex).toString());
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
		return packetByteBuf;
	}
	
}
