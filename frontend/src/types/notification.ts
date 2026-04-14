export type NotificationType = "ACTIVITY_LOG";

export interface Notification {
  id: number;
  type: NotificationType;
  referenceId: number;
  message: string;
  details?: string;
  read: boolean;
  createdAt: string;
}

export interface NotificationResponse {
  content: Notification[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  lastPage: boolean;
  unreadCount: number;
}
