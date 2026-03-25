export interface Package {
  id: string;
  name: string;
  price: number;
  durationInDays: number;
  status:string;
  description:string;
}

export interface CreatePackageRequest {
  name: string;
  price: number;
  durationInDays: number;
  description: string;
  status:string;
}

export interface UpdatePackageRequest {
  name?: string;
  price?: number;
  durationInDays?: number;
  description?: string;
}
