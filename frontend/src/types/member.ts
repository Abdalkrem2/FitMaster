import type { Membership } from "./membership";

export type FitnessGoal =
  | "MUSCLE_GAIN"
  | "WEIGHT_LOSS"
  | "ENDURANCE"
  | "GENERAL_FITNESS";

export type FitnessLevel = "BEGINNER" | "INTERMEDIATE" | "ADVANCED";

export type InjuryType =
  | "KNEE"
  | "SHOULDER"
  | "LOWER_BACK"
  | "UPPER_BACK"
  | "WRIST"
  | "ANKLE"
  | "NECK"
  | "ELBOW"
  | "HIP";

export type SplitType =
  | "FULL_BODY"
  | "UPPER_LOWER"
  | "BRO_SPLIT_4DAY"
  | "BRO_SPLIT_5DAY"
  | "PUSH_PULL_LEGS";

export type TrainingStyle = "STRENGTH" | "HYPERTROPHY" | "CIRCUIT";

export type AllergyType =
  | "GLUTEN"
  | "LACTOSE"
  | "NUTS"
  | "EGGS"
  | "SHELLFISH"
  | "SOY";

export interface MemberProfile {
  goal: FitnessGoal;
  fitnessLevel: FitnessLevel;
  injuries?: InjuryType[];
  weight?: number;
  height?: number;
  age?: number;
  trainingStyle?: TrainingStyle;
  splitType?: SplitType;
  hasDiabetes?: boolean;
  hasHeartConditions?: boolean;
  hasHypertension?: boolean;
  allergies?: AllergyType[];
}

export interface Member {
  id: string;
  fullName: string;
  phone: string;
  debt: number;
  addedByName: string;
  endDate: string;
  gender: "Male" | "Female";
}

export interface MemberDetails extends Member {
  startDate: string;
  profilePicture: string;
  profile?: MemberProfile;
  memberships: Membership[];
}

export interface CreateMemberRequest {
  fullName: string;
  phone: string;
  gender: string;
  profilePicture: string;
}

export interface UpdateMemberRequest {
  fullName?: string;
  phone?: string;
  profilePicture?: string;
}

export interface UpdateMemberProfileRequest {
  goal?: FitnessGoal;
  fitnessLevel: FitnessLevel;
  injuries?: InjuryType[] | null;
  weight?: number | null;
  height?: number | null;
  age?: number | null;
  trainingStyle?: TrainingStyle;
  splitType?: SplitType;
  hasDiabetes?: boolean;
  hasHeartConditions?: boolean;
  hasHypertension?: boolean;
  allergies?: AllergyType[];
}
