import { api } from './api';

export const authService = {
  
  login: async (phone: string, password: string) => {
    const res = await api.post('/auth/signin', { phone, password });
    return {
      token: res.data.jwtToken,
      user: {
        id: res.data.id,
        name: res.data.phone,
        roles: res.data.roles
      }
    };
  }
};
