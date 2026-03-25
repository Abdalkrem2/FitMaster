import React from "react";
import { Outlet, Navigate /*,useLocation*/ } from "react-router-dom";
import { Sidebar } from "../components/Sidebar";
import { EmployeeSidebar } from "../components/EmployeeSidebar";
import { Header } from "../components/Header";
import { useAuth } from "../context/AuthContext";

export const DashboardLayout: React.FC = () => {
  // const token = localStorage.getItem("token");
  // const location = useLocation();

  // if (!token && location.pathname !== "/login") {
  //   return <Navigate to="/login" replace />;
  // }
  const { user, isAdmin } = useAuth();

  if (!user) return <Navigate to="/login" replace />;

  return (
    <div className="flex h-screen bg-[#f3f4f6]">
      {/*check if the user admin or not*/}
      {isAdmin() ? <Sidebar /> : <EmployeeSidebar />}
      <div className="flex-1 ml-64 flex flex-col min-h-screen overflow-hidden">
        <Header />
        <main className="flex-1 overflow-x-hidden overflow-y-auto bg-[#f3f4f6] p-8">
          <div className="mx-auto max-w-7xl animate-in fade-in slide-in-from-bottom-4 duration-500">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};
