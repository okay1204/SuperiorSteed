package me.screescree.SuperiorSteed.superiorhorse.horseeditor;

import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorseInfo;

public class HorseEditorInfo extends SuperiorHorseInfo {
    private Color color;
    private Style style;

    HorseEditorInfo(
        Color color,
        Style style,
        double hunger,
        double hydration,
        double trust,
        double friendliness,
        double comfortability,
        double waterBravery,
        boolean isMale
    ) {
        super(hunger, hydration, trust, friendliness, comfortability, waterBravery, isMale);

        this.color = color;
        this.style = style;
    }

    public static HorseEditorInfo startingTemplate() {
        return new HorseEditorInfo(Color.WHITE, Style.NONE, 1.0, 1.0, 0.5, 0.3, 0.2, 0.1, true);
    }

    public String toString() {
        return "HorseEditorInfo{" +
            "color=" + color +
            ", style=" + style +
            ", hunger=" + getHunger() +
            ", hydration=" + getHydration() +
            ", trust=" + getTrust() +
            ", friendliness=" + getFriendliness() +
            ", comfortability=" + getComfortability() +
            ", waterBravery=" + getWaterBravery() +
            ", isMale=" + isMale() +
            '}';
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
