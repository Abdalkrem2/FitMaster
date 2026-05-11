package com.web.fitmaster.workout;

import java.util.List;

public record DaySplit (
        String label,
        List<MuscleTarget> muscles
){}
