package com.nsquare.restaurant;

public class ProductionFocusEvent {
    private Direction direction;
    private FocusType focusType;
    private Production production;

    public enum FocusType {
        RECEIVING,
        FOCUSED,
        BLURRED
    }

    public ProductionFocusEvent(FocusType focusType, Production production, Direction direction) {
        this.focusType = focusType;
        this.production = production;
        this.direction = direction;
    }

    public FocusType getFocusType() {
        return this.focusType;
    }

    public Production getProduction() {
        return this.production;
    }

    public Direction getDirection() {
        return this.direction;
    }
}
