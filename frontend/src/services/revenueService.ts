export interface RevenueStats {
  today: number;
  thisMonth: number;
  thisYear: number;
  totalDebt: number;
}

export const revenueService = {
  getStats: async (filters?: { dateRange?: { start: string, end: string }, gender?: string }): Promise<RevenueStats> => {
    return {
      today: 0,
      thisMonth: 0,
      thisYear: 0,
      totalDebt: 0
    };
  }
};
