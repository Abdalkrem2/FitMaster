export interface RevenueStats {
  today: number;
  thisMonth: number;
  thisYear: number;
  debt: number;
}


export interface MonthlyRevenue {
  yearTotal:number
  months:Record<number,number>
}


export interface RevenueByPeriod {
  periodTotal:number
  periodDebt:number
 revenues:Revenue[]
}

export interface Revenue{
id:number
addedByName:string
memberName:string
amount:number
pkg:string
debt:number
description:string
createdAt:Date


}