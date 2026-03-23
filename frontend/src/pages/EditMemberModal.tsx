import React, { useState, useEffect } from "react";
import { uploadService } from "@/services/uploadService";
import { memberService } from "@/services/memberService";
import CameraCaptureModal from "../components/CameraCaptureModal";
import { Camera } from "lucide-react";
import type { MemberDetails } from "@/types/member";

interface Props {
  open: boolean;
  onClose: () => void;
  member: MemberDetails | null;
  onUpdated: () => void;
}

export default function EditMemberModal({ open, onClose, member, onUpdated }: Props) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [showCamera, setShowCamera] = useState(false);
  const [preview, setPreview] = useState<string | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [formData, setFormData] = useState({
    fullName: "",
    phone: "",
  });

  useEffect(() => {
    if (member) {
      setFormData({
        fullName: member.name || "",
        phone: member.phone || "",
      });
      setPreview(member.profilePicture || null);
      setFile(null); // Reset any previously selected file
      setError(null);
    }
  }, [member, open]);

  if (!open || !member) return null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
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
      let imageUrl = member.profilePicture || "";

      if (file) {
        imageUrl = await uploadService.uploadImage(file);
      }

      await memberService.updateMember(String(member.id), {
        fullName: formData.fullName,
        phone: formData.phone,
        profilePicture: imageUrl,
      });

      onUpdated();
      onClose();
    } catch (err) {
      console.error(err);
      setError("Failed to update member");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-40">
      <div className="bg-white rounded-2xl p-6 w-[400px] shadow-xl">
        <h2 className="text-xl font-bold mb-4">Edit Member Details</h2>

        <div className="flex justify-center mb-4">
          <div className="w-24 h-24 rounded-full overflow-hidden bg-gray-100 border flex items-center justify-center text-gray-400">
            {preview ? (
              <img src={preview} className="w-full h-full object-cover" alt="Profile preview" />
            ) : (
              <span className="text-sm">No Image</span>
            )}
          </div>
        </div>

        <div className="space-y-3">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
            <input
              name="fullName"
              placeholder="Full Name"
              value={formData.fullName}
              className="w-full border p-2 rounded focus:ring-blue-500 focus:border-blue-500 outline-none"
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Phone</label>
            <input
              name="phone"
              placeholder="Phone"
              value={formData.phone}
              className="w-full border p-2 rounded focus:ring-blue-500 focus:border-blue-500 outline-none"
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Profile Picture</label>
            <div className="flex items-center space-x-2">
              <input
                name="profilePicture"
                type="file"
                accept="image/*"
                className="flex-1 border p-2 rounded text-sm"
                onChange={handleFileChange}
              />
              <button
                onClick={() => setShowCamera(true)}
                className="p-2 border rounded hover:bg-gray-50 flex items-center justify-center bg-gray-100 transition-colors"
                title="Take Photo"
                type="button"
              >
                <Camera size={20} />
              </button>
            </div>
          </div>
        </div>

        {error && <p className="text-red-500 text-sm mt-3">{error}</p>}

        <div className="mt-6 space-y-2">
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700 disabled:opacity-50 transition-colors font-medium"
          >
            {loading ? "Saving..." : "Save Changes"}
          </button>

          <button
            onClick={onClose}
            className="w-full text-gray-600 p-2 rounded hover:bg-gray-100 transition-colors"
            disabled={loading}
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
