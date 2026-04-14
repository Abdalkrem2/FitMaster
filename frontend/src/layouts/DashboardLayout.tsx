import React, { useState } from "react";
import { Outlet, Navigate } from "react-router-dom";
import { Sidebar } from "../components/Sidebar";
import { EmployeeSidebar } from "../components/EmployeeSidebar";
import { Header } from "../components/Header";
import { useAuth } from "../context/AuthContext";
import { NotificationProvider } from "@/context/NotificationContext";

export const DashboardLayout: React.FC = () => {
  const { user, isAdmin } = useAuth();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  if (!user) return <Navigate to="/login" replace />;

  const toggleSidebar = () => setSidebarOpen((prev) => !prev);
  const closeSidebar = () => setSidebarOpen(false);

  return (
    <div className="flex h-screen bg-slate-50">
      {/* Sidebar — always visible on lg+, toggleable on mobile */}
      {isAdmin() ? (
        <Sidebar isOpen={sidebarOpen} onClose={closeSidebar} />
      ) : (
        <EmployeeSidebar isOpen={sidebarOpen} onClose={closeSidebar} />
      )}

      {/* Main content area */}
      <div className="flex-1 lg:ml-[260px] flex flex-col min-h-screen overflow-hidden transition-all duration-300">
        <NotificationProvider>
          <Header onMenuClick={toggleSidebar} />
        </NotificationProvider>

        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-slate-50 p-4 sm:p-6 lg:p-8">
          <div className="mx-auto max-w-7xl animate-fade-in-up">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};
