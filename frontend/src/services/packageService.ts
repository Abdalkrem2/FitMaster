import type{ Package,CreatePackageRequest,UpdatePackageRequest } from "../types/package";
import { api } from "./api";


export const packageService = {
  
  getAllPackages: async (): Promise<Package[]> => {
    const res=await api.get("/packages");
       const pakcages=res.data??[];
    return pakcages.map((pkg:any)=>({
    
        id:pkg.id,
        name:pkg.name,
        price:pkg.price,
        durationInDays:pkg.durationDays,
        status:pkg.status,
        description:pkg.description,
      
    }));
  },

  createPackage: async (pkg: CreatePackageRequest): Promise<Package> => {
    const res=await api.post("/packages",pkg);
    return res.data ;
  },

  updatePackageStatus: async (id: string, status: string): Promise<Package> => {
    const res=await api.put(`/packages/${id}/status`,{status});
    return res.data ;
  },

  updatePackage: async (id: string, pkg: UpdatePackageRequest): Promise<Package> => {
    const res=await api.put(`/packages/${id}`,pkg);
    return res.data ;
  },

  deletePackage: async (id: string): Promise<void> => {
    await api.delete(`/packages/${id}`);
  }

};
