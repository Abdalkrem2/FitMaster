import { api } from './api';
import type { Member, MemberDetails ,CreateMemberRequest, UpdateMemberRequest} from '../types/member';


export const memberService = {
  getAllMembers: async (): Promise<Member[]> => {
    const res= await api.get('/members')
   

    const members = res.data.content??[]; //إذا كانت القيمة null أو undefined استخدم القيمة الثانية.
    return members.map((member: any) => ({
      id: member.id,
      name: member.fullName,
      phone: member.phone,
      gender: member.gender,
      endDate: member.endDate,
      addedBy: member.addedByName,
      debt:member.debt,
    }));
  },

  

  getMemberById: async (id: string): Promise<MemberDetails | undefined> => {
    const res= await api.get(`/members/${id}`)
   

    const member = res.data;

    return {
      id: member.id || Number(id),
      name: member.fullName,
      phone: member.phone,
      gender: member.gender,
      startDate: member.startDate,
      endDate: member.endDate,
      profilePicture: member.profilePicture,
      memberships: member.memberships,
      debt: member.debt,
      addedBy: member.addedByName,
    };
  },

  createMember: async (member: CreateMemberRequest): Promise<Member> => {
    const res= await api.post('/members',member)
    console.log(res.data);
    return res.data;
  },

  updateMember:async(id:string,member:UpdateMemberRequest):Promise<Member>=>{
    const res= await api.patch(`/members/${id}`,member)
   
    return res.data;
  },

  deleteMember:async(id:string)=>{
    const res =await api.delete(`/members/${id}`)
    return res.data;
  }
};
