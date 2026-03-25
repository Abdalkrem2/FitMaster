import React, { useEffect, useState } from 'react';
import { Filter, DollarSign, TrendingUp, CreditCard, Activity } from 'lucide-react';
import { revenueService, type RevenueStats } from '../services/revenueService';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';

const RevenueCard: React.FC<{ title: string; amount: number; icon: React.ReactNode; color: string }> = ({ title, amount, icon, color }) => (
  <Card className="flex flex-col relative overflow-hidden">
    <div className={`absolute top-0 right-0 p-4 opacity-10 ${color}`}>
      {icon}
    </div>
    <div className="flex-1">
      <h3 className="text-sm font-medium text-gray-500 mb-1">{title}</h3>
      <div className="flex items-center text-gray-900">
        <span className="text-3xl font-bold">${amount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</span>
      </div>
    </div>
  </Card>
);

const Revenue: React.FC = () => {
  const [stats, setStats] = useState<RevenueStats | null>(null);
  const [loading, setLoading] = useState(true);
  
  // Filters
  const [dateRange, setDateRange] = useState({ start: '', end: '' });
  const [gender, setGender] = useState('All');

  const fetchStats = async () => {
    setLoading(true);
    try {
      const data = await revenueService.getStats({
        dateRange: dateRange.start && dateRange.end ? dateRange : undefined,
        gender: gender !== 'All' ? gender : undefined
      });
      setStats(data);
    } catch (err) {
      console.error("Failed to load revenue");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleApplyFilters = () => {
    fetchStats();
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Revenue Analytics</h2>
          <p className="text-sm text-gray-500 mt-1">Track financial performance and debts.</p>
        </div>
      </div>

      <Card className="bg-white p-4">
        <div className="flex flex-col md:flex-row gap-4 items-end">
          <div className="flex-1 grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-xs font-medium text-gray-700 mb-1">Start Date</label>
              <input 
                type="date" 
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 text-sm"
                value={dateRange.start}
                onChange={e => setDateRange({ ...dateRange, start: e.target.value })}
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700 mb-1">End Date</label>
              <input 
                type="date" 
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 text-sm"
                value={dateRange.end}
                onChange={e => setDateRange({ ...dateRange, end: e.target.value })}
              />
            </div>
            <div>
              <label className="block text-xs font-medium text-gray-700 mb-1">Gender</label>
              <select 
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-blue-500 text-sm"
                value={gender}
                onChange={e => setGender(e.target.value)}
              >
                <option value="All">All Genders</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
              </select>
            </div>
          </div>
          <Button onClick={handleApplyFilters} className="whitespace-nowrap">
            <Filter className="w-4 h-4 mr-2" /> Apply Filters
          </Button>
        </div>
      </Card>

      {loading || !stats ? (
        <div className="py-12 text-center text-gray-500">Loading analytics...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mt-6">
          <RevenueCard 
            title="Today's Revenue" 
            amount={stats.today} 
            icon={<DollarSign className="w-16 h-16" />} 
            color="text-emerald-500"
          />
          <RevenueCard 
            title="This Month" 
            amount={stats.thisMonth} 
            icon={<TrendingUp className="w-16 h-16" />} 
            color="text-blue-500"
          />
          <RevenueCard 
            title="This Year" 
            amount={stats.thisYear} 
            icon={<Activity className="w-16 h-16" />} 
            color="text-indigo-500"
          />
          <RevenueCard 
            title="Total Debt" 
            amount={stats.totalDebt} 
            icon={<CreditCard className="w-16 h-16" />} 
            color="text-red-500"
          />
        </div>
      )}
    </div>
  );
};

export default Revenue;
