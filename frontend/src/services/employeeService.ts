import { api } from "./api";
import type {
  Employee,
  CreateEmployeeRequest,
  UpdateEmployeeRequest,
} from "../types/employee";

export interface EmployeePageResponse {
  content: Employee[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  lastPage: boolean;
}

export const employeeService = {
  getAllEmployees: async (
    page = 0,
    size = 10,
  ): Promise<EmployeePageResponse> => {
    const res = await api.get("/employees", { params: { page, size } });
    const data = res.data;

    return {
      content: (data.content ?? []).map((e: any) => ({
        id: e.id,
        fullName: e.fullName,
        phone: e.phone,
        gender: e.gender,
        isActivated: e.isActivated,
        profilePicture: e.profilePicture,
        roles: e.roles ?? [],
      })),
      pageNumber: data.pageNumber ?? 0,
      pageSize: data.pageSize ?? size,
      totalElements: data.totalElements ?? 0,
      totalPages: data.totalPages ?? 1,
      lastPage: data.lastPage ?? true,
    };
  },

  createEmployee: async (
    employee: CreateEmployeeRequest,
  ): Promise<Employee> => {
    const res = await api.post("/employees", employee);
    return res.data;
  },

  updateEmployee: async (
    id: number,
    employee: UpdateEmployeeRequest,
  ): Promise<Employee> => {
    const res = await api.patch(`/employees/${id}`, employee);
    return res.data;
  },

  deleteEmployee: async (id: number): Promise<string> => {
    const res = await api.delete(`/employees/${id}`);
    return res.data;
  },
};
