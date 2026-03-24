import { createBrowserRouter, Navigate } from "react-router-dom";
import { DashboardLayout } from "../layouts/DashboardLayout";

// Pages
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Members from "../pages/Members";
import MemberDetails from "../pages/MemberDetails";
import Packages from "../pages/Packages";
import Revenue from "../pages/Revenue";
import ActivityLog from "../pages/ActivityLog";
import Employees from "../pages/Employees";

export const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/",
    element: <DashboardLayout />,
    children: [
      { index: true, element: <Dashboard /> },
      { path: "members", element: <Members /> },
      { path: "members/:id", element: <MemberDetails /> },
      { path: "packages", element: <Packages /> },
      { path: "revenue", element: <Revenue /> },
      { path: "activity", element: <ActivityLog /> },
      { path: "/employees", element: <Employees /> },
    ],
  },
  {
    path: "*",
    element: <Navigate to="/" replace />,
  },
]);
