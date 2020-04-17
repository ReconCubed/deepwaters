package bernie.software.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.common.ToolType;

public class DeepWatersLamp extends Block {
    public DeepWatersLamp(Properties properties) {
        super(properties);
    }

    public DeepWatersLamp() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(1.8F, 1.6F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .lightValue(9)
                .harvestTool(ToolType.PICKAXE)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return getShape(state,worldIn,pos,context);
    }

    public static final EnumProperty ROTATION = BlockStateProperties.HORIZONTAL_FACING;

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer.equals(BlockRenderLayer.TRANSLUCENT)||layer.equals(BlockRenderLayer.CUTOUT);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(ROTATION, context.getPlacementHorizontalFacing().getOpposite());
    }
}