export type AppRole = "EMPLOYEE" | "ADMIN";

export interface Employee {
  id: number;
  fullName: string;
  phone: string;
  gender: string;
  isActivated: boolean;
  profilePicture?: string;
  roles: { roleId: number; roleName: AppRole }[];
}

export interface CreateEmployeeRequest {
  fullName: string;
  phone: string;
  gender: string;
  password: string;
  profilePicture?: string;
  role?: AppRole;
  isActivated?: boolean;
}

export interface UpdateEmployeeRequest {
  fullName?: string;
  phone?: string;
  password?: string;
  profilePicture?: string;
  role?: AppRole;
  isActivated?: boolean;
}
