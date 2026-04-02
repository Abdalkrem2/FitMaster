import React, { useEffect, useState } from "react";
import { Users, DollarSign, AlertCircle, TrendingUp, UserPlus, ActivityIcon, RefreshCw, Trash2, Calendar } from "lucide-react";
import {
  dashboardService,
  type DashboardStats,
} from "../services/dashboardService";
import type{ActivityLogItem} from "../types/activityLog";
import { Card, CardHeader, CardTitle } from "../components/ui/Card";
import { useGetLogs } from "@/hooks/useGetLogs";

const StatCard: React.FC<{
  title: string;
  value: string | number;
  icon: React.ReactNode;
  colorClass: string;
}> = ({ title, value, icon, colorClass }) => (
  <Card className="flex items-center p-6 space-x-4 hover:-translate-y-1 transition-transform">
    <div className={`p-4 rounded-full ${colorClass}`}>{icon}</div>
    <div>
      <p className="text-sm font-medium text-gray-500">{title}</p>
      <h4 className="text-2xl font-bold text-gray-900">{value}</h4>
    </div>
  </Card>
);

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
   const [activities, setActivities] = useState<ActivityLogItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [statsData, activityData] = await Promise.all([
          dashboardService.getStats(),
          useGetLogs(null,null,0,50),
        ]);
        setStats(statsData);
        setActivities(activityData.content);

      } catch (error) {
        console.error("Failed to load dashboard data");
      } finally {
        setLoading(false);
      }
    };
    fetchDashboardData();
  }, []);

const getActivityIcon = (type: string) => {
    switch (type) {
      case 'CREATE': return <UserPlus className="w-5 h-5 text-blue-600" />;
      case 'MEMBERSHIP': return <DollarSign className="w-5 h-5 text-emerald-600" />;
      case 'UPDATE': return <RefreshCw className="w-5 h-5 text-indigo-600" />;
      case 'DELETE': return <Trash2 className="w-5 h-5 text-red-600" />;
      default: return <ActivityIcon className="w-5 h-5 text-gray-600" />;
    }
  };

  const getActivityColor = (type: string) => {
    switch (type) {
      case 'CREATE': return 'bg-blue-100 border-blue-200';
      case 'MEMBERSHIP': return 'bg-emerald-100 border-emerald-200';
      case 'UPDATE': return 'bg-indigo-100 border-indigo-200';
      case 'DELETE': return 'bg-red-100 border-red-200';
      default: return 'bg-gray-100 border-gray-200';
    }
  };

  if (loading || !stats) {
    return (
      <div className="flex h-64 items-center justify-center">
        Loading dashboard...
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">
            Dashboard Overview
          </h2>
          <p className="text-sm text-gray-500 mt-1">Welcome back, Admin</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <StatCard
          title="Active Members"
          value={stats.activeMembers}
          icon={<Users className="w-6 h-6 text-blue-600" />}
          colorClass="bg-blue-100"
        />
        <StatCard
          title="Monthly Revenue (This Month)"
          value={`$${stats.monthlyRevenue.toLocaleString()}`}
          icon={<DollarSign className="w-6 h-6 text-emerald-600" />}
          colorClass="bg-emerald-100"
        />
        <StatCard
          title="Expiring Soon"
          value={stats.expiringSoon}
          icon={<AlertCircle className="w-6 h-6 text-amber-600" />}
          colorClass="bg-amber-100"
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-8">
        <div className="lg:col-span-2 space-y-6">
      
      <Card padding="lg" className="max-w-4xl">
        {loading ? (
          <div className="py-12 text-center text-gray-500">Loading activities...</div>
        ) : activities.length === 0 ? (
          <div className="py-12 text-center text-gray-500">No activities found.</div>
        ) : (
          <div className="space-y-8 max-h-[500px] overflow-y-auto pr-4">
            {activities.map((activity, index) => (
              <div key={activity.id} className="flex relative">
                {index !== activities.length - 1 && (
                  <div className="absolute top-10 left-6 bottom-[-32px] w-[2px] bg-gray-100"></div>
                )}
                


                <div className="relative mr-6 mt-1 z-10">
                  <div className={`w-12 h-12 rounded-full flex items-center justify-center border-2 shadow-sm ${getActivityColor(activity.actionType.toString())}`}>
                    {getActivityIcon(activity.actionType.toString())}
                  </div>
                </div>
                
                <div className="flex-1 pb-2">
                  <div className="bg-gray-50 rounded-xl p-4 border border-gray-100">
                    <p className="text-md font-medium text-gray-900"> {activity.performedByName} {activity.details}</p>
                    <div className="flex items-center mt-2 text-xs text-gray-500 font-medium tracking-wide">
                      <Calendar className="w-3.5 h-3.5 mr-1" />
                      {activity.createdAt}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

     
        
      </Card>
        </div>

      </div>
    </div>
  );
};

export default Dashboard;
