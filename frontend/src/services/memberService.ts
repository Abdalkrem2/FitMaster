import { api } from "./api";
import type {
  Member,
  MemberDetails,
  CreateMemberRequest,
  UpdateMemberRequest,
} from "../types/member";
import type { PageResponse } from "../types/pagination";

export const memberService = {
  async getAllMembers(
    page: number,
    size: number = 20,
    search?: string,
  ): Promise<PageResponse<Member>> {
    const res = await api.get("/members", { params: { page, size, search } });
    return res.data;
  },

  getMemberById: async (id: string): Promise<MemberDetails | undefined> => {
    const res = await api.get(`/members/${id}`);
    const member = res.data;
    return member;
  },

  createMember: async (member: CreateMemberRequest): Promise<Member> => {
    const res = await api.post("/members", member);
    return res.data;
  },

  updateMember: async (
    id: string,
    member: UpdateMemberRequest,
  ): Promise<Member> => {
    const res = await api.patch(`/members/${id}`, member);
    return res.data;
  },

  deleteMember: async (id: string) => {
    const res = await api.delete(`/members/${id}`);
    return res.data;
  },

  getMyDetails: async () => {
    const res = await api.get("/members/me");
    return res.data;
  },
};
