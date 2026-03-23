import { useState } from "react";
import type { Member,CreateMemberRequest } from "../types/member";
import { memberService } from "../services/memberService";


export const useAddMember = () => {
 const [loading,setLoading]=useState(false);
 const [error,setError]=useState('');

 const addMember = async (request: CreateMemberRequest): Promise<Member> => {
    setLoading(true);
    setError('');
    try {
        const res = await memberService.createMember(request);
        return res;
    } catch (err) {
       console.log(err,"faild to create member!");
       throw err;
    } finally {
        setLoading(false);
    }
 }
 return {addMember,loading,error}
    

}
