import React, { useEffect, useState, useCallback } from "react";
import { Search, Plus, Pencil, Trash2, ShieldCheck, User } from "lucide-react";
import { employeeService } from "../services/employeeService";
import { Input } from "../components/ui/Input";
import { Button } from "../components/ui/Button";
import { Table, type Column } from "../components/ui/Table";
import type { Employee } from "../types/employee";
import AddEmployeeModal from "./AddEmployeeModel";
import EditEmployeeModal from "./EditEmplyoeeModel";

const Employees: React.FC = () => {
  const [employees, setEmployees] = useState<Employee[]>([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(true);
  const [addOpen, setAddOpen] = useState(false);
  const [editTarget, setEditTarget] = useState<Employee | null>(null);
  const [deleteConfirmId, setDeleteConfirmId] = useState<number | null>(null);

  const fetchEmployees = useCallback(async () => {
    setLoading(true);
    try {
      const data = await employeeService.getAllEmployees();
      setEmployees(data.content);
    } catch (err) {
      console.error("Failed to fetch employees");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchEmployees();
  }, [fetchEmployees]);

  const handleDelete = async (id: number) => {
    try {
      await employeeService.deleteEmployee(id);
      setDeleteConfirmId(null);
      fetchEmployees();
    } catch (err) {
      console.error("Failed to delete employee");
    }
  };

  const filteredEmployees = employees.filter(
    (e) =>
      e.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      e.phone.includes(searchTerm),
  );

  const columns: Column<Employee>[] = [
    { key: "id", header: "ID" },
    {
      key: "fullName",
      header: "Name",
      render: (row) => (
        <div className="flex items-center gap-2">
          {row.profilePicture ? (
            <img
              src={row.profilePicture}
              className="w-8 h-8 rounded-full object-cover border"
              alt={row.fullName}
            />
          ) : (
            <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-600">
              <User size={14} />
            </div>
          )}
          <span className="font-medium text-gray-900">{row.fullName}</span>
        </div>
      ),
    },
    { key: "phone", header: "Phone" },
    { key: "gender", header: "Gender" },
    {
      key: "roles",
      header: "Role",
      render: (row) => {
        const isAdmin = row.roles?.some((r) => r.roleName === "ADMIN");
        return (
          <span
            className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-semibold ${
              isAdmin
                ? "bg-purple-100 text-purple-700"
                : "bg-blue-100 text-blue-700"
            }`}
          >
            <ShieldCheck size={12} />
            {isAdmin ? "Admin" : "Employee"}
          </span>
        );
      },
    },
    {
      key: "isActivated",
      header: "Status",
      render: (row) => (
        <span
          className={`px-2 py-1 rounded-full text-xs font-semibold ${
            row.isActivated
              ? "bg-green-100 text-green-700"
              : "bg-red-100 text-red-700"
          }`}
        >
          {row.isActivated ? "Active" : "Inactive"}
        </span>
      ),
    },
    {
      key: "actions",
      header: "Actions",
      render: (row) => (
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => setEditTarget(row)}
            className="text-blue-600 border-blue-200 hover:bg-blue-50"
          >
            <Pencil className="w-4 h-4 mr-1" /> Edit
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => setDeleteConfirmId(row.id)}
            className="text-red-600 border-red-200 hover:bg-red-50"
          >
            <Trash2 className="w-4 h-4 mr-1" /> Delete
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Employees</h2>
          <p className="text-sm text-gray-500 mt-1">
            Manage and view all gym employees
          </p>
        </div>
        <Button onClick={() => setAddOpen(true)}>
          <Plus className="w-4 h-4 mr-2" /> Add Employee
        </Button>
      </div>

      {/* Table Card */}
      <div className="bg-white rounded-xl shadow-soft border border-gray-100 p-6 space-y-4">
        <div className="flex items-center space-x-4">
          <div className="relative flex-1 max-w-md">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <Search className="h-5 w-5 text-gray-400" />
            </div>
            <Input
              type="text"
              placeholder="Search employees by name or phone..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 mb-0"
              fullWidth
            />
          </div>
        </div>

        {loading ? (
          <div className="py-12 text-center text-gray-500">
            Loading employees...
          </div>
        ) : (
          <Table
            data={filteredEmployees}
            columns={columns}
            keyExtractor={(row) => row.id}
          />
        )}
      </div>

      {/* Add Modal */}
      <AddEmployeeModal
        open={addOpen}
        onClose={() => setAddOpen(false)}
        onCreated={fetchEmployees}
      />

      {/* Edit Modal */}
      <EditEmployeeModal
        open={!!editTarget}
        onClose={() => setEditTarget(null)}
        employee={editTarget}
        onUpdated={() => {
          setEditTarget(null);
          fetchEmployees();
        }}
      />

      {/* Delete Confirm Dialog */}
      {deleteConfirmId !== null && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl p-6 w-[360px] shadow-xl">
            <h3 className="text-lg font-bold text-gray-900 mb-2">
              Delete Employee
            </h3>
            <p className="text-sm text-gray-600 mb-6">
              Are you sure you want to delete this employee? This action cannot
              be undone.
            </p>
            <div className="flex gap-3">
              <button
                onClick={() => handleDelete(deleteConfirmId)}
                className="flex-1 bg-red-600 text-white p-2 rounded hover:bg-red-700 transition-colors font-medium"
              >
                Delete
              </button>
              <button
                onClick={() => setDeleteConfirmId(null)}
                className="flex-1 text-gray-600 p-2 rounded hover:bg-gray-100 transition-colors border"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Employees;
