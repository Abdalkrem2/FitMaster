import { api } from "./api";

export interface DashboardStats {
  activeMembers: number;
  monthlyRevenue: number;
  expiringSoon: number;
}

export interface ActivityItem {
actionType:String;
entityType:String;
entityId:number;
performedByName:String;
createdAt:String;
details:String;
}

export const dashboardService = {
  getStats: async (): Promise<DashboardStats> => {
    const res = await api.get("/members/stats");
    const revenueResponse = await api.get("/revenue/stats");
    return {
      activeMembers: res.data.activeMembers ?? 0,
      monthlyRevenue: revenueResponse.data.thisMonth,
      expiringSoon: res.data.expiringSoon ?? 0,
    };
  },

  getRecentActivity: async (): Promise<ActivityItem[]> => {
    return [];
  },
};
