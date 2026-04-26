import { api } from './api';
import type { WorkoutPlan, WorkoutProgress } from '../types/workoutPlan';

export const workoutPlanService = {
  async getActivePlan(): Promise<WorkoutPlan> {
    const { data } = await api.get('/workout-plans/active');
    return data;
  },

  async getPlanHistory(): Promise<WorkoutPlan[]> {
    const { data } = await api.get('/workout-plans/history');
    return data;
  },

  async generateNewPlan(): Promise<WorkoutPlan> {
    const { data } = await api.post('/workout-plans/generate', {});
    return data;
  },

  async downloadPlanPdf(): Promise<Blob> {
    const response = await api.get('/workout-plans/active/pdf', {
      responseType: 'blob',
    });
    return new Blob([response.data], { type: 'application/pdf' });
  },

  saveProgress(progress: WorkoutProgress): void {
    localStorage.setItem(`workout_progress_${progress.planId}`, JSON.stringify(progress));
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