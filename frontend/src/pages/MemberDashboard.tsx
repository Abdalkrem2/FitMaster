import React, { useEffect, useState } from "react";
import { Calendar, Plus, ChevronRight, Dumbbell } from "lucide-react";
import { memberService } from "../services/memberService";
import { useNavigate } from "react-router-dom";

const MemberDashboard: React.FC = () => {
  const navigate = useNavigate();
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await memberService.getMyDetails();
        setData(res);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const daysRemaining = data?.endDate
    ? Math.max(
        0,
        Math.ceil(
          (new Date(data.endDate).getTime() - new Date().getTime()) /
            (1000 * 60 * 60 * 24),
        ),
      )
    : 0;

  const isActive = daysRemaining > 0;

  // calculate last member ship
  const lastMembership = data?.memberships?.[0];
  const packageName = lastMembership?.packageName ?? "—";

  if (loading)
    return (
      <div className="flex h-64 items-center justify-center text-gray-400">
        Loading...
      </div>
    );

  return (
    <div className="space-y-6 animate-in fade-in slide-in-from-bottom-4 duration-500">
      {/*greeting */}
      <div className="pb-4 border-b border-gray-200">
        <h1 className="text-2xl font-bold text-gray-900">
          Hello, <span className="text-[#3b5bdb]">{data?.fullName}</span>
        </h1>
        <p className="text-gray-500 text-sm mt-1 font-medium">
          Ready to crush your goals today?
        </p>
      </div>

      {/*Two Column Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* My Plans card  left*/}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 h-full">
            <h2 className="text-lg font-bold text-gray-900 mb-6">My Plans</h2>

            {/* Exercise Plan row "just design"*/}
            <div
              className="flex items-center justify-between py-4 border-b border-gray-100 cursor-pointer hover:bg-gray-50 rounded-lg px-2 transition-colors"
              onClick={() => navigate("/member-plans")}
            >
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-lg bg-gray-100 flex items-center justify-center">
                  <Dumbbell className="w-4 h-4 text-gray-600" />
                </div>
                <span className="text-sm font-medium text-gray-800">
                  My Exercise Plan
                </span>
              </div>
              <div className="flex items-center gap-1 text-sm text-gray-500 font-medium">
                View <ChevronRight className="w-4 h-4" />
              </div>
            </div>

            {/* Generate Plan btn */}
            <div className="flex justify-center mt-8">
              <button
                onClick={() => navigate("/member-plans")}
                className="flex items-center gap-2 px-5 py-2.5 rounded-lg border border-gray-300 text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors"
              >
                <Plus className="w-4 h-4" />
                Generate Plan
              </button>
            </div>
          </div>
        </div>

        {/*Membership +Subscription right*/}
        <div className="space-y-4">
          {/* Membership Card */}
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <div className="flex items-center gap-2 text-gray-500 text-sm font-medium mb-3">
              <Calendar className="w-4 h-4 text-[#3b5bdb]" />
              Membership
            </div>
            <p
              className={`text-3xl font-bold ${
                isActive ? "text-[#3b5bdb]" : "text-red-500"
              }`}
            >
              {isActive ? "Active" : "Expired"}
            </p>
            <p className="text-sm text-gray-500 mt-1">
              {isActive
                ? `Expires in ${daysRemaining} Days`
                : "Please renew your membership"}
            </p>
          </div>

          {/* subscription info card */}
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <div className="flex items-center gap-2 text-gray-500 text-sm font-medium mb-3">
              <Calendar className="w-4 h-4 text-[#3b5bdb]" />
              Subscription info
            </div>
            <p className="text-3xl font-bold text-[#3b5bdb]">{packageName}</p>
            <p className="text-sm text-gray-500 mt-1">
              {lastMembership?.timestamp
                ? `Registered on: ${new Date(lastMembership.timestamp).toLocaleString()}`
                : "No subscription yet"}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MemberDashboard;
