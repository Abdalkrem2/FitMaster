package com.web.fitmaster.workout;

import java.util.List;

public record TrainingConfig(
        int sets,
        int repsMin,
        int repsMax,
        List<DaySplit> daySplits  // توزيع الأيام
) {}