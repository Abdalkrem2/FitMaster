export interface DashboardStats {
  activeMembers: number;
  monthlyRevenue: number;
  expiringSoon: number;
}

export interface ActivityItem {
  id: string;
  type: 'joined' | 'payment' | 'renewed';
  description: string;
  time: string;
}

export const dashboardService = {
  getStats: async (): Promise<DashboardStats> => {
    return {
      activeMembers: 0,
      monthlyRevenue: 0,
      expiringSoon: 0
    };
  },

  getRecentActivity: async (): Promise<ActivityItem[]> => {
    return [];
  }
};
