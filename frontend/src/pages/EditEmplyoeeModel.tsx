import React, { useState, useEffect } from "react";
import { uploadService } from "@/services/uploadService";
import { employeeService } from "@/services/employeeService";
import CameraCaptureModal from "../components/CameraCaptureModal";
import { Camera } from "lucide-react";
import type { Employee, AppRole } from "../types/employee";

interface Props {
  open: boolean;
  onClose: () => void;
  employee: Employee | null;
  onUpdated: () => void;
}

export default function EditEmployeeModal({
  open,
  onClose,
  employee,
  onUpdated,
}: Props) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [showCamera, setShowCamera] = useState(false);
  const [preview, setPreview] = useState<string | null>(null);
  const [file, setFile] = useState<File | null>(null);

  const [formData, setFormData] = useState({
    fullName: "",
    phone: "",
    password: "",
    role: "EMPLOYEE" as AppRole,
    isActivated: true,
  });

  useEffect(() => {
    if (employee) {
      const currentRole = employee.roles?.find(
        (r) => r.roleName === "ADMIN" || r.roleName === "EMPLOYEE",
      );
      setFormData({
        fullName: employee.fullName || "",
        phone: employee.phone || "",
        password: "",
        role: (currentRole?.roleName as AppRole) ?? "EMPLOYEE",
        isActivated: employee.isActivated ?? true,
      });
      setPreview(employee.profilePicture || null);
      setFile(null);
      setError(null);
    }
  }, [employee, open]);

  if (!open || !employee) return null;

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleToggleActive = () => {
    setFormData((prev) => ({ ...prev, isActivated: !prev.isActivated }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const selectedFile = e.target.files[0];
      setFile(selectedFile);
      setPreview(URL.createObjectURL(selectedFile));
    }
  };

  const validate = () => {
    if (!formData.fullName) return "Full name is required";
    if (!formData.phone) return "Phone is required";
    if (formData.password && formData.password.length < 6)
      return "Password must be at least 6 characters";
    return null;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      let imageUrl = employee.profilePicture || "";
      if (file) {
        imageUrl = await uploadService.uploadImage(file);
      }

      await employeeService.updateEmployee(employee.id, {
        fullName: formData.fullName,
        phone: formData.phone,
        ...(formData.password ? { password: formData.password } : {}),
        role: formData.role,
        isActivated: formData.isActivated,
        profilePicture: imageUrl,
      });

      onUpdated();
      onClose();
    } catch (err) {
      console.error(err);
      setError("Failed to update employee");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-40">
      <div className="bg-white rounded-2xl p-6 w-[420px] shadow-xl">
        <h2 className="text-xl font-bold mb-4">Edit Employee</h2>

        {/* Profile Picture Preview */}
        <div className="flex justify-center mb-4">
          <div className="w-20 h-20 rounded-full overflow-hidden bg-gray-100 border flex items-center justify-center text-gray-400">
            {preview ? (
              <img
                src={preview}
                className="w-full h-full object-cover"
                alt="Profile"
              />
            ) : (
              <span className="text-xs">No Image</span>
            )}
          </div>
        </div>

        <div className="space-y-3">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Full Name
            </label>
            <input
              name="fullName"
              value={formData.fullName}
              className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Phone
            </label>
            <input
              name="phone"
              value={formData.phone}
              className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              New Password{" "}
              <span className="text-gray-400 text-xs">
                (leave blank to keep current)
              </span>
            </label>
            <input
              name="password"
              type="password"
              placeholder="New password"
              value={formData.password}
              className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Role
            </label>
            <select
              name="role"
              value={formData.role}
              className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              onChange={handleChange}
            >
              <option value="EMPLOYEE">Employee</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>

          {/* Active Toggle */}
          <div className="flex items-center justify-between p-2 border rounded">
            <span className="text-sm font-medium text-gray-700">
              Account Status
            </span>
            <button
              type="button"
              onClick={handleToggleActive}
              className={`px-3 py-1 rounded-full text-xs font-semibold transition-colors ${
                formData.isActivated
                  ? "bg-green-100 text-green-700 hover:bg-green-200"
                  : "bg-red-100 text-red-700 hover:bg-red-200"
              }`}
            >
              {formData.isActivated ? "Active" : "Inactive"}
            </button>
          </div>

          {/* Profile Picture Upload */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Profile Picture
            </label>
            <div className="flex items-center space-x-2">
              <input
                type="file"
                accept="image/*"
                className="flex-1 border p-2 rounded text-sm"
                onChange={handleFileChange}
              />
              <button
                type="button"
                onClick={() => setShowCamera(true)}
                className="p-2 border rounded hover:bg-gray-50 bg-gray-100 transition-colors"
                title="Take Photo"
              >
                <Camera size={20} />
              </button>
            </div>
          </div>
        </div>

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <div className="mt-5 space-y-2">
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700 disabled:opacity-50 transition-colors font-medium"
          >
            {loading ? "Saving..." : "Save Changes"}
          </button>
          <button
            onClick={onClose}
            disabled={loading}
            className="w-full text-gray-600 p-2 rounded hover:bg-gray-100 transition-colors"
          >
            Cancel
          </button>
        </div>
      </div>

      <CameraCaptureModal
        open={showCamera}
        onClose={() => setShowCamera(false)}
        onCapture={(capturedFile) => {
          setFile(capturedFile);
          setPreview(URL.createObjectURL(capturedFile));
        }}
      />
    </div>
  );
}
