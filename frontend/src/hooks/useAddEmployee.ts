import { useState } from "react";
import type { Employee, CreateEmployeeRequest } from "../types/employee";
import { employeeService } from "../services/employeeService.ts";

export const useAddEmployee = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const addEmployee = async (
    request: CreateEmployeeRequest,
  ): Promise<Employee> => {
    setLoading(true);
    setError("");
    try {
      const res = await employeeService.createEmployee(request);
      return res;
    } catch (err) {
      console.error(err, "failed to create employee!");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { addEmployee, loading, error };
};
