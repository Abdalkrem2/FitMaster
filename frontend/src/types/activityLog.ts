export interface ActivityLogItem {
  id: number;
  actionType: String;
  entityType: String;
  entityId: number;
  performedByName: String;
  createdAt: String;
  details: String;
}
