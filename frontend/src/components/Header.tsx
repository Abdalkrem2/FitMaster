import React from "react";
import { Bell, LogOut } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";

export const Header: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout, isAdmin, isEmployee } = useAuth(); // get user data

  const handleLogout = () => {
    // localStorage.removeItem("token");
    logout(); //from context
    navigate("/login");
  };

  let displayName = "";
  if (isAdmin()) displayName = "Admin";
  else if (isEmployee()) displayName = "Employee";
  else displayName = user?.name || "";

  return (
    <header className="h-16 bg-white border-b border-gray-100 flex items-center justify-end px-8 z-10 sticky top-0 shadow-sm transition-all">
      <div className="flex items-center space-x-4">
        <button className="text-gray-400 hover:text-gray-600 transition-colors p-2 rounded-full hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 relative">
          <Bell className="h-5 w-5" />
          <span className="absolute top-1 right-2 block h-2 w-2 rounded-full bg-red-400 ring-2 ring-white"></span>
        </button>

        <div className="h-8 w-[1px] bg-gray-200 mx-2"></div>

        <div className="flex items-center space-x-3 cursor-pointer group p-2 rounded-lg hover:bg-gray-50 transition-colors">
          <div className="h-8 w-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-semibold group-hover:bg-blue-200 transition-colors">
            {displayName[0] || "U"} {/* display first char or unKnown*/}
          </div>
          <span className="text-sm font-medium text-gray-700">
            {displayName}
          </span>
        </div>

        <button
          onClick={handleLogout}
          title="Logout"
          className="text-gray-400 hover:text-red-500 transition-colors p-2 rounded-full hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-red-500"
        >
          <LogOut className="h-5 w-5" />
        </button>
      </div>
    </header>
  );
};
