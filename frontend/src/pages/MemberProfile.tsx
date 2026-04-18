import { useEffect, useState } from "react";
import axios from "axios";
import {
  User,
  Target,
  TrendingUp,
  Zap,
  Calendar,
  CheckCircle2,
  Save,
  AlertCircle,
  Scale,
  Ruler,
  Clock,
  Shield,
} from "lucide-react";
import { memberService } from "../services/memberService";
import type {
  MemberProfile,
  FitnessGoal,
  FitnessLevel,
  InjuryType,
  TrainingStyle,
  UpdateMemberProfileRequest,
} from "../types/member";

const fitnessGoals: {
  value: FitnessGoal;
  label: string;
  icon: typeof Target;
  color: string;
  bg: string;
}[] = [
  {
    value: "MUSCLE_GAIN",
    label: "Muscle Gain",
    icon: TrendingUp,
    color: "text-indigo-600",
    bg: "bg-indigo-50 border-indigo-200",
  },
  {
    value: "WEIGHT_LOSS",
    label: "Weight Loss",
    icon: Scale,
    color: "text-rose-600",
    bg: "bg-rose-50 border-rose-200",
  },
  {
    value: "ENDURANCE",
    label: "Endurance",
    icon: Zap,
    color: "text-amber-600",
    bg: "bg-amber-50 border-amber-200",
  },
  {
    value: "GENERAL_FITNESS",
    label: "General Fitness",
    icon: Target,
    color: "text-emerald-600",
    bg: "bg-emerald-50 border-emerald-200",
  },
];

const fitnessLevels: { value: FitnessLevel; label: string; desc: string }[] = [
  { value: "BEGINNER", label: "Beginner", desc: "0–1 year" },
  { value: "INTERMEDIATE", label: "Intermediate", desc: "1–3 years" },
  { value: "ADVANCED", label: "Advanced", desc: "3+ years" },
];

const trainingStyles: { value: TrainingStyle; label: string; desc: string }[] =
  [
    { value: "STRENGTH", label: "Strength", desc: "Heavy compound lifts" },
    { value: "HYPERTROPHY", label: "Hypertrophy", desc: "Muscle building" },
    { value: "CIRCUIT", label: "Circuit", desc: "High intensity" },
  ];

const injuryOptions: InjuryType[] = [
  "KNEE",
  "SHOULDER",
  "LOWER_BACK",
  "UPPER_BACK",
  "WRIST",
  "ANKLE",
  "NECK",
  "ELBOW",
  "HIP",
];

const MemberProfile = () => {
  const [profile, setProfile] = useState<MemberProfile | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  useEffect(() => {
    const loadProfile = async () => {
      try {
        const data = await memberService.getMyProfile();
        if (data) {
          setProfile(data);
        } else {
          setProfile({
            goal: "GENERAL_FITNESS",
            fitnessLevel: "BEGINNER",
            daysPerWeek: 3,
            injuries: [],
            weight: undefined,
            height: undefined,
            age: undefined,
            trainingStyle: "STRENGTH",
          });
        }
      } catch (err) {
        if (axios.isAxiosError(err)) {
          const status = err.response?.status;
          if (status === 401 || status === 403) {
            setError("You must be signed in as a member to view this page.");
          } else {
            setError(
              err.response?.data?.message ??
                "Unable to load profile. Please try again.",
            );
          }
        } else {
          setError("Unable to load profile. Please try again.");
        }
      } finally {
        setIsLoading(false);
      }
    };
    loadProfile();
  }, []);

  const handleChange = <K extends keyof MemberProfile>(
    key: K,
    value: MemberProfile[K],
  ) => {
    setProfile((current) => (current ? { ...current, [key]: value } : current));
  };

  const handleInjuriesToggle = (injury: InjuryType) => {
    setProfile((current) => {
      if (!current) return current;
      const injuries = current.injuries ?? [];
      const hasInjury = injuries.includes(injury);
      return {
        ...current,
        injuries: hasInjury
          ? injuries.filter((item) => item !== injury)
          : [...injuries, injury],
      };
    });
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!profile) return;
    setIsSaving(true);
    setError(null);
    setSuccess(null);
    const request: UpdateMemberProfileRequest = {
      goal: profile.goal,
      fitnessLevel: profile.fitnessLevel,
      daysPerWeek: profile.daysPerWeek,
      injuries: profile.injuries,
      weight: profile.weight,
      height: profile.height,
      age: profile.age,
      trainingStyle: profile.trainingStyle,
    };
    try {
      const updated = await memberService.updateMyProfile(request);
      setProfile(updated);
      setSuccess("Profile saved successfully.");
    } catch (err) {
      setError("Unable to save profile. Please try again.");
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex h-64 items-center justify-center text-slate-400">
        Loading profile...
      </div>
    );
  }

  return (
    <div className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-500">
      {/* ─── Page Header ─── */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">My Profile</h1>
          <p className="text-slate-400 text-sm mt-1">
            Manage your fitness profile and training preferences.
          </p>
        </div>
        <div className="flex items-center gap-3 px-4 py-2.5 rounded-2xl bg-indigo-50 border border-indigo-100 self-start sm:self-auto">
          <div className="w-9 h-9 rounded-full bg-gradient-to-br from-indigo-500 to-violet-600 flex items-center justify-center">
            <User className="w-4 h-4 text-white" />
          </div>
          <div>
            <p className="text-sm font-semibold text-slate-800">Member</p>
            <p className="text-xs text-indigo-500">Premium Member</p>
          </div>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* ─── Feedback Messages ─── */}
        {error && (
          <div className="flex items-center gap-3 px-4 py-3 rounded-xl bg-red-50 border border-red-100 text-red-700 text-sm">
            <AlertCircle className="w-4 h-4 shrink-0" />
            {error}
          </div>
        )}
        {success && (
          <div className="flex items-center gap-3 px-4 py-3 rounded-xl bg-emerald-50 border border-emerald-100 text-emerald-700 text-sm">
            <CheckCircle2 className="w-4 h-4 shrink-0" />
            {success}
          </div>
        )}

        {/* ─── Body Metrics ─── */}
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-50 flex items-center gap-2">
            <div className="w-7 h-7 rounded-lg bg-slate-50 flex items-center justify-center">
              <Ruler className="w-3.5 h-3.5 text-slate-500" />
            </div>
            <h2 className="text-sm font-semibold text-slate-700">
              Body Metrics
            </h2>
          </div>
          <div className="p-6 grid grid-cols-1 sm:grid-cols-3 gap-4">
            {[
              {
                key: "weight" as const,
                label: "Weight",
                unit: "kg",
                icon: Scale,
                min: 30,
                max: 300,
              },
              {
                key: "height" as const,
                label: "Height",
                unit: "cm",
                icon: Ruler,
                min: 100,
                max: 250,
              },
              {
                key: "age" as const,
                label: "Age",
                unit: "years",
                icon: Calendar,
                min: 10,
                max: 100,
              },
            ].map(({ key, label, unit, icon: Icon, min, max }) => (
              <div key={key}>
                <label className="block text-xs font-semibold text-slate-500 uppercase tracking-wider mb-2">
                  {label}
                </label>
                <div className="relative">
                  <div className="absolute left-3 top-1/2 -translate-y-1/2">
                    <Icon className="w-4 h-4 text-slate-300" />
                  </div>
                  <input
                    type="number"
                    min={min}
                    max={max}
                    value={profile?.[key] ?? ""}
                    onChange={(e) =>
                      handleChange(
                        key,
                        e.target.value ? Number(e.target.value) : undefined,
                      )
                    }
                    className="w-full pl-10 pr-14 py-3 rounded-xl border border-slate-200 text-sm text-slate-800 font-medium bg-slate-50 focus:bg-white focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100 outline-none transition-all"
                    placeholder={`Enter ${label.toLowerCase()}`}
                  />
                  <span className="absolute right-3 top-1/2 -translate-y-1/2 text-xs text-slate-400 font-medium">
                    {unit}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* ─── Fitness Goal ─── */}
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-50 flex items-center gap-2">
            <div className="w-7 h-7 rounded-lg bg-indigo-50 flex items-center justify-center">
              <Target className="w-3.5 h-3.5 text-indigo-500" />
            </div>
            <h2 className="text-sm font-semibold text-slate-700">
              Fitness Goal
            </h2>
          </div>
          <div className="p-6 grid grid-cols-1 sm:grid-cols-2 gap-3">
            {fitnessGoals.map(({ value, label, icon: Icon, color, bg }) => (
              <button
                key={value}
                type="button"
                onClick={() => handleChange("goal", value)}
                className={`flex items-center gap-3 p-4 rounded-xl border-2 transition-all duration-200 text-left ${
                  profile?.goal === value
                    ? `${bg} ${color} border-current shadow-sm`
                    : "border-slate-100 hover:border-slate-200 text-slate-600 hover:bg-slate-50"
                }`}
              >
                <div
                  className={`w-9 h-9 rounded-xl flex items-center justify-center shrink-0 ${
                    profile?.goal === value ? "bg-white/60" : "bg-slate-100"
                  }`}
                >
                  <Icon
                    className={`w-4 h-4 ${profile?.goal === value ? color : "text-slate-400"}`}
                  />
                </div>
                <span className="text-sm font-semibold">{label}</span>
                {profile?.goal === value && (
                  <CheckCircle2 className={`w-4 h-4 ml-auto ${color}`} />
                )}
              </button>
            ))}
          </div>
        </div>

        {/* ─── Fitness Level + Training Style ─── */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
          {/* Fitness Level */}
          <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
            <div className="px-5 py-4 border-b border-slate-50 flex items-center gap-2">
              <div className="w-7 h-7 rounded-lg bg-violet-50 flex items-center justify-center">
                <TrendingUp className="w-3.5 h-3.5 text-violet-500" />
              </div>
              <h2 className="text-sm font-semibold text-slate-700">
                Fitness Level
              </h2>
            </div>
            <div className="p-5 space-y-2">
              {fitnessLevels.map(({ value, label, desc }) => (
                <button
                  key={value}
                  type="button"
                  onClick={() => handleChange("fitnessLevel", value)}
                  className={`w-full flex items-center justify-between p-3 rounded-xl border transition-all duration-200 ${
                    profile?.fitnessLevel === value
                      ? "border-violet-200 bg-violet-50 text-violet-700"
                      : "border-slate-100 hover:border-slate-200 hover:bg-slate-50 text-slate-600"
                  }`}
                >
                  <div className="flex items-center gap-3 text-left">
                    <div
                      className={`w-2 h-2 rounded-full ${
                        profile?.fitnessLevel === value
                          ? "bg-violet-500"
                          : "bg-slate-300"
                      }`}
                    />
                    <div>
                      <p className="text-sm font-semibold">{label}</p>
                      <p className="text-xs opacity-60">{desc}</p>
                    </div>
                  </div>
                  {profile?.fitnessLevel === value && (
                    <CheckCircle2 className="w-4 h-4 text-violet-500" />
                  )}
                </button>
              ))}
            </div>
          </div>

          {/* Training Style */}
          <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
            <div className="px-5 py-4 border-b border-slate-50 flex items-center gap-2">
              <div className="w-7 h-7 rounded-lg bg-amber-50 flex items-center justify-center">
                <Zap className="w-3.5 h-3.5 text-amber-500" />
              </div>
              <h2 className="text-sm font-semibold text-slate-700">
                Training Style
              </h2>
            </div>
            <div className="p-5 space-y-2">
              {trainingStyles.map(({ value, label, desc }) => (
                <button
                  key={value}
                  type="button"
                  onClick={() => handleChange("trainingStyle", value)}
                  className={`w-full flex items-center justify-between p-3 rounded-xl border transition-all duration-200 ${
                    profile?.trainingStyle === value
                      ? "border-amber-200 bg-amber-50 text-amber-700"
                      : "border-slate-100 hover:border-slate-200 hover:bg-slate-50 text-slate-600"
                  }`}
                >
                  <div className="flex items-center gap-3 text-left">
                    <div
                      className={`w-2 h-2 rounded-full ${
                        profile?.trainingStyle === value
                          ? "bg-amber-500"
                          : "bg-slate-300"
                      }`}
                    />
                    <div>
                      <p className="text-sm font-semibold">{label}</p>
                      <p className="text-xs opacity-60">{desc}</p>
                    </div>
                  </div>
                  {profile?.trainingStyle === value && (
                    <CheckCircle2 className="w-4 h-4 text-amber-500" />
                  )}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* ─── Days Per Week ─── */}
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-50 flex items-center gap-2">
            <div className="w-7 h-7 rounded-lg bg-emerald-50 flex items-center justify-center">
              <Clock className="w-3.5 h-3.5 text-emerald-500" />
            </div>
            <h2 className="text-sm font-semibold text-slate-700">
              Training Days Per Week
            </h2>
          </div>
          <div className="p-6">
            <div className="flex items-center gap-3 mb-4">
              <span className="text-3xl font-bold text-slate-800">
                {profile?.daysPerWeek}
              </span>
              <span className="text-sm text-slate-400">days per week</span>
            </div>
            <div className="flex gap-2">
              {[1, 2, 3, 4, 5, 6, 7].map((day) => (
                <button
                  key={day}
                  type="button"
                  onClick={() => handleChange("daysPerWeek", day)}
                  className={`flex-1 py-2.5 rounded-xl text-sm font-semibold transition-all duration-200 ${
                    (profile?.daysPerWeek ?? 0) >= day
                      ? "bg-emerald-500 text-white shadow-sm shadow-emerald-500/25"
                      : "bg-slate-100 text-slate-400 hover:bg-slate-200"
                  }`}
                >
                  {day}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* ─── Injuries ─── */}
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          <div className="px-6 py-4 border-b border-slate-50 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-lg bg-rose-50 flex items-center justify-center">
                <Shield className="w-3.5 h-3.5 text-rose-400" />
              </div>
              <h2 className="text-sm font-semibold text-slate-700">
                Injury History
              </h2>
            </div>
            {(profile?.injuries?.length ?? 0) > 0 && (
              <span className="text-xs font-medium px-2.5 py-1 rounded-full bg-rose-50 text-rose-500">
                {profile?.injuries?.length} selected
              </span>
            )}
          </div>
          <div className="p-6">
            <p className="text-xs text-slate-400 mb-4">
              Select any current or past injuries so we can customize your plan.
            </p>
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-2.5">
              {injuryOptions.map((injury) => {
                const checked = profile?.injuries?.includes(injury) ?? false;
                return (
                  <button
                    key={injury}
                    type="button"
                    onClick={() => handleInjuriesToggle(injury)}
                    className={`flex items-center gap-2 px-3 py-2.5 rounded-xl border text-sm font-medium transition-all duration-200 ${
                      checked
                        ? "border-rose-200 bg-rose-50 text-rose-600"
                        : "border-slate-100 bg-slate-50 text-slate-500 hover:border-slate-200 hover:bg-slate-100"
                    }`}
                  >
                    <div
                      className={`w-3.5 h-3.5 rounded border flex items-center justify-center shrink-0 transition-all ${
                        checked
                          ? "bg-rose-500 border-rose-500"
                          : "border-slate-300"
                      }`}
                    >
                      {checked && (
                        <svg
                          className="w-2 h-2 text-white"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke="currentColor"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth={3}
                            d="M5 13l4 4L19 7"
                          />
                        </svg>
                      )}
                    </div>
                    <span>{injury.replace(/_/g, " ")}</span>
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* ─── Save Button ─── */}
        <div className="flex justify-end">
          <button
            type="submit"
            disabled={isSaving}
            className="flex items-center gap-2.5 px-8 py-3 rounded-2xl bg-gradient-to-r from-indigo-600 to-violet-600 text-white text-sm font-semibold shadow-md shadow-indigo-500/25 hover:shadow-lg hover:shadow-indigo-500/30 transition-all duration-200 hover:-translate-y-0.5 disabled:opacity-60 disabled:cursor-not-allowed disabled:transform-none"
          >
            {isSaving ? (
              <>
                <div className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                Saving...
              </>
            ) : (
              <>
                <Save className="w-4 h-4" />
                Save Profile
              </>
            )}
          </button>
        </div>
      </form>
    </div>
  );
};

export default MemberProfile;
