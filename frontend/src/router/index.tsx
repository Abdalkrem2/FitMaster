import { createBrowserRouter, Navigate, Outlet } from "react-router-dom";
import { DashboardLayout } from "../layouts/DashboardLayout";
import { useAuth } from "../context/AuthContext";

// Pages
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Members from "../pages/Members";
import MemberDetails from "../pages/MemberDetails";
import Packages from "../pages/Packages";
import Revenue from "../pages/Revenue";
import ActivityLog from "../pages/ActivityLog";
import Employees from "../pages/Employees";
import EmployeeDashboard from "@/pages/EmployeeDashboard";

//Guard: ADMIN only
// if not admin return to tha main page
const AdminOnly = () => {
  const { isAdmin } = useAuth();
  return isAdmin() ? <Outlet /> : <Navigate to="/" replace />;
};

//Guard: Render the appropriate dashboard based on user role
const DashboardRouter = () => {
  const { isAdmin } = useAuth();
  return isAdmin() ? <Dashboard /> : <EmployeeDashboard />;
};

//Router
export const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/",
    element: <DashboardLayout />, // ← الـ DashboardLayout فيه الـ auth guard
    children: [
      // pages for employee & admin
      { index: true, element: <DashboardRouter /> },
      { path: "e-dashboard", element: <EmployeeDashboard /> }, //employee Dashbord
      { path: "members", element: <Members /> },
      { path: "members/:id", element: <MemberDetails /> },

      // pages for admin only
      {
        element: <AdminOnly />,
        children: [
          { path: "packages", element: <Packages /> },
          { path: "revenue", element: <Revenue /> },
          { path: "activity", element: <ActivityLog /> },
          { path: "employees", element: <Employees /> },
        ],
      },
    ],
  },
  {
    path: "*",
    element: <Navigate to="/" replace />,
  },
]);
