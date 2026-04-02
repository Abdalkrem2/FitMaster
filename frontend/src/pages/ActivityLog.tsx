import React, { useEffect, useState } from 'react';
import { Activity as ActivityIcon, UserPlus, DollarSign, RefreshCw, Calendar, Filter, Trash2 } from 'lucide-react';

import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { employeeService } from '../services/employeeService';
import type { ActivityLogItem } from '../types/activityLog';
import type { Employee } from '../types/employee';
import { useGetLogs } from '@/hooks/useGetLogs';

const ActivityLog: React.FC = () => {
  const [activities, setActivities] = useState<ActivityLogItem[]>([]);
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [performedBy, setPerformedBy] = useState<string>("");
  const [entityType, setEntityType] = useState<string>("");
  const [size, setSize] = useState(20);
  const [loading, setLoading] = useState(true);



  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const data = await employeeService.getAllEmployees(0, 1000);
        setEmployees(data.content);
      } catch (err) {
        console.error("Failed to load employees for filter", err);
      }
    };
    fetchEmployees();
  }, []);

  const fetchLogs = async () => {
    setLoading(true);
    const data = await useGetLogs(performedBy, entityType, page, size);
    if (data) {
      setActivities(data.content || []);
      setTotalPages(data.totalPages || 0);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchLogs();
  }, [page, performedBy, entityType]);

  const handleFilter = () => {
    if (page === 0) {
      fetchLogs();
    } else {
      setPage(0);
    }
  };

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

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Activity Log</h2>
          <p className="text-sm text-gray-500 mt-1">Audit trail of all system and user activities.</p>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-soft border border-gray-100 p-6 space-y-4 max-w-4xl">
        <div className="flex flex-col md:flex-row gap-4 items-end">
          <div className="flex-1 w-full">
            <label className="block text-sm font-medium text-gray-700 mb-1">Performed By</label>
            <select
              value={performedBy}
              onChange={(e) => setPerformedBy(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500 sm:text-sm bg-white h-10"
            >
              <option value="">All Employees</option>
              {employees.map((emp) => (
                <option key={emp.id} value={emp.id.toString()}>{emp.fullName}</option>
              ))}
            </select>
          </div>
          <div className="flex-1 w-full">
            <label className="block text-sm font-medium text-gray-700 mb-1">Entity Type</label>
            <select
              value={entityType}
              onChange={(e) => setEntityType(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500 sm:text-sm bg-white h-10"
            >
              <option value="">All Entities</option>
              <option value="MEMBER">MEMBER</option>
              <option value="EMPLOYEE">EMPLOYEE</option>
              <option value="MEMBERSHIP">MEMBERSHIP</option>
            </select>
          </div>
          <Button onClick={handleFilter} className="mb-0 h-10">
            <Filter className="w-4 h-4 mr-2" /> Filter
          </Button>
        </div>
      </div>

      <Card padding="lg" className="max-w-4xl">
        {loading ? (
          <div className="py-12 text-center text-gray-500">Loading activities...</div>
        ) : activities.length === 0 ? (
          <div className="py-12 text-center text-gray-500">No activities found.</div>
        ) : (
          <div className="space-y-8">
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

        {!loading && activities.length > 0 && (
          <div className="flex justify-between items-center pt-8 mt-4 border-t border-gray-100">
            <span className="text-sm text-gray-500">
              Page {page + 1} of {totalPages === 0 ? 1 : totalPages}
            </span>
            <div className="flex gap-2">
              <Button
                variant="outline"
                size="sm"
                disabled={page === 0}
                onClick={() => setPage((p) => p - 1)}
              >
                Previous
              </Button>
              <Button
                variant="outline"
                size="sm"
                disabled={page >= totalPages - 1}
                onClick={() => setPage((p) => p + 1)}
              >
                Next
              </Button>
            </div>
          </div>
        )}
      </Card>
    </div>
  );
};

export default ActivityLog;
