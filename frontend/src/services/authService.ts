import { api } from "./api";
import { employeeService } from "./employeeService";

export const authService = {
  login: async (phone: string, password: string) => {
    const res = await api.post("/auth/signin", { phone, password });

    // Get employee details to include isActivated status
    let isActivated = true; // Default for admins
    if (res.data.roles.includes("EMPLOYEE")) {
      try {
        const employees = await employeeService.getAllEmployees(0, 100);
        const employee = employees.content.find((e) => e.id === res.data.id);
        isActivated = employee?.isActivated ?? true;
      } catch (error) {
        console.error("Failed to fetch employee details:", error);
        // Default to true if we can't fetch details
        isActivated = true;
      }
    }

    return {
      token: res.data.jwtToken,
      user: {
        id: res.data.id,
        name: res.data.phone,
        roles: res.data.roles,
        isActivated: isActivated,
      },
    };
  },
};
