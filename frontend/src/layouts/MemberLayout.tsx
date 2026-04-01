import { Outlet, Navigate, NavLink } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { LogOut, Activity } from "lucide-react";

export const MemberLayout: React.FC = () => {
  const { user, logout } = useAuth();

  if (!user) return <Navigate to="/login" replace />;

  return (
    <div className="min-h-screen bg-[#eef2f7]">
      {/* ─── Header ─── */}
      <header className="bg-white border-b border-gray-200">
        <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
          {/* Logo */}
          <div className="flex items-center gap-2">
            <Activity className="text-blue-600" />
            <span className="text-xl font-extrabold text-gray-900">
              Fit<span className="text-[#3b5bdb]">Master</span>
            </span>
          </div>

          {/* Nav */}
          <nav className="flex items-center gap-1">
            <NavLink
              to="/member-dashboard"
              className={({ isActive }) =>
                `flex items-center px-4 py-3 text-sm font-medium rounded-xl transition-all duration-200 ${
                  isActive
                    ? "bg-blue-50 text-blue-600"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                }`
              }
            >
              Home
            </NavLink>
            <NavLink
              to="/member-plans"
              className={({ isActive }) =>
                `flex items-center px-4 py-3 text-sm font-medium rounded-xl transition-all duration-200 ${
                  isActive
                    ? "bg-blue-50 text-blue-600"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                }`
              }
            >
              Workout Plans
            </NavLink>
            <NavLink
              to="/member-nutrition"
              className={({ isActive }) =>
                `flex items-center px-4 py-3 text-sm font-medium rounded-xl transition-all duration-200 ${
                  isActive
                    ? "bg-blue-50 text-blue-600"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                }`
              }
            >
              Nutrition Plans
            </NavLink>
            <NavLink
              to="/member-profile"
              className={({ isActive }) =>
                `flex items-center px-4 py-3 text-sm font-medium rounded-xl transition-all duration-200 ${
                  isActive
                    ? "bg-blue-50 text-blue-600"
                    : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                }`
              }
            >
              Profile
            </NavLink>
          </nav>

          {/* Logout */}
          <button
            onClick={logout}
            className="flex items-center gap-2 px-5 py-2 rounded-lg border-2 border-gray-300 text-sm font-semibold text-gray-700 hover:bg-gray-50 transition-colors"
          >
            <LogOut className="w-4 h-4" />
            Logout
          </button>
        </div>
      </header>

      {/*page content.. hehehehe*/}
      <main className="max-w-7xl mx-auto px-6 py-8">
        <Outlet />
      </main>
    </div>
  );
};
