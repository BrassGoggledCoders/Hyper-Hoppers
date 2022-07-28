package xyz.brassgoggledcoders.hyperhoppers.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PassingShape implements StringRepresentable {
    STRAIGHT("straight", 0),
    ZERO("zero", 0),
    NINETY("ninety", 90),
    ONE_EIGHTY("one_eighty", 180),
    TWO_SEVENTY("two_seventy", 270);

    private final String name;
    private final int rotation;

    PassingShape(String name, int rotation) {
        this.name = name;
        this.rotation = rotation;
    }

    public int getRotation() {
        return this.rotation;
    }

    @Override
    @NotNull
    public String getSerializedName() {
        return this.name;
    }
}
