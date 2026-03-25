export interface Package {
  id: string;
  name: string;
  price: number;
  durationInDays: number;
  status: string;
  description: string;
}

export interface CreatePackageRequest {
  name: string;
  price: number;
  durationDays: number;
  description: string;
  status: string;
}

export interface UpdatePackageRequest {
  name?: string;
  price?: number;
  durationDays?: number;
  description?: string;
}
