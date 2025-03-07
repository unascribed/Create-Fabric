package com.simibubi.create.content.logistics.block.display.source;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.logistics.block.display.DisplayBehaviour;
import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.target.DisplayBoardTarget;
import com.simibubi.create.content.logistics.block.display.target.DisplayTarget;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import com.simibubi.create.content.logistics.trains.management.display.FlapDisplayLayout;
import com.simibubi.create.content.logistics.trains.management.display.FlapDisplayTileEntity;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class DisplaySource extends DisplayBehaviour {

	public static final List<MutableComponent> EMPTY = ImmutableList.of(new TextComponent(""));
	public static final MutableComponent EMPTY_LINE = new TextComponent("");
	public static final MutableComponent WHITESPACE = new TextComponent(" ");

	public abstract List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats);

	public void transferData(DisplayLinkContext context, DisplayTarget activeTarget, int line) {
		DisplayTargetStats stats = activeTarget.provideStats(context);

		if (activeTarget instanceof DisplayBoardTarget fddt) {
			List<List<MutableComponent>> flapDisplayText = provideFlapDisplayText(context, stats);
			fddt.acceptFlapText(line, flapDisplayText, context);
		}

		List<MutableComponent> text = provideText(context, stats);
		if (text.isEmpty())
			text = EMPTY;
		activeTarget.acceptText(line, text, context);
	}

	public void onSignalReset(DisplayLinkContext context) {};

	public void populateData(DisplayLinkContext context) {};

	public int getPassiveRefreshTicks() {
		return 100;
	};

	protected String getTranslationKey() {
		return id.getPath();
	}

	public Component getName() {
		return new TranslatableComponent(id.getNamespace() + ".display_source." + getTranslationKey());
	}

	public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayTileEntity flapDisplay, FlapDisplayLayout layout, int lineIndex) {
		loadFlapDisplayLayout(context, flapDisplay, layout);
	}

	public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayTileEntity flapDisplay,
		FlapDisplayLayout layout) {
		if (!layout.isLayout("Default"))
			layout.loadDefault(flapDisplay.getMaxCharCount());
	}

	public List<List<MutableComponent>> provideFlapDisplayText(DisplayLinkContext context, DisplayTargetStats stats) {
		return provideText(context, stats).stream()
			.map(Arrays::asList)
			.toList();
	}

	@Environment(EnvType.CLIENT)
	public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder,
		boolean isFirstLine) {}

}
