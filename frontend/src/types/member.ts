import type { Membership } from "./membership";
export interface Member {
  id: number;
  fullName: string;
  phone: string;
  debt: number;
  addedByName: string;
  endDate:string;
  gender: 'Male' | 'Female';
}

export interface MemberDetails extends Member {
  startDate: string;
  profilePicture:string;
  memberships:Membership[];

}

export interface CreateMemberRequest{
  fullName:string;
  phone:string;
  gender:string;
  profilePicture:string;
 
}

export interface UpdateMemberRequest{
  fullName:string;
  phone:string;
  profilePicture:string;
}