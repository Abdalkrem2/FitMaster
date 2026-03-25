
export interface MembershipRequest {
  startDate: string;
  price: number;
  debt: number;
  packageId:string;
  description:string;
}

export interface Membership {
  id:string;
  memberId:string;
  startDate:string;
  endDate:string;
  price: number;
  debt: number;
  packageName:string;
  description:string;
  timestamp:string;
}


export interface MembershipHistory {
  id:string;
  price: number;
  debt: number;
  packageName:string;
  description:string;
  timestamp:string;
}