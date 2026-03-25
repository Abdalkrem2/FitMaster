import React, { useEffect, useState } from 'react';
import { Activity as ActivityIcon, UserPlus, DollarSign, RefreshCw, Calendar } from 'lucide-react';
import { dashboardService, type ActivityItem } from '../services/dashboardService';
import { Card } from '../components/ui/Card';

const ActivityLog: React.FC = () => {
  const [activities, setActivities] = useState<ActivityItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchActivities = async () => {
      try {
        // Fetching more activities to simulate a full log page
        const data = await dashboardService.getRecentActivity();
        const expandedData = [
          ...data,
          { id: '5', type: 'payment', description: 'Payment of $100 received from John Doe', time: '2 days ago' } as ActivityItem,
          { id: '6', type: 'joined', description: 'David Smith joined the gym', time: '2 days ago' } as ActivityItem,
          { id: '7', type: 'renewed', description: 'Alice Walker renewed 6 Months plan', time: '3 days ago' } as ActivityItem,
        ];
        setActivities(expandedData);
      } catch (err) {
        console.error("Failed to load activity log");
      } finally {
        setLoading(false);
      }
    };
    fetchActivities();
  }, []);

  const getActivityIcon = (type: string) => {
    switch (type) {
      case 'joined': return <UserPlus className="w-5 h-5 text-blue-600" />;
      case 'payment': return <DollarSign className="w-5 h-5 text-emerald-600" />;
      case 'renewed': return <RefreshCw className="w-5 h-5 text-indigo-600" />;
      default: return <ActivityIcon className="w-5 h-5 text-gray-600" />;
    }
  };

  const getActivityColor = (type: string) => {
    switch (type) {
      case 'joined': return 'bg-blue-100 border-blue-200';
      case 'payment': return 'bg-emerald-100 border-emerald-200';
      case 'renewed': return 'bg-indigo-100 border-indigo-200';
      default: return 'bg-gray-100 border-gray-200';
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Activity Log</h2>
          <p className="text-sm text-gray-500 mt-1">Audit trail of all system and user activities.</p>
        </div>
      </div>

      <Card padding="lg" className="max-w-4xl">
        {loading ? (
          <div className="py-12 text-center text-gray-500">Loading activities...</div>
        ) : (
          <div className="space-y-8">
            {activities.map((activity, index) => (
              <div key={activity.id} className="flex relative">
                {index !== activities.length - 1 && (
                  <div className="absolute top-10 left-6 bottom-[-32px] w-[2px] bg-gray-100"></div>
                )}
                
                <div className="relative mr-6 mt-1 z-10">
                  <div className={`w-12 h-12 rounded-full flex items-center justify-center border-2 shadow-sm ${getActivityColor(activity.type)}`}>
                    {getActivityIcon(activity.type)}
                  </div>
                </div>
                
                <div className="flex-1 pb-2">
                  <div className="bg-gray-50 rounded-xl p-4 border border-gray-100">
                    <p className="text-md font-medium text-gray-900">{activity.description}</p>
                    <div className="flex items-center mt-2 text-xs text-gray-500 font-medium tracking-wide">
                      <Calendar className="w-3.5 h-3.5 mr-1" />
                      {activity.time}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
};

export default ActivityLog;
