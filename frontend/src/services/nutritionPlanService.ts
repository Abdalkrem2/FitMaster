import { api } from './api';
import type { NutritionPlan } from '../types/nutritionPlan';

export const nutritionPlanService = {
  async getActivePlan(): Promise<NutritionPlan> {
    const { data } = await api.get('/nutrition-plans/active');
    return data;
  },

  async getPlanHistory(): Promise<NutritionPlan[]> {
    const { data } = await api.get('/nutrition-plans/history');
    return data;
  },

  async generateNewPlan(): Promise<NutritionPlan> {
    const { data } = await api.post('/nutrition-plans/generate', {});
    return data;
  },

  async downloadPlanPdf(): Promise<Blob> {
    const response = await api.get('/nutrition-plans/active/pdf', {
      responseType: 'blob',
    });
    return new Blob([response.data], { type: 'application/pdf' });
  },
};
