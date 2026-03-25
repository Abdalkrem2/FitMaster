import { api } from './api';
import type { Member, MemberDetails ,CreateMemberRequest, UpdateMemberRequest} from '../types/member';
import type { PageResponse } from '../types/pagination';


export const memberService = {
 async getAllMembers(page:number,size:number=20): Promise<PageResponse<Member>>  {
  const res = await api.get('/members',
    {params:{page,size}});
  return res.data;
},

  

  getMemberById: async (id: string): Promise<MemberDetails | undefined> => {
    const res= await api.get(`/members/${id}`)
   

    const member = res.data;

    return {
      id: member.id || Number(id),
      fullName: member.fullName,
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
