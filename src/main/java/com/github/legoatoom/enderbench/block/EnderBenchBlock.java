package com.github.legoatoom.enderbench.block;

import com.github.legoatoom.enderbench.block.entity.EnderBenchEntity;
import com.github.legoatoom.enderbench.sound.ModSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * NOTE, DO THE THING ABOUT THE ENDER PALET die can switchen tussen blocken.
 * En een tool beld dat 4 tools kan vast houiden, die je met shift scroll kan switchen.
 */
public class EnderBenchBlock extends BlockWithEntity {

    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    public static final IntProperty DISTANCE = IntProperty.of("distance", 0, 2);
    public static final BooleanProperty LOCKED = BooleanProperty.of("locked");

    public PlayerEntity owner;

    protected EnderBenchBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(ACTIVE,false).with(DISTANCE, 0).with(LOCKED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(DISTANCE).add(ACTIVE).add(LOCKED);
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new EnderBenchEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
            EnderBenchBlock.playSound(world, ModSoundEvents.ENDER_BENCH_OPEN, pos);

        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EnderBenchEntity) {
                ItemScatterer.spawn(world, pos, (EnderBenchEntity)blockEntity);
                // update comparators
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof PlayerEntity){
            this.owner = (PlayerEntity) placer;
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ACTIVE)) {
            double d = (double)pos.getX() + 0.5D;
            double e = (double)pos.getY() - 0.5D;
            double f = (double)pos.getZ() + 0.5D;

            for (Direction direction : Direction.values()){
                if (direction == Direction.DOWN || direction == Direction.UP) continue;
                Direction.Axis axis = direction.getAxis();
                double h = random.nextDouble() * 0.6D - 0.3D;
                double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52D : h;
                double j = random.nextDouble() * 6.0D / 16.0D;
                double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52D : h;
                world.addParticle(ParticleTypes.PORTAL, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
            }

        }
        if (state.get(LOCKED)) {
            double d = (double)pos.getX() + 0.5D;
            double e = (double)pos.getY();
            double f = (double)pos.getZ() + 0.5D;

            for (Direction direction : Direction.values()){
                if (direction == Direction.DOWN || direction == Direction.UP) continue;
                Direction.Axis axis = direction.getAxis();
                double h = random.nextDouble() * 0.6D - 0.3D;
                double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52D : h;
                double j = random.nextDouble() * 6.0D / 16.0D;
                double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52D : h;
                world.addParticle(DustParticleEffect.RED, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
            }

        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            boolean bl = state.get(LOCKED);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    world.getBlockTickScheduler().schedule(pos, this, 4);
                } else {
                    world.setBlockState(pos, (BlockState)state.cycle(LOCKED), 2);
                }
            }

        }
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(LOCKED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LOCKED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, (BlockState)state.cycle(LOCKED));
        }

    }

    public static void playSound(World world, SoundEvent soundEvent, BlockPos pos){
        if(!world.isClient()){
            world.playSound(
                    null,
                    pos,
                    soundEvent,
                    SoundCategory.BLOCKS,
                    .5f,
                    1f
            );
        }
    }
}
