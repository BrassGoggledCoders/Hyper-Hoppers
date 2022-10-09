package xyz.brassgoggledcoders.hyperhoppers.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class HyperHoppersBlockStateProperties {
    public static final BooleanProperty JAMMED = BooleanProperty.create("jammed");
    public static final Property<Direction> SPOUT = EnumProperty.create("spout", Direction.class);
}
