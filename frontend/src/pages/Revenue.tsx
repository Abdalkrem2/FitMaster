import React, { useEffect, useState } from 'react';
import { revenueService } from '../services/revenueService';
import type { MonthlyRevenue, RevenueByPeriod, RevenueStats ,RevenueRow} from '../types/revenue';
import { Table, type Column } from '@/components/ui/Table';
import { Card } from '@/components/ui/Card';
import { Input } from '@/components/ui/Input';
import { Button } from '@/components/ui/Button';
import { TrendingUp, AlertCircle, Calendar, BarChart3, Filter } from 'lucide-react';

const months = [
  '1-JAN', '2-Feb', '3-Mar', '4-Apr', '5-May', '6-Jun',
  '7-Jul', '8-Aug', '9-Sep', '10-Oct', '11-Nov', '12-Dec'
];

const Revenue: React.FC = () => {
  const [stats, setStats] = useState<RevenueStats>({
    today: 0,
    thisMonth: 0,
    thisYear: 0,
    debt: 0
  });
  
  // Filters
  const [selectedYear, setSelectedYear] = useState('2026');
  const [monthlyRevenue, setMonthlyRevenue] = useState<MonthlyRevenue|null>(null);
  const [revenueByPeriod, setRevenueByPeriod] = useState<RevenueByPeriod|null>(null);
  const [error,setError] = useState<Error|null>(null);
  
  const [dateRange, setDateRange] = useState({ start: '', end: '' });
  const [gender, setGender] = useState('All');
  
  const fetchStats = async () => {
    try {
      const data = await revenueService.getStats();
      setStats({
        today: data.today,
        thisMonth: data.thisMonth,
        thisYear: data.thisYear,
        debt: data.debt 
      });
     
    } catch (err) {
      setError(err as Error);
    }
  };

  const fetchMonthlyRevenue = async () => {
    try {
      const data = await revenueService.monthlyRevenue(Number(selectedYear));
      setMonthlyRevenue(data);
    } catch (err) {
      setError(err as Error);
    }
  };

  const fetchRevenueByPeriod = async () => {
    try {
      const data = await revenueService.revenueByPeriod(dateRange.start,dateRange.end,gender);
      setRevenueByPeriod(data);
    } catch (err) {
      setError(err as Error);
    }
  };

  useEffect(() => {
    fetchMonthlyRevenue();
  }, [selectedYear]);

  useEffect(() => {
    fetchStats();
  }, []);

  const columns: Column<RevenueRow>[] = [
    {key:'id',header: 'ID'},
    {
      key:'memberName',
      header: 'Member Name',
      render: (row) => <span className="font-semibold text-slate-800">{row.memberName}</span> 
    },
    { 
      key: 'amount', 
      header: 'Amount',
      render: (row) => <span className="text-emerald-600 font-bold">${row.amount}</span>
    },
    {
      key:'debt',
      header: 'Debt',
      render: (row) => {
        if(row.debt > 0)
          return (
            <span className="inline-flex px-2 py-0.5 rounded text-[11px] font-semibold bg-rose-50 text-rose-600 border border-rose-200/50">
              ${row.debt}
            </span>
          );
        return <span className="text-slate-400 font-medium">-</span>;
      }
    },
    {
      key:'pkg',
      header: 'Package',
      render: (row) => <span className="text-slate-600 text-sm font-medium">{row.pkg}</span>
    },
    {key:'addedByName',header: 'Added By'},
    {key:'createdAt',header: 'Date'},
    {key:'description',header: 'Description'},
  ];

  return (
    <div className='max-w-7xl mx-auto space-y-6'>
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold text-slate-900 tracking-tight">Revenue Analytics</h2>
          <p className="text-sm text-slate-500 mt-1">Track payments, financial growth, and open debts.</p>
        </div>
      </div>

      {error && (
        <div className="bg-rose-50 border border-rose-200 text-rose-600 p-3 rounded-xl text-sm font-medium flex items-center gap-2">
          <AlertCircle className="w-5 h-5 text-rose-500" />
          Something went wrong. Please try again.
        </div>
      )}

      {/* Top Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card variant="gradient" padding="md">
          <h3 className="text-white/80 font-medium text-sm">Today</h3>
          <p className="text-white text-3xl font-bold mt-1 tracking-tight">${stats.today}</p>
        </Card>
        <Card padding="md">
          <h3 className="text-slate-500 font-medium text-sm">This Month</h3>
          <p className="text-slate-900 text-3xl font-bold mt-1 tracking-tight">${stats.thisMonth}</p>
        </Card>
        <Card padding="md">
          <h3 className="text-slate-500 font-medium text-sm">This Year</h3>
          <p className="text-slate-900 text-3xl font-bold mt-1 tracking-tight">${stats.thisYear}</p>
        </Card>
        <Card padding="md" className="border-t-4 border-t-rose-400">
          <h3 className="text-slate-500 font-medium text-sm flex items-center gap-2">
             Outstanding Debt
             <AlertCircle className="w-4 h-4 text-rose-400" />
          </h3>
          <p className="text-rose-600 text-3xl font-bold mt-1 tracking-tight">${stats.debt}</p>
        </Card>
      </div>

      {/* Monthly Breakdown */}
      <Card padding="lg" className="space-y-6">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 border-b border-slate-100 pb-4">
          <div className="flex items-center gap-3">
             <div className="p-2 bg-indigo-50 text-indigo-600 rounded-lg">
                <BarChart3 className="w-5 h-5" />
             </div>
             <div>
               <h3 className="text-lg font-bold text-slate-900">Monthly Breakdown</h3>
               <p className="text-sm text-slate-500 flex items-center gap-2 mt-0.5">
                  <span className="font-semibold text-emerald-600">Total: ${monthlyRevenue?.yearTotal || 0}</span> in {selectedYear}
               </p>
             </div>
          </div>
          <div className="flex items-center gap-3">
             <span className="text-sm font-medium text-slate-500">Year:</span>
             <select 
              className="px-3 py-1.5 border border-slate-200 rounded-lg text-sm bg-slate-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500/20 transition-all font-medium"
              value={selectedYear}
              onChange={e => setSelectedYear(e.target.value)}
            >
              <option value="2026">2026</option>
              <option value="2025">2025</option>
            </select>
          </div>
        </div>

        <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-3">
          {months.map(m => (
            <div key={m} className="p-4 border border-slate-100 rounded-xl bg-slate-50 hover:bg-white hover:shadow-sm hover:border-indigo-100 transition-all text-center group">
              <span className="block text-[11px] font-bold text-slate-400 uppercase tracking-wider mb-2 group-hover:text-indigo-500">{m.split('-')[1]}</span>
              <span className="text-lg font-bold text-slate-800">${monthlyRevenue?.months[Number(m.split('-')[0])] || 0}</span>
            </div>
          ))}
        </div>
      </Card>

      {/* Period Analysis */}
      <Card padding="lg" className="space-y-6">
        <div className="flex items-center gap-3 border-b border-slate-100 pb-4">
           <div className="p-2 bg-emerald-50 text-emerald-600 rounded-lg">
              <Calendar className="w-5 h-5" />
           </div>
           <div>
             <h3 className="text-lg font-bold text-slate-900">Custom Period Analysis</h3>
             <p className="text-sm text-slate-500 mt-0.5">Filter revenue by specific dates and demographics</p>
           </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
          <Input 
             type="date"
             label="Start Date"
             value={dateRange.start}
             onChange={e => setDateRange({ ...dateRange, start: e.target.value })}
             className="mb-0"
          />
          <Input 
             type="date"
             label="End Date"
             value={dateRange.end}
             onChange={e => setDateRange({ ...dateRange, end: e.target.value })}
             className="mb-0"
          />
          <div className="w-full">
            <label className="block text-sm font-medium text-slate-700 mb-1">Gender</label>
            <select 
              className="w-full px-3 py-2 border border-slate-200 rounded-lg shadow-sm text-sm bg-slate-50 focus:bg-white focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-400 transition-all h-9"
              value={gender}
              onChange={e => setGender(e.target.value)}
            >
              <option value="All">All</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
            </select>
          </div>
          <Button 
             onClick={fetchRevenueByPeriod}
             disabled={dateRange.start === "" || dateRange.end === ""}
             className="w-full h-9 bg-gradient-to-r from-indigo-500 to-violet-600 text-white shadow-sm hover:shadow-md"
          >
            <Filter className="w-4 h-4 mr-2" />
            Calculate
          </Button>
        </div>
        
        <div className="pt-2">
          {revenueByPeriod === null ? (
            <div className="py-12 border-2 border-dashed border-slate-200 rounded-xl flex flex-col items-center justify-center text-slate-400">
               <TrendingUp className="w-8 h-8 mb-2 text-slate-300" />
               <p className="text-sm font-medium">Select a period to generate report</p>
            </div>
          ) : (
            <div className="space-y-4 animate-fade-in-up">
              <div className="flex gap-4 p-4 bg-slate-50 rounded-xl border border-slate-100">
                 <div className="flex flex-col">
                   <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Total Revenue</span>
                   <span className="text-xl font-bold text-emerald-600">${revenueByPeriod?.periodTotal || 0}</span>
                 </div>
                 <div className="w-px h-10 bg-slate-200 my-auto hidden sm:block"></div>
                 <div className="flex flex-col">
                   <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">Period Debt</span>
                   <span className="text-xl font-bold text-rose-600">${revenueByPeriod?.periodDebt || 0}</span>
                 </div>
              </div>
              <Table data={revenueByPeriod?.revenues || []} columns={columns} keyExtractor={(row) => row.id} />
            </div>
          )}
        </div>
      </Card>
    </div>
  );
};

export default Revenue;
