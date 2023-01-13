package agh.oop.backend.utils;

import agh.oop.backend.model.MiniatureSize;

public class LabelMapper {
    public static int smallMiniatureWidth = 160;
    public static int smallMiniatureHeight = 100;
    public static int mediumMiniatureWidth = 200;
    public static int mediumMiniatureHeight = 120;
    public static int bigMiniatureWidth = 360;
    public static int bigMiniatureHeight = 240;
    public static MiniatureSize getLabel(int width, int height) {
        if(width == smallMiniatureWidth && height == smallMiniatureHeight) {
            return MiniatureSize.SMALL;
        }
        if(width == mediumMiniatureWidth && height == mediumMiniatureHeight) {
            return MiniatureSize.MEDIUM;
        }
        if(width == bigMiniatureWidth && height == bigMiniatureHeight) {
            return MiniatureSize.BIG;
        }
        throw new RuntimeException("Unknown width and height");
    }

    public static int getWidth(MiniatureSize size) {
        return switch(size) {
            case SMALL -> smallMiniatureWidth;
            case MEDIUM -> mediumMiniatureWidth;
            case BIG -> bigMiniatureWidth;
        };
    }

    public static int getHeight(MiniatureSize size) {
        return switch(size) {
            case SMALL -> smallMiniatureHeight;
            case MEDIUM -> mediumMiniatureHeight;
            case BIG -> bigMiniatureHeight;
        };
    }
}
