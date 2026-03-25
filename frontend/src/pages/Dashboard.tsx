import React, { useEffect, useState } from 'react';
import { Users, DollarSign, AlertCircle, TrendingUp } from 'lucide-react';
import { dashboardService, type DashboardStats, type ActivityItem } from '../services/dashboardService';
import { Card, CardHeader, CardTitle } from '../components/ui/Card';

const StatCard: React.FC<{ title: string; value: string | number; icon: React.ReactNode; colorClass: string }> = ({ title, value, icon, colorClass }) => (
  <Card className="flex items-center p-6 space-x-4 hover:-translate-y-1 transition-transform">
    <div className={`p-4 rounded-full ${colorClass}`}>
      {icon}
    </div>
    <div>
      <p className="text-sm font-medium text-gray-500">{title}</p>
      <h4 className="text-2xl font-bold text-gray-900">{value}</h4>
    </div>
  </Card>
);

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [activities, setActivities] = useState<ActivityItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [statsData, activityData] = await Promise.all([
          dashboardService.getStats(),
          dashboardService.getRecentActivity()
        ]);
        setStats(statsData);
        setActivities(activityData);
      } catch (error) {
        console.error("Failed to load dashboard data");
      } finally {
        setLoading(false);
      }
    };
    fetchDashboardData();
  }, []);

  if (loading || !stats) {
    return <div className="flex h-64 items-center justify-center">Loading dashboard...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-end">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Dashboard Overview</h2>
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
          title="Monthly Revenue" 
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
          <Card>
            <CardHeader>
              <CardTitle>Recent Activity</CardTitle>
            </CardHeader>
            <div className="space-y-6">
              {activities.map((activity, index) => (
                <div key={activity.id} className="flex relative">
                  {index !== activities.length - 1 && (
                    <div className="absolute top-8 left-4 bottom-[-24px] w-[2px] bg-gray-100"></div>
                  )}
                  <div className="relative mr-4 mt-1 z-10">
                    <div className="w-8 h-8 rounded-full bg-blue-50 flex items-center justify-center border-2 border-white shadow-sm">
                      <TrendingUp className="w-4 h-4 text-blue-600" />
                    </div>
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-900">{activity.description}</p>
                    <p className="text-xs text-gray-500">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>
        
        <div className="space-y-6">
          <Card className="bg-gradient-to-br from-indigo-500 to-blue-600 text-white border-none">
            <h3 className="text-lg font-semibold mb-2">Upgrade FitMaster</h3>
            <p className="text-sm text-blue-100 mb-4">
              Get more advanced analytics and mult-branch support.
            </p>
            <button className="bg-white text-blue-600 px-4 py-2 rounded-lg text-sm font-medium hover:bg-gray-50 transition-colors shadow-soft">
              Learn More
            </button>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
