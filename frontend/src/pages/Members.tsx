import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Eye, Plus } from 'lucide-react';
import { memberService} from '../services/memberService';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { Table, type Column } from '../components/ui/Table';
import type { Member } from '../types/member';
import AddMemberModal from '../components/AddMemberModal';
import { getRemainingDays } from '@/utils/date';

const Members: React.FC = () => {
  const [members, setMembers] = useState<Member[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [open,setOpen]=useState(false);
  const [error,setError]=useState<string|null>(null);
  const navigate = useNavigate();

  const [page,setPage]=useState(0);
  console.log(page);
  const [totalPages,setTotalPages]=useState(0);

  useEffect(() => {
    const fetchMembers = async () => {
      try {
        const data = await memberService.getAllMembers(page);
        setMembers(data.content);
        setTotalPages(data.totalPages);
      } catch (err) {
        setError("Failed to fetch members");
      } finally {
        setLoading(false);
      }
    };
    fetchMembers();
  }, [page]);




  const filteredMembers = members.filter(m => 
    m.fullName.toLowerCase().includes(searchTerm.toLowerCase()) || 
    m.phone.includes(searchTerm)
  );

  const columns: Column<Member>[] = [
    { key: 'id', header: 'ID' },
    { key: 'fullName', header: 'Name', render: (row) => <span className="font-medium text-gray-900">{row.fullName}</span> },
    { key: 'phone', header: 'Phone' },
     {key:"gender",header:"Gender"}, 


    { key: 'debt', header: 'Debt', render: (row) => {
    if(row.debt>0)

      return <span className="text-orange-800 font-bold">{row.debt}</span>
    else
      return <span className="text-green-800 font-bold">clear</span>
    }},

    
    { key: 'endDate', header: 'End Date' ,render: (row) => {
      if(!row.endDate){
        return <span className="text-red-800 font-bold">Inactive</span>
      }
    const days=getRemainingDays(row.endDate);

  return (
    <div className="flex flex-col">
       <span className={`text-sm ${days <= 3 ? "text-red-500" : "text-green-800 font-bold"}`}>
        {days > 0
          ? `${days} days `
          : "Expired"}
      </span>

      <span className="text-gray-800 ">
        {row.endDate}
      </span>

     
    </div>
    )}},


    { key: 'addedBy', header: 'Added By' },
   

    {
      key: 'actions', header: 'Actions', render: (row) => (
        <Button variant="outline" size="sm" onClick={() => navigate(`/members/${row.id}`)} className="text-blue-600 border-blue-200 hover:bg-blue-50">
          <Eye className="w-4 h-4 mr-2" /> View
        </Button>
      )
    }
  ];




  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Members</h2>
          <p className="text-sm text-gray-500 mt-1">Manage and view all gym members</p>
        </div>
        <Button onClick={()=> setOpen(true) }>
          <Plus className="w-4 h-4 mr-2" /> Add Member
        </Button>
        <AddMemberModal open={open} onClose={()=>setOpen(false)} onSuccess={(newMember)=>setMembers(prev=>[...prev,newMember])} />
      </div>

      <div className="bg-white rounded-xl shadow-soft border border-gray-100 p-6 space-y-4">
        <div className="flex items-center space-x-4">
          <div className="relative flex-1 max-w-md">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none mb-4">
              <Search className="h-5 w-5 text-gray-400" />
            </div>
            <Input
              type="text"
              placeholder="Search members by name or phone..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10 mb-0"
              fullWidth
            />
          </div>
        </div>

        {loading ? (
          <div className="py-12 text-center text-gray-500">Loading members...</div>
         ) : error ? (
  <div className="text-red-500">{error}</div>
        ) : (
          <Table data={filteredMembers} columns={columns} keyExtractor={(row) => row.id}  />
          
        )}
        
        <div className="flex justify-between items-center pt-4">

          <span className="text-sm text-gray-500">
            Page {page + 1} of {totalPages}
          </span>

          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              disabled={page === 0}
              onClick={() => setPage(p => p - 1)}>
              Previous
            </Button>
            <Button
              variant="outline"
              size="sm"
              disabled={page >= totalPages - 1}
              onClick={() => setPage(p => p + 1)}>
              Next
    </Button>
  </div>

</div>
      </div>
      
    </div>
  );
};

export default Members;
