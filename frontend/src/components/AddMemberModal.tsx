import { useState } from "react";
import { useAddMember } from "../hooks/useAddMember";
import { useNavigate } from "react-router-dom";
import { uploadService } from "@/services/uploadService";
import CameraCaptureModal from "../components/CameraCaptureModal";
import { Camera } from "lucide-react";
import type { Member } from "@/types/member";

interface Props{
    open:boolean;
    onClose:()=>void;
    onSuccess:(newMember:Member)=>void;
}

export default function AddMemberModal({open,onClose,onSuccess}:Props){
    const navigate = useNavigate();
    const [validationError, setValidationError] = useState<string | null>(null);
    const {addMember,loading,error}=useAddMember();
    const [preview, setPreview] = useState<string | null>(null);
    const [formData,setFormData]=useState({
        fullName:"",
        phone:"",
        gender:"",

    });
    const [file, setFile] = useState<File | null>(null);
    const [showCamera, setShowCamera] = useState(false);

    if(!open) return null;

    const handleChange=(e:React.ChangeEvent<any>)=>{
        const {name,value}=e.target;
        setFormData(prev => ({
    ...prev,
    [name]: value, 
  }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
   if (e.target.files && e.target.files.length > 0) {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);  
    if (preview) URL.revokeObjectURL(preview);
    setPreview(URL.createObjectURL(selectedFile));
  }
};
const resetForm = () => {
  setFormData({ fullName: "", phone: "", gender: "" });
  setFile(null);
  if (preview) URL.revokeObjectURL(preview);
  setPreview(null);
};
  const handleClose = () => {
  resetForm();
  onClose();
};

    const validate=()=>{
        
        if(!formData.fullName) return "Full name is required";
        if(!formData.phone) return "Phone is required";
        if(!formData.gender) return "Gender is required";
        return null;
    };
       
      const handleSubmit=async(e:React.FormEvent)=>{
        e.preventDefault();
        const error=validate();
        if(error){
             setValidationError(error);
              return;
        }
          setValidationError(null);
        try {

           let imageUrl = "";

    if (file) {
      const uploadRes=await uploadService.uploadImage(file);

      imageUrl = uploadRes;
    }

            const res= await addMember({...formData,profilePicture:imageUrl});
            onSuccess(res);
            handleClose();
            navigate(`/members/${res.id}`);
        } catch (err) {
            console.log(err);
        }
    };


   return (
    <div onClick={handleClose} className="fixed inset-0 bg-opacity-90 flex items-center justify-center">
      <div onClick={(e)=>e.stopPropagation()} className="bg-white rounded-2xl p-6 w-[400px] shadow-xl">
        
        <h2 className="text-l font-bold mb-1">Register New Member</h2>

        <div className="flex justify-center mb-3">
        {preview && <img src={preview} className="w-24 h-24 rounded-full " />}
        </div>
        <input
          name="fullName"
          placeholder="Full Name"
          value={formData.fullName}
          className="w-full border p-2 mb-2 rounded"
          onChange={handleChange}
        />

        <input
          name="phone"
          placeholder="Phone"
          value={formData.phone}
          className="w-full border p-2 mb-2 rounded"
          onChange={handleChange}
        />

      

        <select
          name="gender"
          value={formData.gender}
          className="w-full border p-2 mb-2 rounded"
          onChange={handleChange}
        >
          
          <option value="Male">Male</option>
          <option value="Female">Female</option>  
        </select>

        <div className="flex items-center space-x-2 mb-2">
            <input
              name="profilePicture"
              type="file"
              className="flex-1 border p-2 rounded"
              onChange={handleFileChange}
            />
            <button
            
              onClick={() => setShowCamera(true)}
              className="p-2 border rounded hover:bg-gray-50 flex items-center justify-center bg-gray-100"
              title="Take Photo"
              type="button"
            >
              <Camera size={24} />
            </button>
        </div>

       

        {validationError &&
            <p className="text-red-500">{validationError}</p>}
        {error &&
            <p className="text-red-500">{error}</p>}

        <button
          onClick={handleSubmit}
          disabled={loading}
          className="w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700"
        >
          {loading ? "Creating..." : "Create Member"}
        </button>

        <button
          onClick={handleClose}
          className="w-full mt-2 text-gray-500"
        >
          Cancel
        </button>
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

