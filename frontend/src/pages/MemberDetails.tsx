import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  ArrowLeft,
  User,
  Phone,
  Calendar,
  CreditCard,
  Activity,
} from "lucide-react";
import { memberService } from "../services/memberService";
import { membershipService } from "../services/membershipService";
import type { MemberDetails } from "../types/member";
import { Button } from "../components/ui/Button";
import { Card, CardHeader, CardTitle } from "../components/ui/Card";
import { Input } from "../components/ui/Input";
import { Table, type Column } from "../components/ui/Table";
import { packageService } from "../services/packageService";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import type { Package } from "@/types/package";
import type { Membership, MembershipHistory } from "@/types/membership";
import EditMemberModal from "../components/EditMemberModal";

const MemberDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [member, setMember] = useState<MemberDetails | null>(null);
  const [memberships, setMemberships] = useState<MembershipHistory[]>([]);
  const [loading, setLoading] = useState(true);

  const [price, setPrice] = useState("");
  const [debt, setDebt] = useState("");
  const [description, setDescription] = useState("");
  const [addingMembership, setAddingMembership] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  const [selectedPackageId, setSelectedPackageId] = useState("");
  const [packages, setPackages] = useState<Package[]>([]);

  useEffect(() => {
    const fetchPackages = async () => {
      const packagesData = await packageService.getAllPackages();
      setPackages(packagesData);
    };
    fetchPackages();
  }, []);

  const handleAddMembership = async (e: React.FormEvent) => {
    if (!member) return;
    e.preventDefault();

    setAddingMembership(true);

    try {
      await membershipService.addMembership(String(id), {
        startDate: new Date().toISOString(),
        price: Number(price),
        debt: Number(debt),
        description: description,
        packageId: selectedPackageId,
      });
      console.log(debt);
      // FIX: The page was not rerendering because we weren't updating the state
      // after adding a membership. We need to re-fetch the member data
      // and update both member and memberships state.
      const updatedMemberData = await memberService.getMemberById(id as string);
      if (updatedMemberData) {
        setMember(updatedMemberData);
        setMemberships(updatedMemberData.memberships || []);
      }

      setPrice("");
      setDebt("");
      setDescription("");
      setSelectedPackageId("");
    } catch (err) {
      console.log(err);
    } finally {
      setAddingMembership(false);
    }
  };

  const loadMemberData = async () => {
    if (!id) return;
    try {
      const memberData = await memberService.getMemberById(id);
      if (memberData) {
        setMember(memberData);
        setMemberships(memberData.memberships || []);
      }
    } catch (err) {
      console.error("Error fetching data:", err);
    }
  };

  useEffect(() => {
    if (!id) return;

    const fetchData = async () => {
      try {
        const memberData = await memberService.getMemberById(id);

        // FIX: The billing history was not working because the memberships state
        // was never initialized with the fetched memberData.memberships
        if (memberData) {
          setMember(memberData);
          setMemberships(memberData.memberships || []);
        }
      } catch (err) {
        console.error("Error fetching data:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleDeleteMember = async () => {
    if (!member) return;
    try {
      await memberService.deleteMember(String(id));
      navigate("/members");
    } catch (err) {
      console.log(err);
    }
  };

  if (loading)
    return <div className="text-center py-12">Loading member details...</div>;
  if (!member)
    return (
      <div className="text-center py-12 text-red-500">Member not found</div>
    );

  const billColumns: Column<MembershipHistory>[] = [
    { key: "timestamp", header: "Date" },
    { key: "packageName", header: "Package" },
    { key: "price", header: "Price", render: (row) => `$${row.price}` },
    {
      key: "debt",
      header: "Debt",
      render: (row) =>
        row.debt > 0 ? (
          <span className="text-red-500 font-medium">${row.debt}</span>
        ) : (
          "$0"
        ),
    },

    { key: "description", header: "Description" },
  ];

  return (
    <div className="space-y-6">
      <div className="flex items-center space-x-4">
        <Button
          variant="secondary"
          onClick={() => navigate("/members")}
          className="p-2 rounded-full"
        >
          <ArrowLeft className="w-5 h-5" />
        </Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Profile Card */}
        <div className="lg:col-span-1 space-y-6">
          <Card className="flex flex-col items-center text-center">
            <div className="w-24 h-24 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 mb-4">
              {member.profilePicture ? (
                <img
                  src={member.profilePicture}
                  className="w-full h-full object-cover rounded-full"
                />
              ) : (
                <User size={48} />
              )}
            </div>

            <h3 className="text-xl font-bold text-gray-900">
              {member.fullName}
            </h3>

            <div className="w-full mt-6 space-y-4 text-left border-t border-gray-100 pt-6">
              <div className="flex items-center text-gray-700">
                <Phone className="w-5 h-5 mr-3 text-gray-400" /> {member.phone}
              </div>
              <div className="flex items-center text-gray-700">
                <User className="w-5 h-5 mr-3 text-gray-400" /> {member.gender}
              </div>
              <div className="flex items-center text-gray-700">
                <Calendar className="w-5 h-5 mr-3 text-gray-400" /> Registered:{" "}
                {member.startDate}
              </div>

              <div className="flex items-center text-gray-700">
                <Activity className="w-5 h-5 mr-3 text-gray-400" /> Ends:{" "}
                {member.endDate}
              </div>
              <div className="flex items-center text-red-600 font-medium pt-2 border-t border-gray-50">
                <CreditCard className="w-5 h-5 mr-3" /> Total Debt: $
                {member.debt}
              </div>
              <div className="flex items-center space-x-2">
                <Button
                  onClick={() => setIsEditModalOpen(true)}
                  className="bg-black text-white"
                >
                  Edit
                </Button>

                <Button
                  onClick={handleDeleteMember}
                  className="bg-red-500 text-white"
                >
                  Delete
                </Button>
              </div>
            </div>
          </Card>
        </div>

        {/* Billing & Forms */}
        <div className="lg:col-span-2 space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Add New Bill</CardTitle>
            </CardHeader>
            <form
              onSubmit={handleAddMembership}
              className="grid grid-cols-1 md:grid-cols-2 gap-4"
            >
              <div className="col-span-1 mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Package
                </label>

                <Select
                  value={selectedPackageId}
                  onValueChange={setSelectedPackageId}
                >
                  <SelectTrigger className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-1 focus:border-blue-500 focus:ring-blue-500 bg-white h-[42px]">
                    <SelectValue placeholder="Select Package" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectGroup>
                      <SelectLabel>Packages</SelectLabel>
                      {packages
                        .filter((p) => p.status === "ACTIVE")
                        .map((pkg) => (
                          <SelectItem key={pkg.id} value={String(pkg.id)}>
                            {pkg.name}
                          </SelectItem>
                        ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              <div className="col-span-1">
                <Input
                  label="Duration (Days)"
                  type="number"
                  value={
                    selectedPackageId
                      ? String(
                          packages.find(
                            (p) => String(p.id) === selectedPackageId,
                          )?.durationInDays ?? "",
                        )
                      : ""
                  }
                  disabled
                />
              </div>

              <div className="col-span-1">
                <Input
                  label="Price ($)"
                  type="number"
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  required
                />
              </div>
              <div className="col-span-1">
                <Input
                  label="Debt ($)"
                  type="number"
                  value={debt}
                  onChange={(e) => setDebt(e.target.value)}
                />
              </div>
              <div className="col-span-1">
                <Input
                  label="Description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>
              <div className="md:col-span-2 flex justify-end">
                <Button type="submit" disabled={addingMembership}>
                  {addingMembership ? "Adding..." : "Add Membership"}
                </Button>
              </div>
            </form>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Billing History</CardTitle>
            </CardHeader>
            <Table
              data={memberships}
              columns={billColumns}
              keyExtractor={(row) => row.id}
            />
          </Card>
        </div>
      </div>

      <EditMemberModal
        open={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        member={member}
        onUpdated={loadMemberData}
      />
    </div>
  );
};

export default MemberDetails;
