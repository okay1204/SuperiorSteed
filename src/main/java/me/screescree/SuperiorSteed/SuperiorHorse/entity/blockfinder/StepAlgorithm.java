package me.screescree.SuperiorSteed.superiorhorse.entity.blockfinder;

import java.util.Optional;

public interface StepAlgorithm<T> {
    StepResult step();
    Optional<T> stepAll();
    T getFound();
}
