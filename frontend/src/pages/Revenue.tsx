import React, { useEffect, useState } from 'react';
import { revenueService } from '../services/revenueService';
import type { MonthlyRevenue, RevenueByPeriod, RevenueStats ,RevenueRow} from '../types/revenue';
import { Table, type Column } from '@/components/ui/Table';

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
  {key:'id',header: 'id'},
  {key:'memberName',header: 'Member Name'},
  { key: 'amount', header: 'Amount' },
  {key:'debt',header: 'Debt'},
  {key:'pkg',header: 'Package'},
  {key:'addedByName',header: 'Added By'},
  {key:'createdAt',header: 'Created At'},
  {key:'description',header: 'Description'},

];



  return (
    <div className='w-full max-w-6xl mx-auto p-2'>
 {error && (
      <div className="bg-red-50 text-red-600 p-3 rounded mb-4 text-sm">
        Something went wrong. Please try again.
      </div>
    )}

  


    <div className="w-full max-w-6xl mx-auto p-2 text-gray-900 font-sans">
      {/* Header section */}
      <div className="flex flex-col mb-8 relative">
        <h2 className="text-2xl font-bold bg-transparent">Revenue Analytics</h2>
        <div className="absolute left-1/2 -translate-x-1/2 top-4 flex items-center text-xl font-medium">
          <span className="text-green-500 mr-1 font-bold text-2xl">$</span>Overall Money
        </div>
      </div>

      {/* Stats summary row */}
      <div className="flex justify-between items-center text-lg font-medium mb-4 px-2">
        <div>
          <span className="text-gray-500 mr-2 font-bold">Today:</span>
          <span className="text-green-500 font-bold">{stats?.today ?? 0}</span>
        </div>
        <div>
          <span className="text-gray-500 mr-2 font-bold">This Month:</span>
          <span className="text-green-500 font-bold">{stats?.thisMonth ?? 0}</span>
        </div>
        <div>
          <span className="text-gray-500 mr-2 font-bold">This Year:</span>
          <span className="text-green-500 font-bold">{stats?.thisYear ?? 0}</span>
        </div>
        <div>
          <span className="text-gray-500 mr-2 font-bold">Debt:</span>
          <span className="text-red-500 font-bold">{stats?.debt ?? 0}</span>
        </div>
      </div>

      <hr className="border-t-[1.5px] border-black w-full" />

      {/* By Year section */}
      <div className="flex items-center gap-4 mt-6 mb-4">
        <h3 className="text-lg font-bold">By Year:</h3>
        <select 
          className="px-4 py-1 border border-gray-400 rounded bg-white text-sm w-28 outline-none"
          value={selectedYear}
          onChange={e => setSelectedYear(e.target.value)}
        >
          <option value="2026">2026</option>
          <option value="2025">2025</option>
        </select>
      </div>

      <div className="mb-6 text-xl font-bold bg-transparent">
        <span className="text-gray-500 mr-2">This Year:</span>
        <span className="text-green-500">{monthlyRevenue?.yearTotal||0} JOD</span>
      </div>

      {/* Months Grid */}
      <div className="grid grid-cols-6 border-l border-t border-indigo-400 mb-6 bg-white overflow-hidden rounded-sm">
        {months.map(m => (
          <div key={m} className="border-r border-b border-indigo-400 p-4 flex flex-col items-center justify-center">
            <span className="text-sm font-medium mb-3">{m}</span>
            <span className="text-green-500 text-sm font-medium">{monthlyRevenue?.months[Number(m.split('-')[0])]} JOD</span> {/*understand this line*/}
          </div>
        ))}
      </div>

      <hr className="border-t-[1.5px] border-black w-full my-6" />

      {/* Money By Period */}
      <div className="mb-4">
        <h3 className="text-lg font-bold">Money By Period</h3>
      </div>

      <div className="grid grid-cols-2 gap-x-6 gap-y-4">
        <div>
          <label className="block text-sm font-bold text-gray-500 mb-1">From</label>
          <input 
            type="date" 
            className="w-full px-3 py-2 border border-indigo-300 rounded bg-white text-sm outline-none"
            value={dateRange.start}
            onChange={e => setDateRange({ ...dateRange, start: e.target.value })}
          />
        </div>
        <div>
          <label className="block text-sm font-bold text-gray-500 mb-1">To</label>
          <input 
            type="date" 
            className="w-full px-3 py-2 border border-indigo-300 rounded bg-white text-sm outline-none"
            value={dateRange.end}
            onChange={e => setDateRange({ ...dateRange, end: e.target.value })}
          />
        </div>
        <div>
          <label className="block text-sm font-bold text-gray-500 mb-1">Gender</label>
          <select 
            className="w-full px-3 py-2 border border-indigo-300 rounded bg-white text-sm outline-none"
            value={gender}
            onChange={e => setGender(e.target.value)}
          >
            <option value="All">All</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>
        </div>
        <div className="flex items-end">
          <button onClick={fetchRevenueByPeriod}
          disabled={dateRange.start===""||dateRange.end===""}
          className="bg-[#4267B2] text-white px-8 py-2 rounded text-sm font-medium hover:bg-[#365899] transition-colors">
            Calculate
          </button>

        </div>
        
      </div>
      {revenueByPeriod === null
  ?
      <div  className="mt-12 text-center text-gray-500 text-lg font-bold pb-8">

        Please select a Period
      </div>
      :
       <div className="mt-12" >
        <div className="flex justify-between items-center text-lg font-medium mb-4 px-2">
            <div>
              <span className="text-gray-500 mr-2 font-bold">Total:</span>
              <span className="text-green-500 font-bold">{revenueByPeriod?.periodTotal||0}</span>
            </div>
            <div>
              <span className="text-gray-500 mr-2 font-bold">Debt:</span>
              <span className="text-red-500 font-bold">{revenueByPeriod?.periodDebt||0}</span>
            </div>
            
           
          </div>

        <Table  data={revenueByPeriod?.revenues||[]} columns={columns} keyExtractor={(row) => row.id} />
      </div>
      }
   

     
  </div>
    </div>
  );
};

export default Revenue;
