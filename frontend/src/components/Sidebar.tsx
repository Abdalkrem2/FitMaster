import React from 'react';
import { NavLink } from 'react-router-dom';
import { LayoutDashboard, Users, Package, DollarSign, Activity, Settings } from 'lucide-react';

const navItems = [
  { name: 'Overview', path: '/', icon: LayoutDashboard },
  { name: 'Members', path: '/members', icon: Users },
  { name: 'Packages', path: '/packages', icon: Package },
  { name: 'Revenue', path: '/revenue', icon: DollarSign },
  { name: 'Activity Log', path: '/activity', icon: Activity },
];

export const Sidebar: React.FC = () => {
  return (
    <aside className="fixed inset-y-0 left-0 w-64 bg-white border-r border-gray-100 flex flex-col z-20 shadow-soft">
      <div className="h-16 flex items-center px-6 border-b border-gray-100">
        <h1 className="text-xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
          FitMaster
        </h1>
      </div>
      
      <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
        {navItems.map((item) => (
          <NavLink
            key={item.name}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center px-4 py-3 text-sm font-medium rounded-xl transition-all duration-200 ${
                isActive
                  ? 'bg-blue-50 text-blue-600'
                  : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500'
              }`
            }
          >
            <item.icon className="h-5 w-5 mr-3" />
            {item.name}
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-gray-100 mt-auto">
        <button className="flex items-center px-4 py-3 text-sm font-medium rounded-xl text-gray-600 hover:bg-gray-50 hover:text-gray-900 transition-colors w-full">
          <Settings className="h-5 w-5 mr-3" />
          Settings
        </button>
      </div>
    </aside>
  );
};
