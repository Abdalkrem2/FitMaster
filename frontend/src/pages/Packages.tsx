import React, { useEffect, useState } from 'react';
import { Package as PackageIcon, Plus, Calendar, DollarSign, BookText } from 'lucide-react';
import { packageService } from '../services/packageService';
import EditPackageModal from '../components/EditPackageModal';
import type { Package } from '../types/package';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { Input } from '../components/ui/Input';
import { Modal } from '../components/ui/Modal';
import { Switch } from "@/components/ui/switch"
const Packages: React.FC = () => {
  const [packages, setPackages] = useState<Package[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingPackage, setEditingPackage] = useState<Package | null>(null);
  // Form State
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [durationInDays, setdurationInDays] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errors, setErrors] = useState<{
  name?: string;
  price?: string;
  durationInDays?: string;
}>({});

  useEffect(() => {
    fetchPackages();
  }, []);

  const fetchPackages = async () => {
    try {
      const data = await packageService.getAllPackages();
      setPackages(data);
    } catch (err) {
      console.error('Failed to load packages');
    } finally {
      setLoading(false);
    }
  };

const validate = () => {
  const newErrors: any = {};

  if (!name) newErrors.name = "Name is required";
  if (!price) newErrors.price = "Price is required";
  if (!durationInDays) newErrors.durationInDays = "Duration is required";

  setErrors(newErrors);

  return Object.keys(newErrors).length === 0;
};


  const handleCreatePackage = async (e: React.FormEvent) => {
    e.preventDefault();
   if (!validate()) return;
    try {
      setIsSubmitting(true);
      const newPkg = await packageService.createPackage({
        name,
        price: Number(price),
        durationInDays: Number(durationInDays),
        description: '',
        status:'ACTIVE'
      });
      setPackages(prev => [...prev, newPkg]);
      setIsModalOpen(false);
      setName(''); setPrice(''); setdurationInDays('');
    } catch (error) {
      console.error("Failed to default package",error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete =async (id: string)=>{
    try {
      await packageService.deletePackage(id);
      setPackages(prev => prev.filter(pkg => pkg.id !== id));
    } catch (error) {
      console.error("Failed to delete package",error);
    }
  };
  
  const handleStatusToggle = async (id: string, currentStatus: string) => {
    const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    try {
      await packageService.updatePackageStatus(id, newStatus);
      setPackages(prev => prev.map(pkg => pkg.id === id ? { ...pkg, status: newStatus } : pkg));
    } catch (error) {
      console.error("Failed to update status", error);
    }
  };
  



 

  return (
    


    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Membership Packages</h2>
          <p className="text-sm text-gray-500 mt-1">Manage subscription plans and pricing.</p>
        </div>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="w-4 h-4 mr-2" /> Create New Package
        </Button>
      </div>

      {loading ? (
        <div className="text-center py-12 text-gray-500">Loading packages...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
          {packages.map((pkg) => (
            <Card key={pkg.id} className="hover:-translate-y-1 transition-transform border border-gray-100 shadow-soft relative overflow-hidden group border-gray-300">
              <div className="absolute top-0 right-0 p-4 opacity-10 group-hover:opacity-20 transition-opacity">
                <PackageIcon size={64} />
              </div>
                   <div className="flex items-center justify-end space-x-2 ">
                  <Switch 
                    className='scale-125 ml-2' 
                    id={pkg.id} 
                    checked={pkg.status === 'ACTIVE'}
                    onCheckedChange={() => handleStatusToggle(pkg.id, pkg.status)}
                  />
                    </div>
              <h3 className="text-xl font-bold text-gray-900 mb-2">{pkg.name}</h3>
              <div className="flex items-end mb-4 text-blue-600">
                <DollarSign className="w-6 h-6 mb-1" />
                <span className="text-4xl font-extrabold">{pkg.price}</span>
              </div>
              <div className="flex items-center text-gray-600 font-medium">
                <Calendar className="w-5 h-5 mr-2" />
                {pkg.durationInDays} days Duration
              </div>
              <div className="flex items-center text-black-400 font-small">
          
                <BookText className="w-5 h-5 mr-2" /> {pkg.description}
              </div>
              <div className="flex gap-3 mt-2 ">
             <Button  onClick={() => handleDelete(pkg.id)}>Delete</Button>
             <Button onClick={()=>{
               setEditingPackage(pkg);
               setIsEditModalOpen(true);
             }}>Edit</Button>
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Create Package Modal */}
      <Modal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)}
        title="Create New Package"
      >
        <form  onSubmit={handleCreatePackage}className="space-y-4">
          <Input 
            label="Plan Name" 
            placeholder="e.g. 1 Year Premium"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
          <Input 
            label="Price ($)" 
            type="number" 
            placeholder="e.g. 200"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
          {errors.price && <p className="text-red-500 text-sm">{errors.price}</p>}
          <Input 
            label="Duration (days)" 
            type="number" 
            placeholder="e.g. 30"
            value={durationInDays}
            onChange={(e) => setdurationInDays(e.target.value)}
            required
          />
          {errors.durationInDays && <p className="text-red-500 text-sm">{errors.durationInDays}</p>}
          <div className="flex justify-end pt-4 mt-6 border-t border-gray-100">
  
            <Button type="button" variant="outline" className="mr-3" onClick={() => setIsModalOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Creating...' : 'Create Package'}
            </Button>
          </div>
        </form>
      </Modal>

      <EditPackageModal 
        isOpen={isEditModalOpen} 
        onClose={() => {
          setIsEditModalOpen(false);
          setEditingPackage(null);
        }} 
        packageItem={editingPackage}
        onUpdate={(updatedPkg) => {
          setPackages(prev => prev.map(p => p.id === updatedPkg.id ? updatedPkg : p));
        }}
      />

    </div>
  );
};

export default Packages;
