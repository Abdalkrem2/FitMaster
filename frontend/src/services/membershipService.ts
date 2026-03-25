import type { MembershipRequest } from "@/types/membership";
import { api } from "./api";


export const membershipService = {
  addMembership: async (memberId:string,membership: MembershipRequest):Promise<void> => {
    await api.post(`/members/${memberId}/memberships`,membership)
  }
};
