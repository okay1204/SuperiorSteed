package me.screescree.SuperiorSteed.superiorhorse.horseeditor.components.amountpicker;

public class AmountPickerSettings {
    private double min;
    private double max;
    private double smallStep;
    private double largeStep;

    public AmountPickerSettings(double min, double max, double smallStep, double largeStep) {
        this.min = min;
        this.max = max;
        this.smallStep = smallStep;
        this.largeStep = largeStep;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getSmallStep() {
        return smallStep;
    }

    public double getLargeStep() {
        return largeStep;
    }
}
