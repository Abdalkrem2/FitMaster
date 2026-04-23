import { api } from "./api";
import type {
  WorkoutPlan,
  WorkoutProgress,
  RenewalChoice,
} from "../types/workoutPlan";

export const workoutPlanService = {
  async getActivePlan(): Promise<WorkoutPlan> {
    const { data } = await api.get("/workout-plans/active");
    return data;
  },

  async generateNewPlan(): Promise<WorkoutPlan> {
    const { data } = await api.post("/workout-plans/generate", {});
    return data;
  },

  async renewPlan(
    planId: number,
    choice: RenewalChoice,
    newSplit?: string,
  ): Promise<WorkoutPlan> {
    const { data } = await api.post(`/workout-plans/${planId}/renew`, {
      choice,
      newSplit,
    });
    return data;
  },

  async markDayCompleted(planId: number, dayNumber: number): Promise<void> {
    await api.post(`/workout-plans/${planId}/days/${dayNumber}/complete`, {});
  },

  async downloadPlanPdf(): Promise<Blob> {
    const response = await api.get("/workout-plans/active/pdf", {
      responseType: "blob",
    });
    return new Blob([response.data], { type: "application/pdf" });
  },

  saveProgress(progress: WorkoutProgress): void {
    localStorage.setItem(
      `workout_progress_${progress.planId}`,
      JSON.stringify(progress),
    );
  },

  loadProgress(planId: number): WorkoutProgress | null {
    try {
      const raw = localStorage.getItem(`workout_progress_${planId}`);
      return raw ? (JSON.parse(raw) as WorkoutProgress) : null;
    } catch {
      return null;
    }
  },

  clearProgress(planId: number): void {
    localStorage.removeItem(`workout_progress_${planId}`);
  },

  getProgressPercent(planId: number, totalExercises: number): number {
    if (totalExercises === 0) return 0;
    const progress = workoutPlanService.loadProgress(planId);
    if (!progress) return 0;
    const done = progress.completedExercises.filter((e) => e.completed).length;
    return Math.round((done / totalExercises) * 100);
  },
};
