import { useState } from "react";
import { useAddEmployee } from "../hooks/useAddEmployee";
import { uploadService } from "@/services/uploadService";
import CameraCaptureModal from "../components/CameraCaptureModal";
import { Camera } from "lucide-react";
import type { AppRole } from "../types/employee";

interface Props {
  open: boolean;
  onClose: () => void;
  onCreated: () => void;
}

export default function AddEmployeeModal({ open, onClose, onCreated }: Props) {
  const { addEmployee, loading, error } = useAddEmployee();
  const [preview, setPreview] = useState<string | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [showCamera, setShowCamera] = useState(false);

  const [formData, setFormData] = useState({
    fullName: "",
    phone: "",
    gender: "",
    password: "",
    role: "EMPLOYEE" as AppRole,
    isActivated: true,
  });

  if (!open) return null;

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>,
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
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
    if (!formData.gender) return "Gender is required";
    if (!formData.password || formData.password.length < 6)
      return "Password must be at least 6 characters";
    return null;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const validationError = validate();
    if (validationError) {
      alert(validationError);
      return;
    }

    try {
      let imageUrl = "";
      if (file) {
        imageUrl = await uploadService.uploadImage(file);
      }

      await addEmployee({ ...formData, profilePicture: imageUrl });
      onCreated();
      onClose();
      // reset form
      setFormData({
        fullName: "",
        phone: "",
        gender: "",
        password: "",
        role: "EMPLOYEE",
        isActivated: true,
      });
      setPreview(null);
      setFile(null);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-40">
      <div className="bg-white rounded-2xl p-6 w-[420px] shadow-xl">
        <h2 className="text-xl font-bold mb-4">Add New Employee</h2>

        {/* Profile Picture Preview */}
        <div className="flex justify-center mb-4">
          <div className="w-20 h-20 rounded-full overflow-hidden bg-gray-100 border flex items-center justify-center text-gray-400">
            {preview ? (
              <img
                src={preview}
                className="w-full h-full object-cover"
                alt="Preview"
              />
            ) : (
              <span className="text-xs">No Image</span>
            )}
          </div>
        </div>

        <div className="space-y-3">
          <input
            name="fullName"
            placeholder="Full Name"
            value={formData.fullName}
            className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          />

          <input
            name="phone"
            placeholder="Phone"
            value={formData.phone}
            className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          />

          <input
            name="password"
            type="password"
            placeholder="Password"
            value={formData.password}
            className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          />

          <select
            name="gender"
            value={formData.gender}
            className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          >
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>

          <select
            name="role"
            value={formData.role}
            className="w-full border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            onChange={handleChange}
          >
            <option value="EMPLOYEE">Employee</option>
            <option value="ADMIN">Admin</option>
          </select>

          {/* Profile Picture Upload */}
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

        {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

        <div className="mt-5 space-y-2">
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700 disabled:opacity-50 transition-colors font-medium"
          >
            {loading ? "Creating..." : "Create Employee"}
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
