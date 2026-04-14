import { api } from "./api";

export const authService = {
  login: async (phone: string, password: string) => {
    try {
      const res = await api.post("/auth/signin", { phone, password });
      return {
        token: res.data.jwtToken,
        user: {
          id: res.data.id,
          phone: res.data.phone,
          name: res.data.fullName,
          roles: res.data.roles,
          isActivated: res.data.isActivated ?? true,
        },
      };
    } catch (err) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      throw err;
    }
  },
};
