package me.screescree.SuperiorSteed.superiorhorse.entity.blockfinder;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.block.Block;

import me.screescree.SuperiorSteed.superiorhorse.SuperiorHorse;
import net.minecraft.world.level.pathfinder.Path;

public class BlockFinder implements StepAlgorithm<Block> {

	private static final int MAX_DISTANCE = 20;
	private static final int MAX_STEPS = (int) Math.pow(MAX_DISTANCE + 1, 3);

	private SuperiorHorse superiorHorse;
	private final Location origin;
	private final Predicate<Block> desiredBlock;
	private Block found;
	private int currentStep = 1;
	
	public BlockFinder(SuperiorHorse superiorHorse, Predicate<Block> desiredBlock) {
		this.superiorHorse = superiorHorse;
		this.origin = superiorHorse.getBukkitEntity().getLocation();
		this.desiredBlock = desiredBlock;
	}

	@Override
	public StepResult step() {
		int sideLength = 1;
		while (currentStep > Math.pow(sideLength, 3)) {
			sideLength += 2;
		}
		// int layer = (sideLength / 2) + 1;

		int position = currentStep > 1 ? currentStep - (int) Math.pow(sideLength - 2, 3) - 1 : 0;

		int x = 0, y = 0, z = 0;
		for (int i = 0; i < position; i++) {
			z++;
			if (z == sideLength) {
				z = 0;
				y++;
				if (y == sideLength) {
					y = 0;
					x++;
				}
			}

			// cause the loop to repeat one more time if all 3 values are not min or max
			if (x != 0 && x != sideLength - 1 && y != 0 && y != sideLength - 1 && z != 0 && z != sideLength - 1) {
				i--;
			}
		}
		x -= (sideLength / 2);
		y -= (sideLength / 2);
		z -= (sideLength / 2);

		
		Block offsetBlock = origin.clone().add(x, y, z).getBlock();
		
		if (desiredBlock.test(offsetBlock)) {
			Path path = superiorHorse.getNMSEntity().getNavigation().createPath(offsetBlock.getLocation().getX(), offsetBlock.getLocation().getY(), offsetBlock.getLocation().getZ(), 0);
			if (path == null || path.canReach()) {
				found = offsetBlock;
				return StepResult.SUCCESS;
			}
		}
		
		if (currentStep >= MAX_STEPS) {
			return StepResult.FAILURE;
		}

		currentStep++;
		return StepResult.PROGRESS;
	}

	@Override
	public Optional<Block> stepAll() {
		StepResult lastResult = StepResult.PROGRESS;
		while (lastResult == StepResult.PROGRESS) {
			lastResult = step();
		}
		return Optional.of(found);
	}

	@Nullable
	public Block getFound() {
		return found;
	}
}